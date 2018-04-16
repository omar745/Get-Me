package com.mouris.mario.getme.ui.home_fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.mouris.mario.getme.data.actors.User;
import com.mouris.mario.getme.data.actors.WishList;
import com.mouris.mario.getme.data.repositories.GeneralRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private GeneralRepository mRepository;

    public HomeViewModel() {
        mRepository = GeneralRepository.getInstance();
    }

    LiveData<List<WishList>> getWishLists(User currentUser) {
        Log.i("TEST", currentUser.friends_list.keySet().toString());
        return mRepository.getWishLists(currentUser.friends_list.keySet());
    }

    LiveData<User> getCurrentUser() {
        return mRepository.getUserById(mRepository.getCurrentUserId());
    }

    LiveData<List<User>> getUsersList() {
        return mRepository.getUsers();
    }
}
