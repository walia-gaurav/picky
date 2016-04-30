package edu.cmu.jsphdev.picky.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
//    Bitmap leftPhoto;
//    Bitmap rightPhoto;

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Log.d("TESTING", "HomeActivity created!");

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
                if (tabId.equals("Public"))  {
                    Fragment publicFragment = getSupportFragmentManager().findFragmentById(R.id.publicFragment);

                    if (publicFragment != null) {
                        ((PublicFragment)publicFragment).refresh();
                    }
                }

            }
        });

        if (null != savedInstanceState) {
            tabHost.setCurrentTab(1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TESTING", "Coming to result");

        if (resultCode == RESULT_OK) {
            /* Get hold of the button from which imageCapture was called. */

            TextView test = (TextView) findViewById(R.id.selectedPicky);
            try {
                selectedPicky = Integer.parseInt(test.getText().toString());
            } catch (NumberFormatException e) {
                Log.d("TESTING", "" + e.getMessage());

            }

            ImageView pickyView = (ImageView) findViewById(selectedPicky);

            Log.d("TESTING", "RequestCode: " + requestCode);
            Bitmap image = null;
            if (requestCode == 1) {
                Log.d("TESTING", "Coming to result");
                image = UploadHelper.retrieveImageFromCamera();
                UploadHelper.updateImageView(pickyView, image);
            } else if (requestCode == 2) {
                image = UploadHelper.retrieveImageFromGallery(this, data);
                UploadHelper.updateImageView(pickyView, image);
            }
//
//            if (selectedPicky == R.id.choice1) {
//                leftPhoto = image;
//                UploadHelper.updateImageView((ImageView) findViewById(R.id.choice2), rightPhoto);
//            } else if (selectedPicky == R.id.choice2) {
//                rightPhoto = image;
//                UploadHelper.updateImageView((ImageView) findViewById(R.id.choice1), leftPhoto);
//            }
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
//        outState.putParcelable("leftPhoto", leftPhoto);
//        outState.putParcelable("rightPhoto", rightPhoto);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("selectedPicky")) {
                selectedPicky = savedInstanceState.getInt("selectedPicky");
//                leftPhoto = savedInstanceState.getParcelable("leftPhoto");
//                rightPhoto = savedInstanceState.getParcelable("rightPhoto");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}
