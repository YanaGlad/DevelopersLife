package com.example.yanagladdeveloperslife.fragments

import android.content.res.AssetManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.yanagladdeveloperslife.ErrorHandler
import com.example.yanagladdeveloperslife.R
import com.example.yanagladdeveloperslife.databinding.FragmentRandomBinding
import com.example.yanagladdeveloperslife.models.GifModel
import com.example.yanagladdeveloperslife.viewmodel.RandomFragmentViewModel
import com.example.yanagladdeveloperslife.viewstate.RandomGifViewState
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

@AndroidEntryPoint
class RandomFragment : Fragment(), Clickable {
    var isOnScreen = false

    private var _binding: FragmentRandomBinding? = null
    private val binding get() = _binding!!

    private val randomFragmentViewModel: RandomFragmentViewModel by viewModels()
    private lateinit var savedUrl: String

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentRandomBinding.inflate(layoutInflater).root

    override fun onDestroy() {
        super.onDestroy()
        randomFragmentViewModel.dispose()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            setupButtonListeners()
            setupObservers()
        }
        randomFragmentViewModel.loadRandomGif()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
        randomFragmentViewModel.dispose()
    }

    private fun FragmentRandomBinding.setupObservers() {
        randomFragmentViewModel.getCurrentGif().observe(viewLifecycleOwner) { gif: GifModel? ->
            if (gif != null) {
                loadDescription.text = gif.description
                loadAuthor.text = "By ${gif.author}"
            }
        }
        randomFragmentViewModel.getIsCurrentGifLoaded().observe(
            viewLifecycleOwner
        ) { isLoaded ->
            if (isLoaded) loadProgressbar.visibility = View.INVISIBLE else loadProgressbar.visibility = View.VISIBLE
        }
        randomFragmentViewModel.canLoadNext.observe(viewLifecycleOwner) { enabled: Boolean? ->
            if (isOnScreen) buttonsLayout.btnNext.isEnabled = enabled!!
        }

        randomFragmentViewModel.canLoadPrevious.observe(viewLifecycleOwner) { enabled ->
            if (isOnScreen) buttonsLayout.btnPrevious.isEnabled = enabled
        }

        randomFragmentViewModel.getError().observe(viewLifecycleOwner) { e ->
            randomFragmentViewModel.updateCanLoadPrevious()
            recycleErrorProgressbar.visibility = View.INVISIBLE
            if (e != ErrorHandler.SUCCESS) {
                loadLinearLayout.visibility = View.GONE
                when (e) {
                    ErrorHandler.LOAD_ERROR -> {
                        recyclErrorBtn.visibility = View.VISIBLE
                        randomFragmentViewModel.canLoadNext = MutableLiveData(false)
                        loadImage.setBackgroundResource(R.drawable.waiting_background)
                    }
                    ErrorHandler.IMAGE_ERROR -> {
                        recyclErrorBtn.visibility = View.VISIBLE
                        setupErrorParams(requireContext().assets)
                        randomFragmentViewModel.canLoadNext = MutableLiveData(false)
                        loadImage.setBackgroundResource(R.drawable.waiting_background)
                    }
                    else -> {
                    }
                }
                recyclErrorBtn.setOnClickListener {
                    if (recycleErrorProgressbar.visibility == View.INVISIBLE) {
                        recycleErrorProgressbar.visibility = View.VISIBLE
                        randomFragmentViewModel.loadRandomGif()
                    }
                }
            } else {
                recyclErrorBtn.visibility = View.GONE
                recycleErrorProgressbar.visibility = View.GONE
                loadLinearLayout.visibility = View.VISIBLE
            }
        }

        randomFragmentViewModel.viewState.observe(viewLifecycleOwner) {
            handleViewState(it)
        }
    }

    private fun FragmentRandomBinding.setupButtonListeners() {
        val onPrevClickListener = View.OnClickListener {
            if (!randomFragmentViewModel.goBack()) Log.e("Cache is empty", "No cached gifResponses")
            else loadGifWithGlide(randomFragmentViewModel.getCurrentGif().value?.gifURL)

            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        val onNextClickListener = View.OnClickListener { loadGif() }
        buttonsLayout.btnPrevious.setOnClickListener(onPrevClickListener)
        buttonsLayout.btnNext.setOnClickListener(onNextClickListener)

        favsButton.setOnClickListener {
            compositeDisposable.add(Observable.just(randomFragmentViewModel)
                .subscribeOn(Schedulers.io())
                .subscribe { db ->
                    db.addGifToDb(randomFragmentViewModel.getCurrentGif().value!!)
                }
            )
            favsButton.setColorFilter(Color.RED)
        }
    }

    private fun FragmentRandomBinding.onGifLoaded(viewState: RandomGifViewState.Loaded) {
        randomFragmentViewModel.addGifModel(viewState.gifResponse.createGifModel())
        loadGifWithGlide(randomFragmentViewModel.getCurrentGif().value?.gifURL)
        recyclErrorBtn.visibility = View.GONE
        recycleErrorProgressbar.visibility = View.GONE
        loadLinearLayout.visibility = View.VISIBLE
        randomFragmentViewModel.setAppError(ErrorHandler.SUCCESS)
    }

    private fun FragmentRandomBinding.handleViewState(viewState: RandomGifViewState?) {

        when (viewState) {
            is RandomGifViewState.Loaded -> {
                onGifLoaded(viewState)
            }
            is RandomGifViewState.Loading -> {

            }
            is RandomGifViewState.Error.NetworkError -> {
                randomFragmentViewModel.setAppError(ErrorHandler.LOAD_ERROR)
                Log.e("Response error", "Can't load post")
            }
            is RandomGifViewState.SuccessOperation -> {
            }
            is RandomGifViewState.Error.UnexpectedError -> {
                randomFragmentViewModel.setAppError(ErrorHandler.LOAD_ERROR)
                Log.e("Response error", "Can't load post")
            }
        }
    }

    private fun loadGifWithGlide(url: String?) {
        randomFragmentViewModel.setIsCurrentGifLoaded(false)
        randomFragmentViewModel.canLoadNext = MutableLiveData(false)
        Glide.with(requireActivity())
            .asGif()
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.waiting_background)
            .listener(object : RequestListener<GifDrawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<GifDrawable?>,
                    isFirstResource: Boolean,
                ): Boolean {
                    savedUrl = model as String
                    randomFragmentViewModel.setAppError(ErrorHandler.SUCCESS)
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any,
                    target: Target<GifDrawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean,
                ): Boolean {
                    if (randomFragmentViewModel.getError().value!! == ErrorHandler.IMAGE_ERROR) {
                        randomFragmentViewModel.setAppError(ErrorHandler.SUCCESS)
                    }
                    randomFragmentViewModel.setIsCurrentGifLoaded(true)
                    randomFragmentViewModel.canLoadNext = MutableLiveData(true)
                    return false
                }
            })
            .into(binding.loadImage)
    }

    private fun setupErrorParams(assetManager: AssetManager?) {
        binding.loadImage.setImageResource(R.color.disabled_btn)
        binding.loadDescription.setText(R.string.no_internet)
        binding.loadAuthor.text = ":("
    }

    private fun loadGif() {
        if (!randomFragmentViewModel.goNext()) {
            randomFragmentViewModel.canLoadNext = MutableLiveData(false)

            randomFragmentViewModel.loadRandomGif()
            loadGifWithGlide(randomFragmentViewModel.getCurrentGif().value?.gifURL)

        } else loadGifWithGlide(
            randomFragmentViewModel.getCurrentGif().value?.gifURL
        )
    }

    override fun nextEnabled(): Boolean {
        return randomFragmentViewModel.canLoadNext.value!!
    }

    override fun previousEnabled(): Boolean {
        return randomFragmentViewModel.canLoadPrevious.value!!
    }
}
