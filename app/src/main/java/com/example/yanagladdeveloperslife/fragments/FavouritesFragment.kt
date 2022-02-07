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
import com.example.yanagladdeveloperslife.viewmodel.RecyclerFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class FavouritesFragment : Fragment(), FavouriteHelper {

    var isOnScreen = false
    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!
    private var gifsRecyclerAdapter: GifsRecyclerAdapter? = null
    private val favouritesFragmentViewModel: RecyclerFragmentViewModel by viewModels()

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentFavouritesBinding.inflate(layoutInflater).root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()

        favouritesFragmentViewModel.favsList.observe(viewLifecycleOwner) {
            gifsRecyclerAdapter?.submitList(favouritesFragmentViewModel.favsList.value)
            binding.recyclerview.adapter = gifsRecyclerAdapter
        }
    }
    override fun onDestroy() {
        super.onDestroy()

        favouritesFragmentViewModel.dispose()
        compositeDisposable.dispose()
    }

    private fun setupRecycler() {
        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = GridLayoutManager(context, 1)
        gifsRecyclerAdapter = GifsRecyclerAdapter(
            this, "favs"
        )
    }

    override fun addToFavs(gifModel: GifModel) {
        compositeDisposable.add(Observable.just(favouritesFragmentViewModel)
            .subscribeOn(Schedulers.io())
            .subscribe { db ->
                db.deleteGifFromFavs(gifModel)
            })
    }

    override fun getAllFavs(): List<GifModel> {
        return favouritesFragmentViewModel.favsList.value!!
    }

    companion object {
        fun newInstance(): FavouritesFragment {
            return FavouritesFragment()
        }
    }
}
