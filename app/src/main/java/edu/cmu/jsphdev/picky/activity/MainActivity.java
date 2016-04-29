package edu.cmu.jsphdev.picky.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.entities.User;
import edu.cmu.jsphdev.picky.util.CurrentSession;

/**
 * Activity to create tabs for Login and Sign-Up.
 */
public class MainActivity extends AppCompatActivity {

    private static final int ALL_PERMISSIONS_REQUEST_CODE = 1;
    private static final String[] ALL = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET};

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] permissionsMissing = missingPermissions();
        ActivityCompat.requestPermissions(this, permissionsMissing, ALL_PERMISSIONS_REQUEST_CODE);
        String existingToken = PreferenceManager.getDefaultSharedPreferences(this).getString("existingUser", "");
        if (!existingToken.isEmpty()) {
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

    private String[] missingPermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();

        for (String permission: ALL) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(permission);
            }

        }
        return new String[permissionsNeeded.size()];
    }

    private void addTabSpecs(String tag, int contentId) {
        TabHost.TabSpec spec = tabHost.newTabSpec(tag);
        spec.setContent(contentId);
        spec.setIndicator(tag);
        tabHost.addTab(spec);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[]grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_REQUEST_CODE: {
                Map<String, Integer> perms = new HashMap<String, Integer>();

                for (String permission: ALL) {
                    perms.put(permission, PackageManager.PERMISSION_GRANTED);
                }
                // Fill with results
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                }
                for (String permission: ALL) {
                    if (perms.get(permission) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this,
                                String.format("%s permission is denied, some functions might malfunction", permission),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
