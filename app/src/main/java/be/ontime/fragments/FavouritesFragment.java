package be.ontime.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import java.util.List;

import be.ontime.Models.Favourite;
import be.ontime.R;
import be.ontime.Utils.PrefsUtils;
import be.ontime.adapters.FavouritesAdapter;

/**
 * Created by Jawad on 11-02-16.
 */
public class FavouritesFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView                favouritesListRecycleView;
    private RecyclerView.Adapter        mFavouritesAdapter;
    private RecyclerView.LayoutManager  mLayoutManager;
    private List<Favourite>             favouritesList;


    private ViewFlipper                 vf;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JourneyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouritesFragment newInstance(String param1, String param2) {
        FavouritesFragment fragment = new FavouritesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);


        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        favouritesList  = PrefsUtils.loadFavourites(getContext());
        if(favouritesList != null && !favouritesList.isEmpty()){
            mLayoutManager   = new LinearLayoutManager(getActivity());
            mFavouritesAdapter = new FavouritesAdapter(getActivity(),favouritesList,FavouritesFragment.this);
            favouritesListRecycleView.setLayoutManager(mLayoutManager);// requires *wrapped* adapter
            favouritesListRecycleView.setAdapter(mFavouritesAdapter);
            if(vf.getDisplayedChild() == 0){
                vf.showNext();
            }

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vf  = (ViewFlipper) getActivity().findViewById(R.id.viewFlipper_2);
        favouritesListRecycleView = (RecyclerView) getActivity().findViewById(R.id.favourites_recyclerview);
        favouritesList  = PrefsUtils.loadFavourites(getContext());
        if(favouritesList != null && !favouritesList.isEmpty()){
            mLayoutManager   = new LinearLayoutManager(getActivity());
            mFavouritesAdapter = new FavouritesAdapter(getActivity(),favouritesList,FavouritesFragment.this);
            favouritesListRecycleView.setLayoutManager(mLayoutManager);// requires *wrapped* adapter
            favouritesListRecycleView.setAdapter(mFavouritesAdapter);
            vf.showNext();
        }
    }

    public void showFlip(){
        vf.showPrevious();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
