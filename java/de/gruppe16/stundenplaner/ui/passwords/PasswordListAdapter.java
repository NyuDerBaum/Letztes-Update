package de.gruppe16.stundenplaner.ui.passwords;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.gruppe16.stundenplaner.R;
import de.gruppe16.stundenplaner.password.PasswordDataEntry;

public class PasswordListAdapter extends RecyclerView.Adapter<PasswordListAdapter.PasswordViewHolder> {

    private List<PasswordDataEntry> passwordEntries;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PasswordDataEntry entry, int position);
    }

    public PasswordListAdapter(List<PasswordDataEntry> passwordEntries, OnItemClickListener listener) {
        this.passwordEntries = passwordEntries;
        this.listener = listener;
    }

    @Override
    public PasswordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.password_item, parent, false);
        return new PasswordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PasswordViewHolder holder, int position) {
        PasswordDataEntry entry = passwordEntries.get(position);
        holder.bind(entry, listener);
    }

    @Override
    public int getItemCount() {
        return passwordEntries.size();
    }

    public static class PasswordViewHolder extends RecyclerView.ViewHolder {

        TextView passwordTitle, passwordUsername;

        public PasswordViewHolder(View itemView) {
            super(itemView);
            passwordTitle = itemView.findViewById(R.id.passwordTitle);
            passwordUsername = itemView.findViewById(R.id.passwordUsername);
        }

        public void bind(final PasswordDataEntry entry, final OnItemClickListener listener) {
            passwordTitle.setText(entry.getTitle());
            passwordUsername.setText(entry.getUsername());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    listener.onItemClick(entry, position);
                }
            });
        }
    }
    public void updateData(List<PasswordDataEntry> newEntries) {
        this.passwordEntries = newEntries;
        notifyDataSetChanged();  // Notify the adapter that the data set has changed
    }

}
