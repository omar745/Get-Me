package com.mouris.mario.getme.ui.my_wishlists_fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.mouris.mario.getme.data.actors.User;
import com.mouris.mario.getme.data.actors.Wishlist;
import com.mouris.mario.getme.data.repositories.GeneralRepository;

import java.util.List;

public class MyWishListsViewModel extends ViewModel {

    private GeneralRepository mRepository;

    public MyWishListsViewModel() {
        mRepository = GeneralRepository.getInstance();
    }

    LiveData<List<Wishlist>> getWishLists() {
        return mRepository.getMyWishListsLiveData(mRepository.getCurrentUserId());
    }

    LiveData<User> getCurrentUser() {
        return mRepository.getUserById(mRepository.getCurrentUserId());
    }
}
