package com.mouris.mario.getme.ui.my_wishlists_fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mouris.mario.getme.R;
import com.mouris.mario.getme.data.actors.User;
import com.mouris.mario.getme.ui.adapters.WishListsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyWishListsFragment extends Fragment {

    @BindView(R.id.wishlists_recyclerView) RecyclerView mWishListsRv;
    @BindView(R.id.empty_placeholder) LinearLayout mEmptyLayout;

    private MyWishListsViewModel mViewModel;

    public MyWishListsFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_wishlists, container, false);
        ButterKnife.bind(this, rootView);

        mViewModel = ViewModelProviders.of(this).get(MyWishListsViewModel.class);

        mWishListsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        WishListsAdapter wishListsAdapter =
                new WishListsAdapter(null, null, false);
        mWishListsRv.setAdapter(wishListsAdapter);

        mViewModel.getCurrentUser().observe(this, currentUser -> {
            if (currentUser != null) {
                //Add the current user to the adapter
                List<User> adapterListOfUsers = new ArrayList<>();
                adapterListOfUsers.add(currentUser);
                wishListsAdapter.setUsersList(adapterListOfUsers);
            }
        });

        mViewModel.getWishLists().observe(this, wishLists -> {
            if (wishLists != null) {

                if (wishLists.size() == 0) {
                    mEmptyLayout.setVisibility(View.VISIBLE);
                } else {
                    mEmptyLayout.setVisibility(View.GONE);
                }

                wishListsAdapter.setWishLists(wishLists);
            }
        });

        return rootView;
    }
}
