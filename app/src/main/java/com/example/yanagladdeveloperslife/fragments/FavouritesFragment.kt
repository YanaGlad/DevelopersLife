package com.example.yanagladdeveloperslife.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.yanagladdeveloperslife.adapters.GifsRecyclerAdapter
import com.example.yanagladdeveloperslife.databinding.FragmentFavouritesBinding
import com.example.yanagladdeveloperslife.values.ErrorHandler
import com.example.yanagladdeveloperslife.viewmodel.FavouritesFragmentViewModel
import com.example.yanagladdeveloperslife.viewmodel.RecyclerFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment() {
    var isOnScreen = false
    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!
    private var gifsRecyclerAdapter: GifsRecyclerAdapter? = null
    private val favouritesFragmentViewModel: FavouritesFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favouritesFragmentViewModel.favsList.observe(viewLifecycleOwner) { gifs ->
            if (gifs != null) {
                gifsRecyclerAdapter?.submitList(gifs)
                binding.recyclerview.adapter = gifsRecyclerAdapter
            }
        }
    }

    companion object {

        fun newInstance(): FavouritesFragment {
            return FavouritesFragment()
        }
    }
}