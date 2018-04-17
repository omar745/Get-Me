package com.mouris.mario.getme.ui.wishlist_detail_activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.mouris.mario.getme.data.actors.WishList;
import com.mouris.mario.getme.data.repositories.GeneralRepository;

public class WishlistsDetailViewModel extends ViewModel {

    private GeneralRepository mRepository;

    public WishlistsDetailViewModel() {
        mRepository = GeneralRepository.getInstance();
    }

    LiveData<WishList> getWishList(String wishlistId) {
        return mRepository.getWishListById(wishlistId);
    }
}