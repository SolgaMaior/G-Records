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
import android.widget.Button;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class active_member extends Fragment implements SearchableFragment {

    private boolean sortAscending = true;
    private MemberInfoAdapter adapter;
    private ArrayList<MemberModelClass> memberList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.active_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_active);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get data from repository and filter for active
        memberList = new ArrayList<>();
        for (MemberModelClass m : MemberRepository.getInstance().getMembers()) {
            if (isActive(m)) {
                memberList.add(m);
            }
        }
        sortList();

        adapter = new MemberInfoAdapter(getContext(), new ArrayList<>(memberList));
        recyclerView.setAdapter(adapter);

        // Sort toggle button
        Button sortBtn = view.findViewById(R.id.btn_sort_toggle);
        sortBtn.setOnClickListener(v -> {
            sortAscending = !sortAscending;
            sortList();
            adapter.updateList(new ArrayList<>(memberList));
        });

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

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the list from the repository and filter for active
        memberList.clear();
        for (MemberModelClass m : MemberRepository.getInstance().getMembers()) {
            if (isActive(m)) {
                memberList.add(m);
            }
        }
        sortList();
        if (adapter != null) {
            adapter.updateList(new ArrayList<>(memberList));
        }
    }

    private int parseDate(String dateStr) {
        // Try to parse as yyyyMMdd, MM/dd/yyyy, or fallback to digits only
        try {
            if (dateStr.matches("\\d{8}")) {
                return Integer.parseInt(dateStr);
            } else if (dateStr.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                Date date = sdf.parse(dateStr);
                SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMdd");
                return Integer.parseInt(ymd.format(date));
            } else {
                // Remove non-digits and try
                return Integer.parseInt(dateStr.replaceAll("[^0-9]", ""));
            }
        } catch (Exception e) {
            return 0; // fallback for invalid date
        }
    }

    private void sortList() {
        Collections.sort(memberList, new Comparator<MemberModelClass>() {
            @Override
            public int compare(MemberModelClass m1, MemberModelClass m2) {
                int d1 = parseDate(m1.getEnd_date());
                int d2 = parseDate(m2.getEnd_date());
                return sortAscending ? Integer.compare(d1, d2) : Integer.compare(d2, d1);
            }
        });
    }

    @Override
    public void onSearchQuery(String query) {
        if (adapter != null) {
            adapter.getFilter().filter(query);
        }
    }

    private boolean isActive(MemberModelClass member) {
        int today = getTodayInt();
        int end = parseDate(member.getEnd_date());
        return end >= today;
    }

    private int getTodayInt() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return year * 10000 + month * 100 + day;
    }
}
