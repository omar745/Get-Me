package com.mouris.mario.getme.data.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mouris.mario.getme.data.FirebaseQueryLiveData;
import com.mouris.mario.getme.data.actors.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UsersRepository {

    private DatabaseReference mUsersDbReference;

    //A hash map to keep track of existing live data objects not to create new ones when unnecessary
    private Map<String, LiveData<User>> mActiveLiveDataObjects = new HashMap<>();

    //The liveData object that holds all the users
    private LiveData<List<User>> mUsersLiveData;

    private static UsersRepository sInstance;

    static UsersRepository getInstance() {
        if (sInstance == null) {
            sInstance = new UsersRepository();
        }
        return sInstance;
    }

    private UsersRepository() {
        //Initiate the Users database reference
        mUsersDbReference = FirebaseDatabase.getInstance().getReference().child(User.ROOT_REF_NAME);
        mUsersDbReference.keepSynced(true);
    }

    //A method to retrieve a LiveData containing all the users. (It creates a new object if null)
    LiveData<List<User>> getUsersLiveData() {
        if (mUsersLiveData == null) {
            //Create a liveData that returns a DataSnapshot
            FirebaseQueryLiveData dataSnapshotsLiveData =
                    new FirebaseQueryLiveData(mUsersDbReference);
            mUsersLiveData = Transformations.map(dataSnapshotsLiveData, dataSnapshots -> {
                if (dataSnapshots != null) {
                    List<User> usersList = new ArrayList<>();
                    for (DataSnapshot userSnapshot: dataSnapshots.getChildren()) {
                        User newUser = userSnapshot.getValue(User.class);
                        if (newUser != null) newUser.id = userSnapshot.getKey();
                        usersList.add(newUser);
                    }
                    return usersList;
                } else {
                    return null;
                }
            });
        }

        return mUsersLiveData;
    }

    //A method to retrieve a specific list of users
    LiveData<List<User>> getUsersByIdsList(Set<String> listOfIds) {
        LiveData<List<User>> listOfUsersLiveData;
        FirebaseQueryLiveData dataSnapshotsLiveData = new FirebaseQueryLiveData(mUsersDbReference);
        listOfUsersLiveData = Transformations.map(dataSnapshotsLiveData, dataSnapshots -> {
            if (dataSnapshots != null) {
                List<User> usersList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshots.getChildren()) {
                    User newUser = userSnapshot.getValue(User.class);
                    if (newUser != null) {
                        newUser.id = userSnapshot.getKey();
                        if (listOfIds.contains(newUser.id)) {
                            usersList.add(newUser);
                        }
                    }
                }
                return usersList;
            } else {
                return null;
            }
        });

        return listOfUsersLiveData;
    }

    //A method that retrieves the LiveData object observing this user if existing, or creates a
    // new one if not and adds it to the mActiveLiveDataObjects
    LiveData<User> getUserLiveDataById(String userId) {
        if (!mActiveLiveDataObjects.containsKey(userId)) {
            //Create a liveData that returns a DataSnapshot
            FirebaseQueryLiveData dataSnapshotLiveData =
                    new FirebaseQueryLiveData(mUsersDbReference.child(userId));
            LiveData<User> userLiveData =
                    Transformations.map(dataSnapshotLiveData, dataSnapshot -> {
                if (dataSnapshot != null) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) user.id = dataSnapshot.getKey();
                    return user;
                } else {
                    return null;
                }
            });
            mActiveLiveDataObjects.put(userId, userLiveData);
        }

        return mActiveLiveDataObjects.get(userId);
    }

    void deleteUserFromFirebase(String userId,
                                DatabaseReference.CompletionListener completionListener) {
        mUsersDbReference.child(userId).removeValue(completionListener);
    }

    void createNewUser(String userId, User user,
                       DatabaseReference.CompletionListener completionListener) {
        mUsersDbReference.child(userId).setValue(user, completionListener);
    }

    void pushFriendsList(String userId, HashMap<String, Boolean> friendsList,
                         DatabaseReference.CompletionListener completionListener) {
        mUsersDbReference.child(userId)
                .child(User.FRIENDS_LIST).setValue(friendsList, completionListener);
    }

    void setNotificationsAllowedForFriend(String currentUser,
                                          String friendId, boolean notificationsAllowed,
                                          DatabaseReference.CompletionListener completionListener) {
        mUsersDbReference.child(currentUser).child(User.FRIENDS_LIST)
                .child(friendId).setValue(notificationsAllowed, completionListener);
    }
}
