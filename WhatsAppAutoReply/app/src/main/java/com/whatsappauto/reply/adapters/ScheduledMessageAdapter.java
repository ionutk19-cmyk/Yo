package com.whatsappauto.reply.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.whatsappauto.reply.databinding.ItemScheduledMessageBinding;
import com.whatsappauto.reply.models.ScheduledMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScheduledMessageAdapter extends ListAdapter<ScheduledMessage, ScheduledMessageAdapter.ViewHolder> {
    private OnItemClickListener listener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());

    public interface OnItemClickListener {
        void onItemClick(ScheduledMessage message);
        void onDeleteClick(ScheduledMessage message);
    }

    public ScheduledMessageAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<ScheduledMessage> DIFF_CALLBACK = new DiffUtil.ItemCallback<ScheduledMessage>() {
        @Override
        public boolean areItemsTheSame(@NonNull ScheduledMessage oldItem, @NonNull ScheduledMessage newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ScheduledMessage oldItem, @NonNull ScheduledMessage newItem) {
            return oldItem.getContactName().equals(newItem.getContactName()) &&
                   oldItem.getMessage().equals(newItem.getMessage()) &&
                   oldItem.getScheduledTime() == newItem.getScheduledTime() &&
                   oldItem.isSent() == newItem.isSent();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemScheduledMessageBinding binding = ItemScheduledMessageBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemScheduledMessageBinding binding;

        ViewHolder(ItemScheduledMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ScheduledMessage message) {
            binding.tvContactName.setText("To: " + message.getContactName());
            binding.tvMessage.setText(message.getMessage());
            
            String status = message.isSent() ? " (Sent)" : "";
            binding.tvScheduledTime.setText(dateFormat.format(new Date(message.getScheduledTime())) + status);

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(message);
                }
            });

            binding.getRoot().setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(message);
                }
                return true;
            });
        }
    }
}