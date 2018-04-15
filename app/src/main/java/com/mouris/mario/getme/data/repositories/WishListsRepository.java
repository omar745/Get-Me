package com.mouris.mario.getme.data.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mouris.mario.getme.data.FirebaseQueryLiveData;
import com.mouris.mario.getme.data.actors.Gift;
import com.mouris.mario.getme.data.actors.WishList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WishListsRepository {

    private DatabaseReference mWishListsDbReference;

    //A hash map to keep track of existing live data objects not to create new ones when unnecessary
    private Map<String, LiveData<WishList>> mActiveLiveDataObjects = new HashMap<>();

    //The liveData object that holds all the WishLists
    private LiveData<List<WishList>> mWishListsLiveData;

    private static WishListsRepository sInstance;

    static WishListsRepository getInstance() {
        if (sInstance == null) {
            sInstance = new WishListsRepository();
        }
        return sInstance;
    }

    private WishListsRepository() {
        //Initiate the WishLists database reference
        mWishListsDbReference = FirebaseDatabase.getInstance()
                .getReference().child(WishList.ROOT_REF_NAME);
        mWishListsDbReference.keepSynced(true);
    }

    //A method to retrieve a LiveData containing all the WishLists for my friends.
    // (It creates a new object if null)
    LiveData<List<WishList>> getWishListsLiveData(Set<String> friendsIds) {
        if (mWishListsLiveData == null) {
            //Create a liveData that returns a DataSnapshot
            FirebaseQueryLiveData dataSnapshotsLiveData =
                    new FirebaseQueryLiveData(mWishListsDbReference);
            mWishListsLiveData = Transformations.map(dataSnapshotsLiveData, dataSnapshots -> {
                if (dataSnapshots != null) {
                    List<WishList> wishListsList = new ArrayList<>();
                    for (DataSnapshot wishListSnapshot: dataSnapshots.getChildren()) {
                        WishList newWishList = wishListSnapshot.getValue(WishList.class);
                        if (newWishList != null && friendsIds.contains(newWishList.owner)) {
                            newWishList.id = wishListSnapshot.getKey();
                            wishListsList.add(newWishList);
                        }

                    }
                    return wishListsList;
                } else {
                    return null;
                }
            });
        }

        return mWishListsLiveData;
    }

    //A method that retrieves the LiveData object observing this WishList if existing, or creates a
    // new one if not and adds it to the mActiveLiveDataObjects
    LiveData<WishList> getWishListLiveDataById(String wishListId) {
        if (!mActiveLiveDataObjects.containsKey(wishListId)) {
            //Create a liveData that returns a DataSnapshot
            FirebaseQueryLiveData dataSnapshotLiveData =
                    new FirebaseQueryLiveData(mWishListsDbReference.child(wishListId));
            LiveData<WishList> wishListLiveData =
                    Transformations.map(dataSnapshotLiveData, dataSnapshot -> {
                        if (dataSnapshot != null) {
                            WishList wishList = dataSnapshot.getValue(WishList.class);
                            if (wishList != null) wishList.id = dataSnapshot.getKey();
                            return wishList;
                        } else {
                            return null;
                        }
                    });
            mActiveLiveDataObjects.put(wishListId, wishListLiveData);
        }

        return mActiveLiveDataObjects.get(wishListId);
    }

    void deleteWishListFromFirebase(String wishListId,
                                DatabaseReference.CompletionListener completionListener) {
        mWishListsDbReference.child(wishListId).removeValue(completionListener);
    }

    String pushWishListToFirebase(WishList wishList,
                       DatabaseReference.CompletionListener completionListener) {
        if (wishList.id == null) {
            wishList.id = mWishListsDbReference.push().getKey();
        }
        mWishListsDbReference.child(wishList.id).setValue(wishList, completionListener);
        return wishList.id;
    }

    void addGiftToWishList(String wishListId, Gift gift,
                           DatabaseReference.CompletionListener completionListener) {
        if (gift.id == null) {
            gift.id = mWishListsDbReference.child(wishListId).push().getKey();
        }
        mWishListsDbReference.child(wishListId).child(gift.id).setValue(gift, completionListener);
    }
}
