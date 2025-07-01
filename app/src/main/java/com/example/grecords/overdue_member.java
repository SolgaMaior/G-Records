package com.example.grecords;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import com.example.grecords.MemberInfoAdapter;
import com.example.grecords.MemberModelClass;
import com.example.grecords.MemberRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass. Use the
 * {@link overdue_member#newInstance} factory method to create an instance of
 * this fragment.
 */
public class overdue_member extends Fragment implements SearchableFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MemberInfoAdapter adapter;
    private ArrayList<MemberModelClass> memberList;

    public overdue_member() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment overdue_member.
     */
    // TODO: Rename and change types and number of parameters
    public static overdue_member newInstance(String param1, String param2) {
        overdue_member fragment = new overdue_member();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the list from the repository and filter for overdue
        memberList.clear();
        for (MemberModelClass m : MemberRepository.getInstance().getMembers()) {
            if (isOverdue(m)) {
                memberList.add(m);
            }
        }
        sortList();
        if (adapter != null) {
            adapter.updateList(new ArrayList<>(memberList));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overdue_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.overdue_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get data from repository and filter for overdue
        memberList = new ArrayList<>();
        for (MemberModelClass m : MemberRepository.getInstance().getMembers()) {
            if (isOverdue(m)) {
                memberList.add(m);
            }
        }
        sortList();

        adapter = new MemberInfoAdapter(getContext(), new ArrayList<>(memberList));
        recyclerView.setAdapter(adapter);

        // Edit/Delete actions
        adapter.setOnMemberActionListener(new MemberInfoAdapter.OnMemberActionListener() {
            @Override
            public void onEdit(MemberModelClass member, int position) {
                // Show edit dialog (implement as needed)
            }

            @Override
            public void onDelete(MemberModelClass member, int position) {
                memberList.remove(position);
                MemberRepository.getInstance().removeMember(member);
                adapter.updateList(new ArrayList<>(memberList));
            }
        });
        return view;
    }

    private int parseDate(String dateStr) {
        try {
            if (dateStr.matches("\\d{8}")) {
                return Integer.parseInt(dateStr);
            } else if (dateStr.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                Date date = sdf.parse(dateStr);
                SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMdd");
                return Integer.parseInt(ymd.format(date));
            } else {
                return Integer.parseInt(dateStr.replaceAll("[^0-9]", ""));
            }
        } catch (Exception e) {
            return 0;
        }
    }

    private void sortList() {
        Collections.sort(memberList, new Comparator<MemberModelClass>() {
            @Override
            public int compare(MemberModelClass m1, MemberModelClass m2) {
                int d1 = parseDate(m1.getEnd_date());
                int d2 = parseDate(m2.getEnd_date());
                return Integer.compare(d1, d2);
            }
        });
    }

    private boolean isOverdue(MemberModelClass member) {
        int today = getTodayInt();
        int end = parseDate(member.getEnd_date());
        return end < today;
    }

    private int getTodayInt() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return year * 10000 + month * 100 + day;
    }

    @Override
    public void onSearchQuery(String query) {
        if (adapter != null) {
            adapter.getFilter().filter(query);
        }
    }
}
