package edu.cmu.jsphdev.picky.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.fragment.PublicFragment;
import edu.cmu.jsphdev.picky.fragment.UploadHelper;

/**
 * HomeActivity that initializes the tab widget in the app, for different menu.
 */
public class HomeActivity extends AppCompatActivity {

    int selectedPicky;

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tabHost = (TabHost) findViewById(R.id.homeTabHost);
        tabHost.setup();


        /* Initializing different tab holders. */
        addTabSpecs("Public", R.id.publicTab, R.drawable.group_icon);
        addTabSpecs("Upload", R.id.uploadTab, R.drawable.upload_icon);
        addTabSpecs("Profile", R.id.profileTab, R.drawable.profile_icon);
        addTabSpecs("Account", R.id.accountTab, R.drawable.account_icon);
        addTabSpecs("Logout", R.id.logoutTab, R.drawable.logout_icon);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabId) {
                    case "Public": {
                        Fragment publicFragment = getSupportFragmentManager().findFragmentById(R.id.publicFragment);
                        if (publicFragment != null) {
                            ((PublicFragment) publicFragment).refresh();
                        }
                        break;
                    }
                    case "Profile": {
                        Fragment publicFragment = getSupportFragmentManager().findFragmentById(R.id.publicFragment);
                        if (publicFragment != null) {
                            ((PublicFragment) publicFragment).refresh();
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        });

        if (null != savedInstanceState) {
            tabHost.setCurrentTab(1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            TextView test = (TextView) findViewById(R.id.selectedPicky);
            try {
                selectedPicky = Integer.parseInt(test.getText().toString());
            } catch (NumberFormatException e) {
                Log.d("TESTING", "" + e.getMessage());
            }

            ImageView pickyView = (ImageView) findViewById(selectedPicky);

            Bitmap image = null;
            if (requestCode == 1) {
                image = UploadHelper.retrieveImageFromCamera();
                UploadHelper.updateImageView(pickyView, image);
            } else if (requestCode == 2) {
                image = UploadHelper.retrieveImageFromGallery(this, data);
                UploadHelper.updateImageView(pickyView, image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * Common method to apply specifications to each tab.
     */
    private void addTabSpecs(String tag, int contentId, int icon) {
        TabHost.TabSpec spec = tabHost.newTabSpec(tag);

        spec.setContent(contentId);
        spec.setIndicator("", ContextCompat.getDrawable(this, icon));
        tabHost.addTab(spec);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        try {
            outState.putInt("selectedPicky", Integer.parseInt(((TextView) findViewById(R.id.selectedPicky)).getText()
                    .toString()));
        } catch (NumberFormatException e) {

        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("selectedPicky")) {
                selectedPicky = savedInstanceState.getInt("selectedPicky");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}
