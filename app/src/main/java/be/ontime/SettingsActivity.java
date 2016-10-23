package be.ontime;

/**
 * Created by Jawad on 06-10-16.
 */

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.io.File;

import be.ontime.fragments.OpenSourceLicensesFragment;

/**
 * A {@link SettingsActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar  = (Toolbar)findViewById(R.id.toolbar);

        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.action_settings);

        getFragmentManager().beginTransaction()
                .replace(R.id.content, new PrefsFragment()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: onBackPressed(); return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PrefsFragment extends PreferenceFragment
            implements SharedPreferences.OnSharedPreferenceChangeListener,
            Preference.OnPreferenceClickListener,
            Preference.OnPreferenceChangeListener {

        private Preference preferenceLicences;
        private Preference preferenceClearCache;
        private Preference preferenceProductTour;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.pref_general);

            preferenceLicences= findPreference("prefLicences");
            preferenceLicences.setOnPreferenceClickListener(this);

            preferenceClearCache= findPreference("prefclearcache");
            preferenceClearCache.setOnPreferenceClickListener(this);

            preferenceProductTour = findPreference("prefProductTour");
            preferenceProductTour.setOnPreferenceClickListener(this);

        }

        @Override
        public boolean onPreferenceClick(Preference preference) {

            if (preference.getKey().equals("prefProductTour")) {

                startActivity(new Intent(getActivity(), IntroActivity.class));
                return true;
            }

            if (preference.getKey().equals("prefclearcache")) {

                android.app.AlertDialog.Builder popupBuilder = new android.app.AlertDialog.Builder(getActivity());
                popupBuilder.setCancelable(true);
                popupBuilder.setTitle(R.string.pref_clear_cache);
                popupBuilder.setMessage(R.string.docwillbecleared);
                popupBuilder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick
                            (DialogInterface dialog, int which) {
                    }
                });
                popupBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            File dir = getActivity().getCacheDir();
                            if (dir != null && dir.isDirectory()) {
                                if (dir.isDirectory()) {
                                    String[] children = dir.list();
                                    for (int i = 0; i < children.length; i++) {
                                        new File(dir, children[i]).delete();
                                    }
                                }
                                dir.delete();
                            }
                            preferenceClearCache.setSummary(R.string.pref_cleared_cache_summary);
                            preferenceClearCache.setEnabled(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                popupBuilder.show();

                return true;
            }
            if (preference.getKey().equals("prefLicences")) {

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                DialogFragment newFragment = OpenSourceLicensesFragment.newInstance(R.string.pref_licences);
                newFragment.show(ft, "dialog");

                return true;
            }

            return false;
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            return false;
        }
    }
}