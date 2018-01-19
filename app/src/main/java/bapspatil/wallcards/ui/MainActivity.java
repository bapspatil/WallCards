package bapspatil.wallcards.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.aviran.cookiebar2.CookieBar;

import bapspatil.wallcards.R;
import bapspatil.wallcards.utils.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.text_greeting) TextView mGreetingTextView;
    @BindView(R.id.button_popular) Button mPopularButton;
    @BindView(R.id.button_curated) Button mCuratedButton;
    @BindView(R.id.button_favorites) Button mFavoritesButton;
    @BindView(R.id.image_logout) ImageView mLogoutImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if(!NetworkUtils.hasNetwork(this))
            CookieBar.build(MainActivity.this)
            .setTitle(R.string.no_internet)
            .setMessage(R.string.no_internet_messages)
            .setBackgroundColor(R.color.colorPrimary)
            .setLayoutGravity(Gravity.BOTTOM)
            .setDuration(5000)
            .show();

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String userFirstName;
        if (mUser != null && mUser.getDisplayName() != null) {
            userFirstName = mUser.getDisplayName();
            String greetingString = getString(R.string.hey_there_greeting) + userFirstName + getString(R.string.dot);
            mGreetingTextView.setText(greetingString);
        } else {
            String greetingStringWithoutName = getString(R.string.hey_there_without_name);
            mGreetingTextView.setText(greetingStringWithoutName);
        }

        handleButtonClicks();
        mLogoutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intentToLogin = new Intent(MainActivity.this, SplashActivity.class);
                startActivity(intentToLogin);
                finish();
            }
        });
    }

    private void handleButtonClicks() {
        final Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        mPopularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToWallCards = new Intent(MainActivity.this, WallCardsActivity.class);
                intentToWallCards.putExtra(getString(R.string.section), getString(R.string.popular));
                startActivity(intentToWallCards, bundle);
            }
        });
        mCuratedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToWallCards = new Intent(MainActivity.this, WallCardsActivity.class);
                intentToWallCards.putExtra(getString(R.string.section), getString(R.string.curated));
                startActivity(intentToWallCards, bundle);
            }
        });
        mFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToWallCards = new Intent(MainActivity.this, WallCardsActivity.class);
                intentToWallCards.putExtra(getString(R.string.section), getString(R.string.favorites));
                startActivity(intentToWallCards, bundle);
            }
        });
    }

}
