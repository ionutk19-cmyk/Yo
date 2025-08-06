package com.whatsappautoresponder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.whatsappautoresponder.databinding.FragmentScheduledMessagesBinding
import com.whatsappautoresponder.ui.adapter.ScheduledMessagesAdapter
import com.whatsappautoresponder.ui.viewmodel.ScheduledMessagesViewModel

class ScheduledMessagesFragment : Fragment() {

    private var _binding: FragmentScheduledMessagesBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: ScheduledMessagesViewModel
    private lateinit var adapter: ScheduledMessagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduledMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViewModel()
        setupRecyclerView()
        observeData()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[ScheduledMessagesViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = ScheduledMessagesAdapter { message ->
            // Handle message click/edit
            viewModel.deleteMessage(message)
        }
        
        binding.recyclerViewMessages.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ScheduledMessagesFragment.adapter
        }
    }

    private fun observeData() {
        viewModel.allMessages.observe(viewLifecycleOwner) { messages ->
            adapter.submitList(messages)
            
            if (messages.isEmpty()) {
                binding.textViewEmpty.visibility = View.VISIBLE
                binding.recyclerViewMessages.visibility = View.GONE
            } else {
                binding.textViewEmpty.visibility = View.GONE
                binding.recyclerViewMessages.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}