package com.mouris.mario.getme.ui.friends_fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.mouris.mario.getme.data.actors.User;
import com.mouris.mario.getme.data.repositories.GeneralRepository;

import java.util.List;

public class FriendsViewModel extends ViewModel {

    private GeneralRepository mRepository;

    public FriendsViewModel() {
        mRepository = GeneralRepository.getInstance();
    }

    LiveData<List<User>> getFriendsList(User user) {
        return mRepository.getUsersByIdsList(user.friends_list.keySet());
    }

    LiveData<User> getCurrentUser() {
        return mRepository.getUserById(mRepository.getCurrentUserId());
    }

    void setNotificationsAllowedForFriend(String friendId, boolean notificationsAllowed,
                                          DatabaseReference.CompletionListener completionListener) {
        mRepository.setNotificationsAllowedForFriend(friendId,
                notificationsAllowed, completionListener);
    }
}
