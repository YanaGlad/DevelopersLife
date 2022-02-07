package com.example.yanagladdeveloperslife.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.yanagladdeveloperslife.ErrorHandler
import com.example.yanagladdeveloperslife.R
import com.example.yanagladdeveloperslife.databinding.LoadItemBinding
import com.example.yanagladdeveloperslife.fragments.FavouriteHelper
import com.example.yanagladdeveloperslife.models.GifModel
import com.example.yanagladdeveloperslife.viewmodel.GifViewModel

import dagger.hilt.android.internal.managers.ViewComponentManager


class GifsRecyclerAdapter(val helper: FavouriteHelper, _type: String) : ListAdapter<GifModel, GifsRecyclerAdapter.ViewHolder>(DiffCallback()) {

    private val type: String = _type

    class DiffCallback : DiffUtil.ItemCallback<GifModel>() {
        override fun areItemsTheSame(oldItem: GifModel, newItem: GifModel): Boolean {
            return oldItem.gifURL == newItem.gifURL
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: GifModel, newItem: GifModel): Boolean {
            return oldItem == newItem
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = getItem(position)
        holder.bind(model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LoadItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            type, helper
        )
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(
        private val binding: LoadItemBinding,
        val type: String,
        val helper: FavouriteHelper
    ) :
        RecyclerView.ViewHolder(binding.root) {
        val context: Context = binding.root.context
        private var viewModel: GifViewModel? = null

        private val requestListener: RequestListener<GifDrawable> =
            object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<GifDrawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    viewModel!!.error = MutableLiveData(ErrorHandler.IMAGE_ERROR)
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any,
                    target: Target<GifDrawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    if (viewModel != null) {
                        viewModel!!.isCurrentGifLoaded = MutableLiveData(true)
                        viewModel!!.error = MutableLiveData(ErrorHandler.SUCCESS)
                    } else {
                        viewModel!!.error = MutableLiveData(ErrorHandler.IMAGE_ERROR)
                    }
                    return false
                }
            }

        @SuppressLint("SetTextI18n")
        fun bind(gifModel: GifModel) {
            val context = itemView.context
            val res = if (context is ViewComponentManager.FragmentContextWrapper) {
                context.baseContext
            } else context

            val gifViewModel: GifViewModel = ViewModelProvider((res as ViewModelStoreOwner)).get(type, GifViewModel::class.java)

            setViewModel(gifViewModel)
            loadImage(gifModel.gifURL)

            binding.loadAuthor.text = context.getString(R.string.by) + " " + gifModel.author
            binding.loadDescription.text = gifModel.description

            for (item in helper.getAllFavs()) {
                if (item.description == gifModel.description) {
                    binding.favsButton.setColorFilter(Color.RED)
                    break
                }
            }
            binding.favsButton.setOnClickListener {
                helper.addToFavs(gifModel)
                binding.favsButton.setColorFilter(Color.RED)
            }
        }

        private fun setViewModel(viewModel: GifViewModel) {
            this.viewModel = viewModel
            viewModel.isCurrentGifLoaded
                .observe(activityContext() as LifecycleOwner) { isLoaded ->
                    if (isLoaded) binding.loadProgressbar.visibility =
                        View.GONE else binding.loadProgressbar.visibility =
                        View.VISIBLE
                }
            viewModel.error.observe(activityContext() as LifecycleOwner) { error ->
                if (error == ErrorHandler.SUCCESS)
                    if (error == ErrorHandler.IMAGE_ERROR)
                        setupRecycleErrorParams() else binding.loadLinearLayout.visibility =
                        View.VISIBLE
            }
        }

        private fun setupRecycleErrorParams() {
            prepareForLoading()
            binding.loadProgressbar.visibility = View.GONE
            binding.loadDescription.setText(R.string.no_internet)
            binding.loadAuthor.text = ":("
        }

        private fun prepareForLoading() {
            if (viewModel != null) viewModel!!.isCurrentGifLoaded = MutableLiveData(false)
            binding.loadImage.setImageResource(R.color.disabled_btn)
        }

        private fun loadImage(url: String?) {
            prepareForLoading()
            Glide.with(context)
                .asGif()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(requestListener)
                .placeholder(R.drawable.waiting_background)
                .into(binding.loadImage)
        }

        private fun activityContext(): Context? {
            val context = itemView.context
            return if (context is ViewComponentManager.FragmentContextWrapper) {
                context.baseContext
            } else context
        }
    }
}
