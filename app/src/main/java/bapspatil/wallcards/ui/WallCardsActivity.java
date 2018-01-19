package bapspatil.wallcards.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.wang.avi.AVLoadingIndicatorView;

import org.aviran.cookiebar2.CookieBar;

import java.util.ArrayList;

import bapspatil.wallcards.BuildConfig;
import bapspatil.wallcards.R;
import bapspatil.wallcards.adapters.WallCardsRecyclerViewAdapter;
import bapspatil.wallcards.data.FavsContract;
import bapspatil.wallcards.models.ProfileImage;
import bapspatil.wallcards.models.URLs;
import bapspatil.wallcards.models.User;
import bapspatil.wallcards.models.Wallpaper;
import bapspatil.wallcards.network.UnsplashAPI;
import bapspatil.wallcards.utils.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class WallCardsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.appbar) AppBarLayout appBar;
    @BindView(R.id.collapsing_bar) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.wall_cards_rv) RecyclerView mWallCardsRecyclerView;
    @BindView(R.id.loading_indicator) AVLoadingIndicatorView loadingIndicator;

    private ArrayList<Wallpaper> wallpapersList = new ArrayList<>();
    private Intent intent;
    private WallCardsRecyclerViewAdapter mAdapter;
    private String section;
    private static final int FAVORITES_LOADER_ID = 13;

    private void appBarInit() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Typeface typeface = ResourcesCompat.getFont(this, R.font.caveat_brush);
        collapsingToolbar.setCollapsedTitleTypeface(typeface);
        collapsingToolbar.setExpandedTitleTypeface(typeface);
        collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.md_white_1000));
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.md_white_1000));

        intent = getIntent();
        section = intent.getStringExtra("SECTION");
        toolbar.setTitle(section);
        collapsingToolbar.setTitle(section);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_cards);
        ButterKnife.bind(this);
        if(!NetworkUtils.hasNetwork(this))
            CookieBar.build(WallCardsActivity.this)
                    .setTitle(R.string.no_internet)
                    .setMessage(R.string.no_internet_messages)
                    .setBackgroundColor(R.color.colorPrimary)
                    .setLayoutGravity(Gravity.BOTTOM)
                    .setDuration(5000)
                    .show();

        appBarInit();

        recyclerViewInit();

        fetchWallCards(section);
    }

    private void recyclerViewInit() {
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mWallCardsRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new WallCardsRecyclerViewAdapter(getApplicationContext(), wallpapersList, new WallCardsRecyclerViewAdapter.OnWardClickListener() {
            @Override
            public void onWallCardClicked(Wallpaper wallpaper, ImageView wallCardImageView) {
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(WallCardsActivity.this, wallCardImageView, "WallCardTransition").toBundle();
                Intent intentToFullWardCardScreen = new Intent(WallCardsActivity.this, FullWallCardActivity.class);
                intentToFullWardCardScreen.putExtra("WALLPAPER", wallpaper);
                startActivity(intentToFullWardCardScreen, bundle);
            }
        });
        mWallCardsRecyclerView.setAdapter(new ScaleInAnimationAdapter(mAdapter));
    }

    private void fetchWallCards(String section) {
        UnsplashAPI unsplashAPI = NetworkUtils.getCacheEnabledRetrofit(this).create(UnsplashAPI.class);
        Call<ArrayList<Wallpaper>> call;
        switch (section) {
            case "POPULAR":
                appBar.setBackgroundColor(ContextCompat.getColor(this, R.color.md_deep_orange_700));
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.md_deep_orange_900));
                call = unsplashAPI.getPopularWallpapers(BuildConfig.UNSPLASH_CLIENT_ID);
                call.enqueue(new Callback<ArrayList<Wallpaper>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Wallpaper>> call, Response<ArrayList<Wallpaper>> response) {
                        if(response.body() != null) {
                            wallpapersList.addAll(response.body());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Timber.e("Response from Unsplash is null!");
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Wallpaper>> call, Throwable t) {
                        Timber.e("Couldn't fetch data from Unsplash API!");
                    }
                });
                loadingIndicator.setVisibility(View.GONE);
                mWallCardsRecyclerView.setVisibility(View.VISIBLE);
                break;
            case "CURATED":
                appBar.setBackgroundColor(ContextCompat.getColor(this, R.color.md_blue_700));
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.md_blue_900));
                call = unsplashAPI.getCuratedWallpapers(BuildConfig.UNSPLASH_CLIENT_ID);
                call.enqueue(new Callback<ArrayList<Wallpaper>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Wallpaper>> call, Response<ArrayList<Wallpaper>> response) {
                        if(response.body() != null) {
                            wallpapersList.addAll(response.body());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Timber.e("Response from Unsplash is null!");
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Wallpaper>> call, Throwable t) {
                        Timber.e("Couldn't fetch data from Unsplash API!");
                    }
                });
                loadingIndicator.setVisibility(View.GONE);
                mWallCardsRecyclerView.setVisibility(View.VISIBLE);
                break;
            case "FAVORITES":
                appBar.setBackgroundColor(ContextCompat.getColor(this, R.color.md_pink_500));
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.md_pink_700));

                getSupportLoaderManager().initLoader(FAVORITES_LOADER_ID, null, this);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, FavsContract.FavsEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        wallpapersList.clear();
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Wallpaper wallpaper = new Wallpaper();
                wallpaper.setId(cursor.getString(cursor.getColumnIndex(FavsContract.FavsEntry._ID)));
                wallpaper.setColor(cursor.getString(cursor.getColumnIndex(FavsContract.FavsEntry.COLUMN_COLOR)));
                wallpaper.setLikes(cursor.getInt(cursor.getColumnIndex(FavsContract.FavsEntry.COLUMN_LIKES)));
                URLs urls = new URLs(cursor.getString(cursor.getColumnIndex(FavsContract.FavsEntry.COLUMN_URL)));
                wallpaper.setUrls(urls);
                ProfileImage profileImage = new ProfileImage(cursor.getString(cursor.getColumnIndex(FavsContract.FavsEntry.COLUMN_USER_PIC)));
                User user = new User(cursor.getString(cursor.getColumnIndex(FavsContract.FavsEntry.COLUMN_USER_NAME)),
                        profileImage);
                wallpaper.setUser(user);
                wallpapersList.add(wallpaper);
                mAdapter.notifyDataSetChanged();
                cursor.moveToNext();
            }
            loadingIndicator.setVisibility(View.GONE);
            mWallCardsRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mAdapter.notifyDataSetChanged();
            CookieBar.build(this)
                    .setTitle(R.string.no_favs)
                    .setMessage(R.string.no_favs_messages)
                    .setBackgroundColor(R.color.md_pink_500)
                    .setDuration(500)
                    .setLayoutGravity(Gravity.BOTTOM)
                    .show();
            loadingIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Nothing to do here I guess
    }
}
