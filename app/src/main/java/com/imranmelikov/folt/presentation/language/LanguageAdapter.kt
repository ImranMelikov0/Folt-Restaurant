package com.imranmelikov.folt.presentation.language

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.imranmelikov.folt.databinding.LanguageRvBinding
import com.imranmelikov.folt.domain.model.Language

class LanguageAdapter:RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {
    class LanguageViewHolder(val binding:LanguageRvBinding):RecyclerView.ViewHolder(binding.root)


    var onItemClick:((Language)->Unit)?=null
    var languageName=""
    // DiffUtil for efficient RecyclerView updates
    private val diffUtil=object : DiffUtil.ItemCallback<Language>(){
        override fun areItemsTheSame(oldItem: Language, newItem: Language): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Language, newItem: Language): Boolean {
            return oldItem==newItem
        }
    }
    private val recyclerDiffer= AsyncListDiffer(this,diffUtil)

    // Getter and setter list country
    var languageList:List<Language>
        get() = recyclerDiffer.currentList
        set(value) = recyclerDiffer.submitList(value)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding=LanguageRvBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LanguageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return languageList.size
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language=languageList[position]
        holder.binding.languageText.text=language.language

        if (language.language==languageName){
            holder.binding.selectedImg.visibility= View.VISIBLE
        }else{
            holder.binding.selectedImg.visibility= View.GONE
        }
        holder.itemView.setOnClickListener {
            onItemClick?.let {
                it(language)
            }
        }
    }
}