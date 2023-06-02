package com.uberalles.dicodingstorysubmission.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uberalles.dicodingstorysubmission.R
import com.uberalles.dicodingstorysubmission.databinding.CardViewBinding
import com.uberalles.dicodingstorysubmission.repos.Story
import com.uberalles.dicodingstorysubmission.ui.detail.DetailActivity


class StoriesAdapter : PagingDataAdapter<Story, StoriesAdapter.ViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stories = getItem(position)

        stories?.let {
            holder.bind(it).apply {
                holder.itemView.setOnClickListener {
                    val intent = Intent(holder.itemView.context, DetailActivity::class.java).apply {
                        putExtra(DetailActivity.EXTRA_NAME, stories.name)
                        putExtra(DetailActivity.EXTRA_PHOTO_URL, stories.photoUrl)
                        putExtra(DetailActivity.EXTRA_DESCRIPTION, stories.description)
                    }

                    holder.itemView.context.startActivity(
                        intent,
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            holder.itemView.context as Activity,
                            Pair(
                                holder.itemView.findViewById(R.id.item_name),
                                "name"
                            ),
                            Pair(
                                holder.itemView.findViewById(R.id.item_photo),
                                "photo"
                            ),
                            Pair(
                                holder.itemView.findViewById(R.id.item_description),
                                "description"
                            )
                        )
                            .toBundle()
                    )
                }
            }
        }
    }

    inner class ViewHolder(private val binding: CardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.itemName.text = story.name
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .skipMemoryCache(true)
                .into(binding.itemPhoto)

            binding.itemDescription.text = story.description
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(
                oldItem: Story,
                newItem: Story
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Story,
                newItem: Story
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

//class StoriesAdapter : RecyclerView.Adapter<StoriesAdapter.ViewHolder>() {
//    private val stories = ArrayList<Story>()
//
//    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val name: TextView = view.findViewById(R.id.item_name)
//        val description: TextView = view.findViewById(R.id.item_description)
//        val photoUrl: ImageView = view.findViewById(R.id.item_photo)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesAdapter.ViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        return ViewHolder(inflater.inflate(R.layout.card_view, parent, false))
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val listStory = stories[position]
//
//        holder.apply {
//            name.text = listStory.name
//            description.text = listStory.description
//            photoUrl.setBackgroundResource(R.drawable.ic_launcher_background)
//        }
//    }
//
//    override fun getItemCount(): Int = stories.size
//
//}