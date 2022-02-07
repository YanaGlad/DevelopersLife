package com.example.yanagladdeveloperslife.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.yanagladdeveloperslife.ErrorHandler
import com.example.yanagladdeveloperslife.PageOperation
import com.example.yanagladdeveloperslife.R
import com.example.yanagladdeveloperslife.adapters.GifsRecyclerAdapter
import com.example.yanagladdeveloperslife.databinding.FragmentRecycleBinding
import com.example.yanagladdeveloperslife.models.GifModel
import com.example.yanagladdeveloperslife.viewmodel.RecyclerFragmentViewModel

import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

interface FavouriteHelper {
    fun addToFavs(gifModel: GifModel)
    fun getAllFavs(): List<GifModel>
}

@AndroidEntryPoint
class RecyclerFragment : Fragment(), FavouriteHelper, Clickable {

    var isOnScreen = false

    private var type: String? = null
    private val recyclerFragmentViewModel: RecyclerFragmentViewModel by viewModels()
    private var gifsRecyclerAdapter: GifsRecyclerAdapter? = null

    private var _binding: FragmentRecycleBinding? = null
    private val binding get() = _binding!!

    private val compositeDisposable = CompositeDisposable()

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
        recyclerFragmentViewModel.canLoadNext = MutableLiveData(recyclerFragmentViewModel.error.value == ErrorHandler.SUCCESS)
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
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecycleBinding.inflate(layoutInflater)

        with(binding) {
            setupRecycler()
            setupOnNextOnPrevListeners()
            setupObservers()
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun FragmentRecycleBinding.setupRecycler() {
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = GridLayoutManager(context, 1)
        gifsRecyclerAdapter = recyclerFragmentViewModel.type.value?.let {
            GifsRecyclerAdapter(
                this@RecyclerFragment,
                it
            )
        }
    }

    private fun FragmentRecycleBinding.setupOnNextOnPrevListeners() {
        val onNextClickListener = View.OnClickListener { type?.let { type -> loadGifs(PageOperation.NEXT, type) } }
        val onPrevClickListener = View.OnClickListener { type?.let { type -> loadGifs(PageOperation.PREVIOUS, type) } }

        buttonsLayout.btnNext.setOnClickListener(onNextClickListener)
        buttonsLayout.btnPrevious.setOnClickListener(onPrevClickListener)
    }

    private fun FragmentRecycleBinding.setupObservers() {
        recyclerFragmentViewModel.getGifModels().observe(viewLifecycleOwner) { gifs ->
            if (gifs != null) {
                recyclerFragmentViewModel.setError(ErrorHandler.SUCCESS)
                gifsRecyclerAdapter?.submitList(gifs)
                recyclerview.adapter = gifsRecyclerAdapter
            }
        }
        recyclerFragmentViewModel.error.observe(viewLifecycleOwner) { e ->
            recyclerFragmentViewModel.updateCanLoadPrevious()
            recyclerFragmentViewModel.canLoadNext = MutableLiveData(true)

            recycleErrorProgressbar.visibility = View.INVISIBLE

            if (e != ErrorHandler.SUCCESS) {
                recyclerview.visibility = View.GONE
                recyclErrorLayout.visibility = View.VISIBLE

                if (e == ErrorHandler.LOAD_ERROR) binding.recyclErrorTitle.text =
                    "${context?.getString(R.string.errorr)} ${context?.getString(R.string.no_internet)} "

                recyclErrorBtn.setOnClickListener {
                    if (recycleErrorProgressbar.visibility == View.INVISIBLE) {
                        recycleErrorProgressbar.visibility = View.VISIBLE
                        if (e == ErrorHandler.LOAD_ERROR) {
                            type?.let { loadGifs(PageOperation.STAND, it) }
                        }
                    }
                }
            } else {
                recyclErrorLayout.visibility = View.GONE
                recyclerview.visibility = View.VISIBLE
            }
        }
        recyclerFragmentViewModel.canLoadNext.observe(viewLifecycleOwner) { enabled ->
            if (isOnScreen) buttonsLayout.btnNext.isEnabled = enabled
        }
        recyclerFragmentViewModel.canLoadPrevious.observe(viewLifecycleOwner) { enabled ->
            if (isOnScreen) buttonsLayout.btnPrevious.isEnabled = enabled
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG_RESUME", "HEREEEE")
        gifsRecyclerAdapter?.submitList(recyclerFragmentViewModel.getGifModels().value!!)
    }

    override fun nextEnabled(): Boolean {
        return recyclerFragmentViewModel.canLoadNext.value == true
    }

    override fun previousEnabled(): Boolean {
        return recyclerFragmentViewModel.canLoadPrevious.value!!
    }

    override fun addToFavs(gifModel: GifModel) {
        compositeDisposable.add(Observable.just(recyclerFragmentViewModel)
            .subscribeOn(Schedulers.io())
            .subscribe { db ->
                db.addGifToDb(gifModel)
            })
    }

    override fun getAllFavs(): List<GifModel> {
        return recyclerFragmentViewModel.favsList.value!!
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
