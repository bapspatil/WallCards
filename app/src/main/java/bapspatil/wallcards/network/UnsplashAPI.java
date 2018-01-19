package bapspatil.wallcards.network;

import java.util.ArrayList;

import bapspatil.wallcards.models.Wallpaper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by bapspatil
 */

public interface UnsplashAPI {

    String BASE_URL = "https://api.unsplash.com";

    @GET("/photos")
    Call<ArrayList<Wallpaper>> getPopularWallpapers(@Query("client_id") String CLIENT_ID);

    @GET("/photos/curated")
    Call<ArrayList<Wallpaper>> getCuratedWallpapers(@Query("client_id") String CLIENT_ID);
}
