package be.b_rail.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import be.b_rail.R;

/**
 * Created by Jawad on 09-02-16.
 */
public class StationsFragment extends BaseFragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener{



    private Snackbar                            snackBar;
    // The SearchView for doing filtering.
    private SearchView                          mSearchView;
    // If non-null, this is the current filter the user has provided.
    private String                              mCurFilter;

    private boolean 							list_visible 		= true;

    @Override
    public int getTitleResourceId() {
        return R.string.Stations;
    }

    /**
     * Creates a new instance of a StationsFragment.
     */
    public static StationsFragment newInstance() {
        return new StationsFragment();
    }
    /***************************************************************
     * Menu*********************************************************
     ***************************************************************/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.menu_stations, menu);
        // Place an action bar item for searching.

        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setQueryHint(getString(R.string.search_hint));
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);
        mSearchView.setIconifiedByDefault(true);

        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
       /* boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerLinear);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);*/
        MenuItem menuItem = menu.findItem(R.id.action_type_view);
        if (list_visible) {
            menuItem.setTitle(R.string.type_grid);
        } else {
            menuItem.setTitle(R.string.type_list);
        }
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {

            case R.id.action_search: return true;
            case R.id.action_type_view: return true;

            default:return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_stations, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    /*****************************************************************
     * Search*********************************************************
     ***************************************************************/
    @Override
    public boolean onClose() {
        if (!TextUtils.isEmpty(mSearchView.getQuery())) {
            mSearchView.setQuery(null, true);
        }
        clearSearch();
        return true;
    }
    private void clearSearch(){
        try {
           // mAdapter.getFilter().filter("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        try{

        }catch(IllegalStateException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

}
