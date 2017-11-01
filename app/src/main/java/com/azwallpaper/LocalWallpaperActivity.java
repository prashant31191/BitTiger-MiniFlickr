package com.azwallpaper;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fmsirvent.ParallaxEverywhere.PEWImageView;
import com.model.JsonImageModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.utils.Blur;

import java.util.ArrayList;
import java.util.List;


/**

 */
public class LocalWallpaperActivity extends AppCompatActivity {
    private static final String TAG = LocalWallpaperActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private GridLayoutManager mLayoutManager;
    private static final int COLUMN_NUM = 1;
    private GalleryAdapter mAdapter;


    String id = "1";
    String strTitle = "Wallpaper";
    /**
     * Sets the Action Bar for new Android versions.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void actionBarSetup() {
        try {
            ((LocalWallpaperActivity) this).getSupportActionBar().setTitle(strTitle);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "----------onCreate----------");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_dashboard);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setHasFixedSize(true);

        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString("id") != null) {
                id = getIntent().getExtras().getString("id");
            }

            if (getIntent().getExtras().getString("title") != null) {
                strTitle = getIntent().getExtras().getString("title");
                actionBarSetup();
            }

        }

        if (id != null) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ArrayList<JsonImageModel> arrayListJsonImageModel = App.gettingRecordFromJson("z" + id + "cate_.json");
                    if (arrayListJsonImageModel != null) {
                        App.showLog("====arrayListJsonImageModel===" + arrayListJsonImageModel.size());
                        mLayoutManager = new GridLayoutManager(LocalWallpaperActivity.this, COLUMN_NUM);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mAdapter = new GalleryAdapter(LocalWallpaperActivity.this, arrayListJsonImageModel);
                        mRecyclerView.setAdapter(mAdapter);
                        progressBar.setVisibility(View.GONE);
                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            },1500);

        }
        else
        {
            progressBar.setVisibility(View.GONE);
        }

    }

    public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

        private Context mContext;   // get resource of this component
        private List<JsonImageModel> mList;

        public GalleryAdapter(Context mContext, List<JsonImageModel> mList) {
            this.mContext = mContext;
            this.mList = mList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public PEWImageView mImageView;
            public TextView tvTitle;


            public ViewHolder(View itemView) {
                super(itemView);
                mImageView = (PEWImageView) itemView.findViewById(R.id.gallery_item);
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

            }
        }

        @Override
        public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gallery,
                    parent, false);
            GalleryAdapter.ViewHolder vh = new GalleryAdapter.ViewHolder(v);
            return vh;
        }


        @Override
        public void onBindViewHolder(final GalleryAdapter.ViewHolder holder, int position) {
//        Contact item = mList.get(position);
            final JsonImageModel item = mList.get(position);
            holder.tvTitle.setText(item.title);
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

            Picasso.with(mContext)
                    .load(item.thumbnail_url)
                    .resize(10, 10)
                    //.fit()
                    //.centerCrop()

                    .error(R.mipmap.ic_launcher)
                    .transform(blurTransformation)
                    .into(holder.mImageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.with(mContext)
                                    .load(item.thumbnail_url) // image url goes here

                                    .into(holder.mImageView);
                        }

                        @Override
                        public void onError() {
                        }
                    });

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        //    public void addAll(List<Contact> newList) {
        public void addAll(List<JsonImageModel> newList) {
            mList.addAll(newList);
        }

        public void add(JsonImageModel item) {
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


}
