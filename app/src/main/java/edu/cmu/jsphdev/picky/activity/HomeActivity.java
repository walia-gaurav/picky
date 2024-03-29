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
import edu.cmu.jsphdev.picky.fragment.ProfileFragment;
import edu.cmu.jsphdev.picky.fragment.PublicFragment;
import edu.cmu.jsphdev.picky.util.UploadHelper;

/**
 * HomeActivity that initializes the tab widget in the app, for different menu.
 */
public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    /* Resource Id for the selected picky (to identify between left/right buttons */
    private int selectedPicky;
    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tabHost = (TabHost) findViewById(R.id.homeTabHost);
        tabHost.setup();

        /* Initializing different tab holders. */
        addTabSpecs("Public", R.id.publicTab, R.drawable.picky_logo);
        addTabSpecs("Upload", R.id.uploadTab, R.drawable.upload_picky_logo);
        addTabSpecs("Profile", R.id.profileTab, R.drawable.profile_icon);
        addTabSpecs("Account", R.id.accountTab, R.drawable.account_logo);

        /*
        Refreshing fragments on tabChangeEvent.
         */
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
                        Fragment profileFragment = getSupportFragmentManager().findFragmentByTag
                                ("profile_fragment_tag");
                        if (profileFragment != null) {
                            ((ProfileFragment) profileFragment).loadPickyHistory();
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

    /**
     * This is called when the UploadFragment completes fetching the images either from the camera or gallery.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            TextView selectedPickyHiddenField = (TextView) findViewById(R.id.selectedPicky);
            try {
                selectedPicky = Integer.parseInt(selectedPickyHiddenField.getText().toString());
            } catch (NumberFormatException ex) {
                Log.e(TAG, "Problem parsing number", ex);
            }

            ImageView pickyView = (ImageView) findViewById(selectedPicky);

            Bitmap image = null;
            if (requestCode == 1) {
                /*
                For retrieval from Camera.
                 */
                image = UploadHelper.retrieveImageFromCamera();
                UploadHelper.updateImageView(pickyView, image);
            } else if (requestCode == 2) {
                /*
                For retrieval from Gallery.
                 */
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

    /**
     * Gets called just before this activity gets destroyed. Saving picky side to refresh the activity at a later
     * point. In low-memory devices, the camera activity switches off the app's activity, and only then clicks a
     * picture. Once the picture is taken, the activity is restored again. The state has been manually handled.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        try {
            outState.putInt("selectedPicky", Integer.parseInt(((TextView) findViewById(R.id.selectedPicky)).getText()
                    .toString()));
        } catch (NumberFormatException ex) {
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * Gets called when the activity restarts, and fetchs the saved instance.
     */
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
