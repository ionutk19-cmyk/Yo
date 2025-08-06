package com.whatsappauto.reply.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.whatsappauto.reply.R;
import com.whatsappauto.reply.adapters.ScheduledMessageAdapter;
import com.whatsappauto.reply.databinding.FragmentAutoReplyBinding;
import com.whatsappauto.reply.models.ScheduledMessage;
import com.whatsappauto.reply.viewmodels.ScheduledMessageViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ScheduledMessageFragment extends Fragment {
    private FragmentAutoReplyBinding binding;
    private ScheduledMessageViewModel viewModel;
    private ScheduledMessageAdapter adapter;
    private long selectedTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAutoReplyBinding.inflate(inflater, container, false);
        
        viewModel = new ViewModelProvider(this).get(ScheduledMessageViewModel.class);
        
        setupRecyclerView();
        observeData();
        
        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new ScheduledMessageAdapter(new ScheduledMessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ScheduledMessage message) {
                // Handle item click if needed
            }

            @Override
            public void onDeleteClick(ScheduledMessage message) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Scheduled Message")
                        .setMessage("Are you sure you want to delete this scheduled message?")
                        .setPositiveButton("Delete", (dialog, which) -> viewModel.delete(message))
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void observeData() {
        viewModel.getAllMessages().observe(getViewLifecycleOwner(), messages -> {
            adapter.submitList(messages);
            binding.tvEmpty.setVisibility(messages.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    public void showAddDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_scheduled_message, null);
        EditText editContactName = dialogView.findViewById(R.id.editContactName);
        EditText editMessage = dialogView.findViewById(R.id.editMessage);
        Button btnSelectTime = dialogView.findViewById(R.id.btnSelectTime);
        TextView tvSelectedTime = dialogView.findViewById(R.id.tvSelectedTime);

        btnSelectTime.setOnClickListener(v -> showDateTimePicker(tvSelectedTime));

        new AlertDialog.Builder(getContext())
                .setTitle("Add Scheduled Message")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String contactName = editContactName.getText().toString().trim();
                    String message = editMessage.getText().toString().trim();
                    
                    if (!contactName.isEmpty() && !message.isEmpty() && selectedTime > 0) {
                        ScheduledMessage scheduledMessage = new ScheduledMessage(contactName, message, selectedTime);
                        viewModel.insert(scheduledMessage);
                        selectedTime = 0;
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDateTimePicker(TextView tvSelectedTime) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                            (view1, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                
                                selectedTime = calendar.getTimeInMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
                                tvSelectedTime.setText(sdf.format(new Date(selectedTime)));
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true);
                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}