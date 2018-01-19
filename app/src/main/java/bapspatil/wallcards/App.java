package bapspatil.wallcards;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by bapspatil
 */

public class App extends Application {

    private static App APP;

    @Override
    public void onCreate() {
        super.onCreate();
        APP = this;
        if(BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
    }

    public static App getInstance() {
        return APP;
    }
}
