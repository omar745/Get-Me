package com.mouris.mario.getme.ui.home_fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import com.mouris.mario.getme.data.actors.Wishlist;
import com.mouris.mario.getme.ui.adapters.WishListsAdapter;
import com.mouris.mario.getme.ui.wishlist_detail_activity.WishlistDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment
        implements WishListsAdapter.WishListViewHolder.OnItemClickListener {

    @BindView(R.id.wishlists_recyclerView) RecyclerView mWishListsRv;
    @BindView(R.id.empty_placeholder) LinearLayout mEmptyLayout;

    private HomeViewModel mViewModel;

    public HomeFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);

        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        mWishListsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        WishListsAdapter wishListsAdapter =
                new WishListsAdapter(null, null,
                        true, this);
        mWishListsRv.setAdapter(wishListsAdapter);

        mViewModel.getCurrentUser().observe(this, currentUser -> {
            if (currentUser != null) {
                mViewModel.getWishLists(currentUser).observe(this, wishLists -> {
                    if (wishLists != null) {

                        if (wishLists.size() == 0) {
                            mEmptyLayout.setVisibility(View.VISIBLE);
                        } else {
                            mEmptyLayout.setVisibility(View.GONE);
                        }

                        wishListsAdapter.setWishLists(wishLists);
                    }
                });
            }
        });

        mViewModel.getUsersList().observe(this, users -> {
            if (users != null) {
                wishListsAdapter.setUsersList(users);
            }
        });

        return rootView;
    }

    @Override
    public void onWishlistClickListener(Wishlist wishlist) {
        Intent wishlistDetailIntent = new Intent(getContext(), WishlistDetailActivity.class);
        wishlistDetailIntent.putExtra(WishlistDetailActivity.WISHLIST_ID_EXTRA, wishlist.id);
        startActivity(wishlistDetailIntent);
    }
}
