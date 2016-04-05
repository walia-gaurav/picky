package edu.cmu.jsphdev.picky.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

import edu.cmu.jsphdev.picky.R;

public class HomeActivity extends AppCompatActivity {

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tabHost = (TabHost) findViewById(R.id.homeTabHost);
        tabHost.setup();

        /*
        Initializing different tab holders.
         */
        addTabSpecs("Group", R.id.publicTab, R.drawable.group_icon);
        addTabSpecs("Upload", R.id.uploadTab, R.drawable.upload_icon);
        addTabSpecs("Profile", R.id.profileTab, R.drawable.profile_icon);
        addTabSpecs("Account", R.id.accountTab, R.drawable.account_icon);
        addTabSpecs("Logout", R.id.logoutTab, R.drawable.logout_icon);


    }

    private void addTabSpecs(String tag, int contentId, int icon) {
        TabHost.TabSpec spec = tabHost.newTabSpec(tag);

        spec.setContent(contentId);
        spec.setIndicator("", ContextCompat.getDrawable(this, icon));
        tabHost.addTab(spec);
    }

}
