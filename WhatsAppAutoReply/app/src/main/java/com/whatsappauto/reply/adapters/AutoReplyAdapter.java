package com.whatsappauto.reply.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.whatsappauto.reply.databinding.ItemAutoReplyBinding;
import com.whatsappauto.reply.models.AutoReply;

public class AutoReplyAdapter extends ListAdapter<AutoReply, AutoReplyAdapter.ViewHolder> {
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AutoReply autoReply);
        void onSwitchChanged(AutoReply autoReply, boolean isEnabled);
        void onDeleteClick(AutoReply autoReply);
    }

    public AutoReplyAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<AutoReply> DIFF_CALLBACK = new DiffUtil.ItemCallback<AutoReply>() {
        @Override
        public boolean areItemsTheSame(@NonNull AutoReply oldItem, @NonNull AutoReply newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull AutoReply oldItem, @NonNull AutoReply newItem) {
            return oldItem.getKeyword().equals(newItem.getKeyword()) &&
                   oldItem.getReplyMessage().equals(newItem.getReplyMessage()) &&
                   oldItem.isEnabled() == newItem.isEnabled();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAutoReplyBinding binding = ItemAutoReplyBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemAutoReplyBinding binding;

        ViewHolder(ItemAutoReplyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(AutoReply autoReply) {
            binding.tvKeyword.setText("Keyword: " + autoReply.getKeyword());
            binding.tvReplyMessage.setText(autoReply.getReplyMessage());
            binding.switchEnabled.setChecked(autoReply.isEnabled());

            binding.switchEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (listener != null) {
                    listener.onSwitchChanged(autoReply, isChecked);
                }
            });

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(autoReply);
                }
            });

            binding.getRoot().setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(autoReply);
                }
                return true;
            });
        }
    }
}