package com.example.yanagladdeveloperslife.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.yanagladdeveloperslife.MainActivity
import com.example.yanagladdeveloperslife.R
import com.example.yanagladdeveloperslife.adapters.GifsRecyclerAdapter
import com.example.yanagladdeveloperslife.databinding.FragmentRecycleBinding
import com.example.yanagladdeveloperslife.values.ErrorHandler
import com.example.yanagladdeveloperslife.values.PageOperation
import com.example.yanagladdeveloperslife.viewmodel.RecyclerFragmentViewModel

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecyclerFragment : ButtonSupportedFragment() {
    var isOnScreen = false
    private var type: String? = null
    private val recyclerFragmentViewModel: RecyclerFragmentViewModel by viewModels()
    private var gifsRecyclerAdapter: GifsRecyclerAdapter? = null

    private var _binding: FragmentRecycleBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            type = requireArguments().getString("TAB_TYPE")
            recyclerFragmentViewModel.setType(type)
        } else {
            Log.e("TAG_MAIN_FRAG", "No arguments in bundle")
        }

        loadGifs(PageOperation.STAND, type!!)
    }


    private fun loadGifs(pageOperation: PageOperation?, type: String) {
        recyclerFragmentViewModel.setCanLoadNext(recyclerFragmentViewModel.error.value == ErrorHandler.SUCCESS)
        recyclerFragmentViewModel.currentPage.value?.let {
            if (pageOperation != null) {
                recyclerFragmentViewModel.setCurrentPage(
                    it, pageOperation
                )
            }
        }
        recyclerFragmentViewModel.loadRecyclerGifs(type)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecycleBinding.inflate(layoutInflater)

        btnPrev = binding.btnPrevious
        btnNex = binding.btnNext

        setupRecycler()
        setupOnNextOnPrevListeners()
        setupObservers()

        return binding.root
    }

    private fun setupRecycler() {
        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = GridLayoutManager(context, 1)
        gifsRecyclerAdapter = recyclerFragmentViewModel.type.value?.let {
            GifsRecyclerAdapter(
                it
            )
        }
    }

    private fun setupOnNextOnPrevListeners() {
        onNextClickListener = View.OnClickListener {
            type?.let { type ->
                loadGifs(
                    PageOperation.NEXT,
                    type
                )
            }
        }
        onPrevClickListener = View.OnClickListener {
            type?.let { type ->
                loadGifs(
                    PageOperation.PREVIOUS,
                    type
                )
            }
        }
        binding.btnNext.setOnClickListener(onNextClickListener)
        binding.btnPrevious.setOnClickListener(onPrevClickListener)

    }

    private fun setupObservers() {
        recyclerFragmentViewModel.getGifModels().observe(viewLifecycleOwner) { gifs ->
            if (gifs != null) {
                recyclerFragmentViewModel.setError(ErrorHandler.SUCCESS)

                gifsRecyclerAdapter?.submitList(gifs)
                binding.recyclerview.adapter = gifsRecyclerAdapter
            }
        }
        recyclerFragmentViewModel.error.observe(viewLifecycleOwner) { e ->
            recyclerFragmentViewModel.updateCanLoadPrevious()
            recyclerFragmentViewModel.setCanLoadNext(true)
            binding.recycleErrorProgressbar.visibility = View.INVISIBLE
            if (e != ErrorHandler.SUCCESS) {
                binding.recyclerview.visibility = View.GONE
                binding.recyclErrorLayout.visibility = View.VISIBLE
                if (e == ErrorHandler.LOAD_ERROR) binding.recyclErrorTitle.text =
                    "${context?.getString(R.string.errorr)} ${context?.getString(R.string.no_internet)} "
                binding.recyclErrorBtn.setOnClickListener {
                    if (binding.recycleErrorProgressbar.visibility == View.INVISIBLE) {
                        binding.recycleErrorProgressbar.visibility = View.VISIBLE
                        if (e == ErrorHandler.LOAD_ERROR) {
                            type?.let { loadGifs(PageOperation.STAND, it) }
                        }
                    }
                }
            } else {
                binding.recyclErrorLayout.visibility = View.GONE
                binding.recyclerview.visibility = View.VISIBLE
            }
        }
        recyclerFragmentViewModel.getCanLoadNext().observe(viewLifecycleOwner) { enabled ->
            if (isOnScreen) btnNex.isEnabled = enabled
        }
        recyclerFragmentViewModel.getCanLoadPrevious().observe(viewLifecycleOwner) { enabled ->
            if (isOnScreen) btnPrev.isEnabled = enabled
        }
    }

    override fun nextEnabled(): Boolean {
        return recyclerFragmentViewModel.getCanLoadNext().value == true
    }

    override fun previousEnabled(): Boolean {
        return recyclerFragmentViewModel.getCanLoadPrevious().value!!
    }

    companion object {

        fun newInstance(type: String?): RecyclerFragment {
            val fragment = RecyclerFragment()
            val bundle = Bundle()
            bundle.putString("TAB_TYPE", type)
            fragment.arguments = bundle
            return fragment
        }
    }
}