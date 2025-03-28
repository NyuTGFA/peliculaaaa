package com.example.peliculaaaa.pelicula.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.peliculaaaa.R;
import com.example.peliculaaaa.pelicula.models.SlideItems;

import java.util.List;

public class SlideAdapters extends RecyclerView.Adapter<SlideAdapters.SlideViewHolder> {
    private List<SlideItems> slideItems;
    private ViewPager2 viewPager2;
    private Context context;

    public SlideAdapters(List<SlideItems> slideItems, ViewPager2 viewPager2) {
        this.slideItems = slideItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SlideAdapters.SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new SlideViewHolder(LayoutInflater.from(context).inflate(
                R.layout.slide_item_container, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull SlideAdapters.SlideViewHolder holder, int position) {
        holder.setImageView(slideItems.get(position));
    }

    @Override
    public int getItemCount() {
        return slideItems.size();
    }

    public class SlideViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageslider);
        }

        void setImageView(SlideItems slideItem) {
            RequestOptions requestOptions = new RequestOptions()
                    .transform(new CenterCrop(), new RoundedCorners(60));

            Glide.with(context)
                    .load(slideItem.getImage()) // Carga desde la URL
                    .apply(requestOptions)
                    .into(imageView);
        }
    }
}
