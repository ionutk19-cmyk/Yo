package com.example.calendarapp.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendarapp.databinding.FragmentEventsBinding
import com.example.calendarapp.ui.events.adapter.EventsAdapter
import com.example.calendarapp.ui.ViewModelFactory
import com.example.calendarapp.repository.EventRepository
import com.example.calendarapp.CalendarApplication
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: EventsViewModel by viewModels { 
        ViewModelFactory(EventRepository((requireActivity().application as CalendarApplication).database.eventDao()))
    }
    private lateinit var eventsAdapter: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        eventsAdapter = EventsAdapter { event ->
            // Handle event click
        }
        
        binding.recyclerEvents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventsAdapter
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collectLatest { events ->
                eventsAdapter.submitList(events)
                binding.tvNoEvents.visibility = if (events.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}