package com.example.yanagladdeveloperslife.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.yanagladdeveloperslife.adapters.GifsRecyclerAdapter
import com.example.yanagladdeveloperslife.databinding.FragmentFavouritesBinding
import com.example.yanagladdeveloperslife.models.GifModel
import com.example.yanagladdeveloperslife.viewmodel.FavouritesFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment(), FavouriteHelper {
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

    private fun setupRecycler() {
        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = GridLayoutManager(context, 1)
        gifsRecyclerAdapter = GifsRecyclerAdapter(
            this, "favs"
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()

        favouritesFragmentViewModel.favsList.observe(viewLifecycleOwner) {
            gifsRecyclerAdapter?.submitList(favouritesFragmentViewModel.favsList.value)
            binding.recyclerview.adapter = gifsRecyclerAdapter

        }
    }

    companion object {

        fun newInstance(): FavouritesFragment {
            return FavouritesFragment()
        }
    }

    override fun addToFavs(gifModel: GifModel) {
         //TODO remove from db
    }
}