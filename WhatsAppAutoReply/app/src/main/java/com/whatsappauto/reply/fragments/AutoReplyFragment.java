package com.whatsappauto.reply.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.whatsappauto.reply.R;
import com.whatsappauto.reply.adapters.AutoReplyAdapter;
import com.whatsappauto.reply.databinding.FragmentAutoReplyBinding;
import com.whatsappauto.reply.models.AutoReply;
import com.whatsappauto.reply.viewmodels.AutoReplyViewModel;

public class AutoReplyFragment extends Fragment {
    private FragmentAutoReplyBinding binding;
    private AutoReplyViewModel viewModel;
    private AutoReplyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAutoReplyBinding.inflate(inflater, container, false);
        
        viewModel = new ViewModelProvider(this).get(AutoReplyViewModel.class);
        
        setupRecyclerView();
        observeData();
        
        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new AutoReplyAdapter(new AutoReplyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AutoReply autoReply) {
                // Handle item click if needed
            }

            @Override
            public void onSwitchChanged(AutoReply autoReply, boolean isEnabled) {
                autoReply.setEnabled(isEnabled);
                viewModel.update(autoReply);
            }

            @Override
            public void onDeleteClick(AutoReply autoReply) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Auto Reply")
                        .setMessage("Are you sure you want to delete this auto reply?")
                        .setPositiveButton("Delete", (dialog, which) -> viewModel.delete(autoReply))
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void observeData() {
        viewModel.getAllAutoReplies().observe(getViewLifecycleOwner(), autoReplies -> {
            adapter.submitList(autoReplies);
            binding.tvEmpty.setVisibility(autoReplies.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    public void showAddDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_auto_reply, null);
        EditText editKeyword = dialogView.findViewById(R.id.editKeyword);
        EditText editReplyMessage = dialogView.findViewById(R.id.editReplyMessage);

        new AlertDialog.Builder(getContext())
                .setTitle("Add Auto Reply")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String keyword = editKeyword.getText().toString().trim();
                    String replyMessage = editReplyMessage.getText().toString().trim();
                    
                    if (!keyword.isEmpty() && !replyMessage.isEmpty()) {
                        AutoReply autoReply = new AutoReply(keyword, replyMessage);
                        viewModel.insert(autoReply);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}