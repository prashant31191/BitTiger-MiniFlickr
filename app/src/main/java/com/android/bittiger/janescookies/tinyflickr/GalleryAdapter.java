package com.android.bittiger.janescookies.tinyflickr;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fmsirvent.ParallaxEverywhere.PEWImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by xicheng on 16/6/16.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context mContext;   // get resource of this component

    private List<GalleryItem> mList;
//    private List<Contact> mList;

//    public GalleryAdapter(Context mContext, List<Contact> mList) {
    public GalleryAdapter(Context mContext, List<GalleryItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public PEWImageView mImageView;
        public TextView tvTitle;


        public ViewHolder(View itemView) {
            super(itemView);
//            mImageView = (ImageView) itemView.findViewById(R.id.image_item);
//            mImageView = (ImageView) itemView.findViewById(R.id.networkimage);

            mImageView = (PEWImageView) itemView.findViewById(R.id.gallery_item);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

            // for test, use me.jpg from drawable/
            //Log.d("Contact", "--------ViewHolder------setImageResource to me " );
            //mImageView.setImageResource(R.drawable.me);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gallery,
                parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        Contact item = mList.get(position);
        final GalleryItem item = mList.get(position);



        //ImageView nameImageView = holder.mImageView;
//        Log.d("Contact", "--------onBindViewHolder------setImageResource to dog " );
//        nameImageView.setImageResource(R.drawable.dog);

        holder.tvTitle.setText(item.getTitle());
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //click on photo and redirects to another page
                Intent intent = new Intent(mContext, PhotoActivity.class);
                intent.putExtra("item", item);
                mContext.startActivity(intent);
            }
        });

       /* switch (position % 5) {
            case 0 : holder.mImageView.setImageResource(R.mipmap.ic_launcher); break;
            case 1 : holder.mImageView.setImageResource(R.mipmap.ic_launcher); break;
            case 2 : holder.mImageView.setImageResource(R.mipmap.ic_launcher); break;
            case 3 : holder.mImageView.setImageResource(R.mipmap.ic_launcher); break;
            case 4 : holder.mImageView.setImageResource(R.mipmap.ic_launcher); break;
        }
*/
        Glide.with(mContext)
                .load(item.getUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                        try{
                           // holder.mImageView.setImageBitmap(resource);

                            // This is the quick and easy integration path.
// May not be optimal (since you're dipping in and out of threads)
                        /*    Palette.from(resource).maximumColorCount(2).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    // Get the "vibrant" color swatch based on the bitmap
                                    Palette.Swatch vibrant = palette.getVibrantSwatch();
                                    if (vibrant != null) {
                                        // Set the background color of a layout based on the vibrant color
                                        holder.tvTitle.setBackgroundColor(vibrant.getRgb());
                                        // Update the title TextView with the proper text color
                                        holder.tvTitle.setTextColor(vibrant.getTitleTextColor());
                                    }
                                }
                            });*/

                            Palette.from(resource)
                                    .generate(new Palette.PaletteAsyncListener() {
                                        @Override
                                        public void onGenerated(Palette palette) {
                                            if (palette.getVibrantSwatch() != null) {
                                                Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                                if (textSwatch == null) {
//                                                Toast.makeText(MainActivity.this, "Null swatch :(", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                holder.tvTitle.setBackgroundColor(textSwatch.getRgb());
                                                holder.tvTitle.setTextColor(textSwatch.getTitleTextColor());
                                                //holder.tvTitle.setTextColor(textSwatch.getBodyTextColor());
                                            }
                                        }
                                    });
                        }
                        catch (Exception e)
                        {
                         e.printStackTrace();
                        }
                    }
                });

        Picasso.with(mContext)
                .load(item.getUrl())
                .resize(20, 30)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .transform(blurTransformation)
                .into(holder.mImageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.with(mContext)
                                .load(item.getUrl()) // image url goes here
                                // .resize(imageViewWidth, imageViewHeight)
                                .resize(175, 250)
                                // .resize(holder.ivImage.getWidth(), holder.ivImage.getHeight())
                                .placeholder(holder.mImageView.getDrawable())
                                .into(holder.mImageView);
                    }

                    @Override
                    public void onError() {
                    }
                });

       /* Glide.with(mContext)
                .load(item.getUrl())
                .thumbnail(0.5f)
                .into(holder.mImageView);*/

    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

//    public void addAll(List<Contact> newList) {
    public void addAll(List<GalleryItem> newList) {
        mList.addAll(newList);
    }

    public void add(GalleryItem item) {
        mList.add(item);
    }

    public void clear() {
        mList.clear();
    }




    Transformation blurTransformation = new Transformation() {
        @Override
        public Bitmap transform(Bitmap source) {
            Bitmap blurred = Blur.fastblur(mContext, source, 10);
            source.recycle();
            return blurred;
        }

        @Override
        public String key() {
            return "blur()";
        }
    };
}
