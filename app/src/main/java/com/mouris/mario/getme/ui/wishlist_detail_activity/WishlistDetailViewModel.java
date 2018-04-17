package com.mouris.mario.getme.ui.wishlist_detail_activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.mouris.mario.getme.data.actors.User;
import com.mouris.mario.getme.data.actors.Wishlist;
import com.mouris.mario.getme.data.repositories.GeneralRepository;

public class WishlistDetailViewModel extends ViewModel {

    private GeneralRepository mRepository;

    public WishlistDetailViewModel() {
        mRepository = GeneralRepository.getInstance();
    }

    LiveData<Wishlist> getWishList(String wishlistId) {
        return mRepository.getWishlistById(wishlistId);
    }

    LiveData<User> getUser(String userId) {
        return mRepository.getUserById(userId);
    }
}
