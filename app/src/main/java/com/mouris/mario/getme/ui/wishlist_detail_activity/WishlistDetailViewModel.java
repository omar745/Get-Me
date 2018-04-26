package com.mouris.mario.getme.ui.wishlist_detail_activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
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

    String getCurrentUserId() {
        return mRepository.getCurrentUserId();
    }

    void shareInGift(String wishlistId, String giftId,
                     DatabaseReference.CompletionListener completionListener) {
        mRepository.shareInGift(wishlistId, giftId, getCurrentUserId(), completionListener);
    }

    void cancelBuyingGift(String wishlistId, String giftId,
                          DatabaseReference.CompletionListener completionListener) {
        mRepository.cancelBuyingGift(wishlistId, giftId, completionListener);
    }

    void cancelSharingGift(String wishlistId, String giftId,
                           DatabaseReference.CompletionListener completionListener) {
        mRepository.cancelSharingGift(wishlistId, giftId, getCurrentUserId(), completionListener);
    }
}
