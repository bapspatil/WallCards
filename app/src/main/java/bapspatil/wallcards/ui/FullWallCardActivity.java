package bapspatil.wallcards.ui;

import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.aviran.cookiebar2.CookieBar;

import java.io.IOException;

import bapspatil.wallcards.R;
import bapspatil.wallcards.data.FavsContract;
import bapspatil.wallcards.models.Wallpaper;
import bapspatil.wallcards.sync.FavoritesWidgetProvider;
import bapspatil.wallcards.utils.GlideApp;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

public class FullWallCardActivity extends AppCompatActivity {

    private Intent intent;
    private Wallpaper mWallpaper;
    private Bitmap mWallpaperBitmap;

    @BindView(R.id.wall_card_wallpaper_iv) ImageView mWallCardImageView;
    @BindView(R.id.set_wallpaper_fab) FloatingActionButton mSetWallpaperFab;
    @BindView(R.id.favorites_fab) FloatingActionButton mFavoritesFab;
    @BindView(R.id.user_pic_civ) CircleImageView mUserPictureImageView;
    @BindView(R.id.user_name_tv) TextView mUserNameTextView;
    @BindView(R.id.likes_tv) TextView mLikesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_wall_card);
        ButterKnife.bind(this);
        hideStatusBar();
        getWindow().setNavigationBarColor(Color.parseColor("#CC000000"));
        intent = getIntent();
        mWallpaper = intent.getParcelableExtra("WALLPAPER");

        Timber.e(mWallpaper.toString());
        mUserNameTextView.setText(mWallpaper.getUser().getName());
        String likes = mWallpaper.getLikes() + getString(R.string.space_likes);
        mLikesTextView.setText(likes);
        GlideApp.with(this)
                .load(mWallpaper.getUser().getProfileImage().getMedium())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.bg_round)
                .fallback(R.drawable.bg_round)
                .into(mUserPictureImageView);
        GlideApp.with(this)
                .load(mWallpaper.getUrls().getRegular())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.gradient_home)
                .fallback(R.drawable.gradient_home)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(mWallCardImageView);

        GlideApp.with(this)
                .asBitmap()
                .load(mWallpaper.getUrls().getRegular())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mWallpaperBitmap = resource;
                    }
                });

        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        mSetWallpaperFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    wallpaperManager.setBitmap(mWallpaperBitmap);
                    CookieBar.build(FullWallCardActivity.this)
                            .setTitle(R.string.image_set_as_wall)
                            .setDuration(3000)
                            .setBackgroundColor(R.color.md_pink_700)
                            .setLayoutGravity(Gravity.TOP)
                            .show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), R.string.not_set_wallpaper, Toast.LENGTH_LONG).show();
                }
            }
        });
        mFavoritesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAsFavorite();
            }
        });

        mWallCardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserPictureImageView.getAlpha() == 1) {
                    mUserPictureImageView.animate()
                            .alpha(0)
                            .translationXBy(-100)
                            .setDuration(300)
                            .start();
                    mUserNameTextView.animate()
                            .alpha(0)
                            .translationXBy(-100)
                            .setDuration(300)
                            .start();
                    mLikesTextView.animate()
                            .alpha(0)
                            .translationXBy(-100)
                            .setDuration(300)
                            .start();
                    mSetWallpaperFab.animate()
                            .alpha(0)
                            .translationXBy(80)
                            .setDuration(300)
                            .start();
                    mFavoritesFab.animate()
                            .alpha(0)
                            .translationXBy(120)
                            .setDuration(300)
                            .start();
                } else {
                    mUserPictureImageView.animate()
                            .alpha(1)
                            .translationXBy(100)
                            .setDuration(300)
                            .start();
                    mUserNameTextView.animate()
                            .alpha(1)
                            .translationXBy(100)
                            .setDuration(300)
                            .start();
                    mLikesTextView.animate()
                            .alpha(1)
                            .translationXBy(100)
                            .setDuration(300)
                            .start();
                    mSetWallpaperFab.animate()
                            .alpha(1)
                            .translationXBy(-80)
                            .setDuration(300)
                            .start();
                    mFavoritesFab.animate()
                            .alpha(1)
                            .translationXBy(-120)
                            .setDuration(300)
                            .start();
                }
            }
        });
    }

    private void hideStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void setAsFavorite() {
        String[] wallpaperId = {mWallpaper.getId()};
        Cursor cursor = getContentResolver().query(FavsContract.FavsEntry.CONTENT_URI,
                null,
                FavsContract.FavsEntry._ID + "=?",
                wallpaperId,
                null);
        if (cursor != null) {
            if (cursor.getCount() == 0) {
                try {
                    new FavoritesTask().execute("ADD");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                removeWallCardFromFavorites();
            }
            cursor.close();
        }
    }
    private class FavoritesTask extends AsyncTask<String, Void, Void> {
        String task;
        @Override
        protected Void doInBackground(String... strings) {
            task = strings[0];
            if(task.equals("ADD"))
                addWallCardToFavorites();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(task.equals("ADD"))
            CookieBar.build(FullWallCardActivity.this)
                    .setTitle(R.string.add_fav)
                    .setBackgroundColor(R.color.md_blue_700)
                    .setLayoutGravity(Gravity.TOP)
                    .setDuration(2000)
                    .show();
            updateWallcardsWidget(getApplicationContext());
        }
    }
    private void addWallCardToFavorites() {
        ContentValues cv = new ContentValues();
        cv.put(FavsContract.FavsEntry._ID, mWallpaper.getId());
        cv.put(FavsContract.FavsEntry.COLUMN_COLOR, mWallpaper.getColor());
        cv.put(FavsContract.FavsEntry.COLUMN_LIKES, mWallpaper.getLikes());
        cv.put(FavsContract.FavsEntry.COLUMN_URL, mWallpaper.getUrls().getRegular());
        cv.put(FavsContract.FavsEntry.COLUMN_USER_NAME, mWallpaper.getUser().getName());
        cv.put(FavsContract.FavsEntry.COLUMN_USER_PIC, mWallpaper.getUser().getProfileImage().getMedium());
        Uri uri = getContentResolver().insert(FavsContract.FavsEntry.CONTENT_URI, cv);
        if (uri != null)
            Timber.d("Uri add: %s", uri.toString());

    }

    private void removeWallCardFromFavorites() {
        String wallpaperDeletedId = mWallpaper.getId();
        Uri uri = FavsContract.FavsEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(wallpaperDeletedId).build();
        getContentResolver().delete(uri, null, null);
        Timber.d("Uri delete: %s", uri.toString());
        CookieBar.build(this)
                .setTitle(R.string.remove_fav)
                .setBackgroundColor(R.color.md_red_700)
                .setLayoutGravity(Gravity.TOP)
                .setDuration(2000)
                .show();
        updateWallcardsWidget(getApplicationContext());
    }

    private void updateWallcardsWidget(Context context) {
        ComponentName name = new ComponentName(this, FavoritesWidgetProvider.class);
        int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(name);

        Intent intent = new Intent(this, FavoritesWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, ids);
        sendBroadcast(intent);
    }
}
