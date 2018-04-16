package com.mouris.mario.getme.ui.my_wishlists_fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mouris.mario.getme.R;

public class MyWishListsFragment extends Fragment {

    public MyWishListsFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_wishlists, container, false);


        return rootView;
    }
}
