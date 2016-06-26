package be.b_rail;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import be.b_rail.Utils.PrefsUtils;
import be.b_rail.fragments.BaseFragment;
import be.b_rail.fragments.SncbFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout                drawer;
    private TextView                    userName;

    private int 						mCurrentTitle;
    private int 						mSelectedFragment;

    private static final int 			CASE_SNCB 		  = 0;
    //Used in savedInstanceState
    private static String               BUNDLE_SELECTEDFRAGMENT 		= "BDL_SELFRG";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_SELECTEDFRAGMENT, mSelectedFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Check if the EULA has been accepted; if not, show it.
        if (!PrefsUtils.isTosAccepted(this)) {
            startActivity(new Intent(this, IntroActivity.class));
            finish();
        }else{

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            userName	= (TextView) navigationView.getHeaderView(0).findViewById(R.id.textView_username);

            //-----------------------------------------------------------------

            if (savedInstanceState != null) {
                mSelectedFragment = savedInstanceState.getInt(BUNDLE_SELECTEDFRAGMENT);

                if ( getSupportFragmentManager().findFragmentById(R.id.frame_container) == null)
                    selectFragment(mSelectedFragment);

            }else {
                selectFragment(CASE_SNCB);
            }

            AccountManager accountManager = AccountManager.get(this);
            Account account = getAccount(accountManager);

            if (account != null) {
                //titleContact.setText(""+account.name);
                userName.setText(""+account.name);
            }
        }
    }
    private Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //Check to see which item was being clicked and perform appropriate action
        switch (id){

            //Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.nav_sncb:
                selectFragment(CASE_SNCB);
                return true;
            case R.id.nav_share:
                //startActivity( new Intent(MainActivity.this, HelpActivity.class));
                return true;
            case R.id.nav_settings:
                 startActivity( new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            default:
                Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                return true;
        }
    }
    private void selectFragment(int position) {

        switch (position) {

            case CASE_SNCB:  openFragment(SncbFragment.newInstance("",""));  break;

            default: break;
        }

    }
    private void openFragment(BaseFragment baseFragment) {
        if (baseFragment != null) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, baseFragment)
                    .commitAllowingStateLoss();

            if (baseFragment.getTitleResourceId() > 0)
                mCurrentTitle = baseFragment.getTitleResourceId();
        }
    }
}
