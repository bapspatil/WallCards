package bapspatil.wallcards.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;

import bapspatil.wallcards.R;
import bapspatil.wallcards.models.Wallpaper;
import bapspatil.wallcards.utils.GlideApp;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bapspatil
 */

public class WallCardsRecyclerViewAdapter extends RecyclerView.Adapter<WallCardsRecyclerViewAdapter.WallCardViewHolder> {

    private OnWardClickListener mListener;
    private Context mContext;
    private ArrayList<Wallpaper> mWallpapersList;

    public WallCardsRecyclerViewAdapter(Context context, ArrayList<Wallpaper> wallpaperArrayList, OnWardClickListener listener) {
        this.mContext = context;
        this.mWallpapersList = wallpaperArrayList;
        this.mListener = listener;
    }

    @Override
    public WallCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_wall_card, parent, false);
        return new WallCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WallCardViewHolder holder, int position) {
        GlideApp.with(mContext)
                .load(mWallpapersList.get(position).getUrls().getRegular())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.gradient_home)
                .fallback(R.drawable.gradient_home)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(holder.mWallCardImageView);
    }

    @Override
    public int getItemCount() {
        if(mWallpapersList == null) return 0;
        else return mWallpapersList.size();
    }

    public class WallCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.wall_card_iv) ImageView mWallCardImageView;

        public WallCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onWallCardClicked(mWallpapersList.get(getAdapterPosition()), mWallCardImageView);
        }
    }

    public interface OnWardClickListener {
        void onWallCardClicked(Wallpaper wallpaper, ImageView wallCardImageView);
    }
}
