package com.whatsappautoresponder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.whatsappautoresponder.databinding.FragmentAutoReplyRulesBinding
import com.whatsappautoresponder.ui.adapter.AutoReplyRulesAdapter
import com.whatsappautoresponder.ui.viewmodel.AutoReplyRulesViewModel

class AutoReplyRulesFragment : Fragment() {

    private var _binding: FragmentAutoReplyRulesBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: AutoReplyRulesViewModel
    private lateinit var adapter: AutoReplyRulesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAutoReplyRulesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViewModel()
        setupRecyclerView()
        observeData()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[AutoReplyRulesViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = AutoReplyRulesAdapter { rule ->
            // Handle rule click/edit
            viewModel.deleteRule(rule)
        }
        
        binding.recyclerViewRules.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@AutoReplyRulesFragment.adapter
        }
    }

    private fun observeData() {
        viewModel.allRules.observe(viewLifecycleOwner) { rules ->
            adapter.submitList(rules)
            
            if (rules.isEmpty()) {
                binding.textViewEmpty.visibility = View.VISIBLE
                binding.recyclerViewRules.visibility = View.GONE
            } else {
                binding.textViewEmpty.visibility = View.GONE
                binding.recyclerViewRules.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}