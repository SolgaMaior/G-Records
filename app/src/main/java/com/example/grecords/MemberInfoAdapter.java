package com.example.grecords;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemberInfoAdapter extends RecyclerView.Adapter<MemberInfoAdapter.ViewHolder> implements Filterable {

    private final Context context;
    private ArrayList<MemberModelClass> memberList;
    private ArrayList<MemberModelClass> memberListFull; // For filtering
    private OnMemberActionListener actionListener;

    public interface OnMemberActionListener {

        void onEdit(MemberModelClass member, int position);

        void onDelete(MemberModelClass member, int position);
    }

    public void setOnMemberActionListener(OnMemberActionListener listener) {
        this.actionListener = listener;
    }

    public MemberInfoAdapter(Context context, ArrayList<MemberModelClass> memberList) {
        this.context = context;
        this.memberList = memberList;
        this.memberListFull = new ArrayList<>(memberList);
    }

    @NonNull
    @Override
    public MemberInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberInfoAdapter.ViewHolder holder, int position) {
        MemberModelClass member = memberList.get(position);
        holder.nameTextView.setText(member.getMember_name());
        String duration = "Start: " + member.getStart_date() + "\nEnd: " + member.getEnd_date();
        holder.durationTextView.setText(duration);

        holder.editButton.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onEdit(member, position);
            }
        });
        holder.deleteButton.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDelete(member, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, durationTextView;
        ImageButton editButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.member_name);
            durationTextView = itemView.findViewById(R.id.duration);
            editButton = itemView.findViewById(R.id.btn_edit);
            deleteButton = itemView.findViewById(R.id.btn_delete);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<MemberModelClass> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(memberListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (MemberModelClass item : memberListFull) {
                        if (item.getMember_name().toLowerCase().contains(filterPattern)
                                || String.valueOf(item.getStart_date()).contains(filterPattern)
                                || String.valueOf(item.getEnd_date()).contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                memberList.clear();
                memberList.addAll((ArrayList) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public void updateList(ArrayList<MemberModelClass> newList) {
        memberList = newList;
        memberListFull = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        memberList.remove(position);
        notifyItemRemoved(position);
    }
}
