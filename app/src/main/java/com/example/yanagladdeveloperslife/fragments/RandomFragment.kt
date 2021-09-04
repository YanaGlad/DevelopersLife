package com.example.yanagladdeveloperslife.fragments

import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.yanagladdeveloperslife.MainActivity
import com.example.yanagladdeveloperslife.R
import com.example.yanagladdeveloperslife.databinding.FragmentRandomBinding
import com.example.yanagladdeveloperslife.models.GifModel
import com.example.yanagladdeveloperslife.values.ErrorHandler
import com.example.yanagladdeveloperslife.viewmodel.RandomFragmentViewModel
import com.example.yanagladdeveloperslife.viewstate.RandomGifViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomFragment : ButtonSupportedFragment() {
    var isOnScreen = false

    private var _binding: FragmentRandomBinding? = null
    private val binding get() = _binding!!

    private val randomFragmentViewModel: RandomFragmentViewModel by viewModels()
    private lateinit var savedUrl: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRandomBinding.inflate(layoutInflater)

        setupButtonListeners()
        setupObservers()

        randomFragmentViewModel.loadRandomGif()

        return binding.root
    }

    private fun setupObservers() {
        randomFragmentViewModel.getCurrentGif().observe(viewLifecycleOwner) { gif: GifModel? ->
            if (gif != null) {
                binding.loadDescription.text = gif.description
                binding.loadAuthor.text = "By ${gif.author}"
            }
        }
        randomFragmentViewModel.getIsCurrentGifLoaded().observe(
            viewLifecycleOwner
        ) { isLoaded ->
            if (isLoaded) binding.loadProgressbar.visibility =
                View.INVISIBLE else binding.loadProgressbar.visibility =
                View.VISIBLE
        }
        randomFragmentViewModel.getCanLoadNext().observe(viewLifecycleOwner) { enabled: Boolean? ->
            if (isOnScreen) binding.buttonsLayout.btnNext.isEnabled = enabled!!
        }

        randomFragmentViewModel.getCanLoadPrevious().observe(viewLifecycleOwner) { enabled ->
            if (isOnScreen) binding.buttonsLayout.btnPrevious.isEnabled = enabled
        }

        randomFragmentViewModel.getError().observe(viewLifecycleOwner) { e ->
            randomFragmentViewModel.updateCanLoadPrevious()
            binding.recycleErrorProgressbar.visibility = View.INVISIBLE
            if (e != ErrorHandler.SUCCESS) {
                binding.loadLinearLayout.visibility = View.GONE
                when (e) {
                    ErrorHandler.LOAD_ERROR -> binding.recyclErrorBtn.visibility = View.VISIBLE
                    ErrorHandler.IMAGE_ERROR -> {
                        binding.recyclErrorBtn.visibility = View.VISIBLE
                        setupErrorParams(requireContext().assets)
                        randomFragmentViewModel.setCanLoadNext(false)
                    }
                    else -> {
                    }
                }
                binding.recyclErrorBtn.setOnClickListener {
                    if (binding.recycleErrorProgressbar.visibility == View.INVISIBLE) {
                        binding.recycleErrorProgressbar.visibility = View.VISIBLE
                        if (e == ErrorHandler.IMAGE_ERROR) {
                            loadGifWithGlide(savedUrl)
                        }
                    }
                }
            } else {
                binding.recyclErrorBtn.visibility = View.GONE
                binding.recycleErrorProgressbar.visibility = View.GONE
                binding.loadLinearLayout.visibility = View.VISIBLE
            }
        }

        randomFragmentViewModel.viewState.observe(viewLifecycleOwner) {
            handleViewState(it)
        }
    }

    private fun setupButtonListeners() {
        val onPrevClickListener = View.OnClickListener {
            if (!randomFragmentViewModel.goBack()) Log.e(
                "Cache is empty",
                "No cached gifs"
            ) else loadGifWithGlide(randomFragmentViewModel.getCurrentGif().value?.gifURL)

            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        val onNextClickListener = View.OnClickListener { loadGif() }
        binding.buttonsLayout.btnPrevious.setOnClickListener(onPrevClickListener)
        binding.buttonsLayout.btnNext.setOnClickListener(onNextClickListener)
    }

    private fun onGifLoaded(viewState: RandomGifViewState.Loaded) {
        randomFragmentViewModel.addGifModel(viewState.gif.createGifModel())
        loadGifWithGlide(randomFragmentViewModel.getCurrentGif().value?.gifURL)
    }

    private fun handleViewState(viewState: RandomGifViewState?) {

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
        randomFragmentViewModel.setCanLoadNext(false)
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
                    isFirstResource: Boolean
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
                    isFirstResource: Boolean
                ): Boolean {
                    if (randomFragmentViewModel.getError()
                            .value!! == ErrorHandler.IMAGE_ERROR
                    ) {
                        randomFragmentViewModel.setAppError(ErrorHandler.SUCCESS)
                    }
                    randomFragmentViewModel.setIsCurrentGifLoaded(true)
                    randomFragmentViewModel.setCanLoadNext(true)
                    return false
                }
            })
            .into(binding.loadImage)
    }

    private fun setupErrorParams(assetManager: AssetManager?) {
        binding.loadImage.setImageResource(R.color.disabled_btn)

//        Glide.with(context)
//                .load(Support.loadBitmapImage(assetManager, "devnull.png"))
//                .into(image);
        binding.loadDescription.setText(R.string.no_internet)
        binding.loadAuthor.text = ":("
    }

    private fun loadGif() {
        if (!randomFragmentViewModel.goNext()) {
            randomFragmentViewModel.setCanLoadNext(false)

            randomFragmentViewModel.loadRandomGif()
            loadGifWithGlide(randomFragmentViewModel.getCurrentGif().value?.gifURL)

        } else loadGifWithGlide(
            randomFragmentViewModel.getCurrentGif().value?.gifURL
        )
    }

    override fun nextEnabled(): Boolean {
        return randomFragmentViewModel.getCanLoadNext().value!!
    }

    override fun previousEnabled(): Boolean {
        return randomFragmentViewModel.getCanLoadPrevious().value!!
    }


}