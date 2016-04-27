package edu.cmu.jsphdev.picky.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

import com.google.gson.Gson;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.entities.User;
import edu.cmu.jsphdev.picky.util.CurrentSession;

/**
 * Activity to create tabs for Login and Sign-Up.
 */
public class MainActivity extends AppCompatActivity {

    private static final String[] ALL = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET
    };

    TabHost tabHost;

    /*
        <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, ALL, 1);

        String existingToken = PreferenceManager.getDefaultSharedPreferences(this).getString("existingUser", "");
        if (existingToken != null && !existingToken.isEmpty()) {
            CurrentSession.setActiveUser(new Gson().fromJson(existingToken, User.class));
            startActivity(new Intent(this, HomeActivity.class));
        }

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        /*
        Initializing different tab holders.
         */
        addTabSpecs("Log In", R.id.loginContent);
        addTabSpecs("Sign Up", R.id.signUpContent);
    }

    private void addTabSpecs(String tag, int contentId) {
        TabHost.TabSpec spec = tabHost.newTabSpec(tag);
        spec.setContent(contentId);
        spec.setIndicator(tag);
        tabHost.addTab(spec);
    }
}
