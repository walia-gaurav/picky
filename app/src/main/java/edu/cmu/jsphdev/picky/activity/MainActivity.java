package edu.cmu.jsphdev.picky.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

import edu.cmu.jsphdev.picky.R;

/**
 * Activity to create tabs for Login and Sign-Up.
 */
public class MainActivity extends AppCompatActivity {

    TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
