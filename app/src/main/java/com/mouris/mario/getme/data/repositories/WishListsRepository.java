package com.mouris.mario.getme.data.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mouris.mario.getme.data.FirebaseQueryLiveData;
import com.mouris.mario.getme.data.actors.Buyer;
import com.mouris.mario.getme.data.actors.Gift;
import com.mouris.mario.getme.data.actors.Wishlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WishListsRepository {

    private DatabaseReference mWishListsDbReference;

    //A hash map to keep track of existing live data objects not to create new ones when unnecessary
    private Map<String, LiveData<Wishlist>> mActiveLiveDataObjects = new HashMap<>();

    //The liveData object that holds all the friends WishLists
    private LiveData<List<Wishlist>> mFriendWishListsLiveData;
    private Set<String> mFriendsIds;

    //The liveData object that holds all the current user's WishLists
    private LiveData<List<Wishlist>> mMyWishListsLiveData;

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
                .getReference().child(Wishlist.ROOT_REF_NAME);
        mWishListsDbReference.keepSynced(true);
    }

    //A method to retrieve a LiveData containing all the WishLists for my friends.
    // (It creates a new object if null)
    LiveData<List<Wishlist>> getFriendsWishListsLiveData(Set<String> friendsIds) {
        if (mFriendWishListsLiveData == null || !friendsIds.equals(mFriendsIds)) {
            mFriendsIds = friendsIds;
            //Create a liveData that returns a DataSnapshot
            FirebaseQueryLiveData dataSnapshotsLiveData =
                    new FirebaseQueryLiveData(mWishListsDbReference);
            mFriendWishListsLiveData = Transformations.map(dataSnapshotsLiveData, dataSnapshots -> {
                if (dataSnapshots != null) {
                    List<Wishlist> wishlist = new ArrayList<>();
                    for (DataSnapshot wishListSnapshot: dataSnapshots.getChildren()) {
                        Wishlist newWishlist = wishListSnapshot.getValue(Wishlist.class);
                        if (newWishlist != null && friendsIds.contains(newWishlist.owner)) {
                            newWishlist.id = wishListSnapshot.getKey();
                            wishlist.add(newWishlist);
                        }

                    }
                    return wishlist;
                } else {
                    return null;
                }
            });
        }

        return mFriendWishListsLiveData;
    }

    //A method to retrieve a LiveData containing all my WishLists.
    // (It creates a new object if null)
    LiveData<List<Wishlist>> getMyWishListsLiveData(String currentUserId) {
        if (mMyWishListsLiveData == null) {
            //Create a liveData that returns a DataSnapshot
            FirebaseQueryLiveData dataSnapshotsLiveData =
                    new FirebaseQueryLiveData(mWishListsDbReference);
            mMyWishListsLiveData = Transformations.map(dataSnapshotsLiveData, dataSnapshots -> {
                if (dataSnapshots != null) {
                    List<Wishlist> wishlist = new ArrayList<>();
                    for (DataSnapshot wishListSnapshot: dataSnapshots.getChildren()) {
                        Wishlist newWishlist = wishListSnapshot.getValue(Wishlist.class);
                        if (newWishlist != null && newWishlist.owner.equals(currentUserId)) {
                            newWishlist.id = wishListSnapshot.getKey();
                            wishlist.add(newWishlist);
                        }

                    }
                    return wishlist;
                } else {
                    return null;
                }
            });
        }

        return mMyWishListsLiveData;
    }

    //A method that retrieves the LiveData object observing this WishList if existing, or creates a
    // new one if not and adds it to the mActiveLiveDataObjects
    LiveData<Wishlist> getWishListLiveDataById(String wishListId) {
        if (!mActiveLiveDataObjects.containsKey(wishListId)) {
            //Create a liveData that returns a DataSnapshot
            FirebaseQueryLiveData dataSnapshotLiveData =
                    new FirebaseQueryLiveData(mWishListsDbReference.child(wishListId));
            LiveData<Wishlist> wishListLiveData =
                    Transformations.map(dataSnapshotLiveData, dataSnapshot -> {
                        if (dataSnapshot != null) {
                            Wishlist wishlist = dataSnapshot.getValue(Wishlist.class);
                            if (wishlist != null) wishlist.id = dataSnapshot.getKey();
                            return wishlist;
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

    String pushWishListToFirebase(Wishlist wishlist,
                       DatabaseReference.CompletionListener completionListener) {
        if (wishlist.id == null) {
            wishlist.id = mWishListsDbReference.push().getKey();
        }
        mWishListsDbReference.child(wishlist.id).setValue(wishlist, completionListener);
        return wishlist.id;
    }

    void addGiftToWishList(String wishListId, Gift gift,
                           DatabaseReference.CompletionListener completionListener) {
        if (gift.id == null) {
            gift.id = mWishListsDbReference.child(wishListId).push().getKey();
        }
        mWishListsDbReference.child(wishListId)
                .child(Wishlist.GIFTS_LIST).child(gift.id).setValue(gift, completionListener);
    }

    public void setBuyerForGift(String wishlistId, String giftId, Buyer buyer,
                                DatabaseReference.CompletionListener completionListener) {
        mWishListsDbReference.child(wishlistId).child(Wishlist.GIFTS_LIST).child(giftId)
                .child(Gift.BUYER_INFO).setValue(buyer, completionListener);
    }

    public void shareInGift(String wishlistId, String giftId, String userId,
                            DatabaseReference.CompletionListener completionListener) {
        mWishListsDbReference.child(wishlistId).child(Wishlist.GIFTS_LIST).child(giftId)
                .child(Gift.BUYER_INFO).child(Buyer.SHARER_IDS)
                .child(userId).setValue(true, completionListener);
    }

    public void cancelBuyingGift(String wishlistId, String giftId,
                                 DatabaseReference.CompletionListener completionListener) {
        mWishListsDbReference.child(wishlistId).child(Wishlist.GIFTS_LIST).child(giftId)
                .child(Gift.BUYER_INFO).removeValue(completionListener);
    }

    public void cancelSharingGift(String wishlistId, String giftId, String userId,
                                  DatabaseReference.CompletionListener completionListener) {
        mWishListsDbReference.child(wishlistId).child(Wishlist.GIFTS_LIST).child(giftId)
                .child(Gift.BUYER_INFO).child(Buyer.SHARER_IDS)
                .child(userId).removeValue(completionListener);
    }
}
