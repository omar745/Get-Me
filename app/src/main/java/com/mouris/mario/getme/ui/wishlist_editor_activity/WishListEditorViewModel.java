package com.mouris.mario.getme.ui.wishlist_editor_activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mouris.mario.getme.data.actors.Gift;
import com.mouris.mario.getme.data.actors.Wishlist;
import com.mouris.mario.getme.data.repositories.GeneralRepository;

import java.util.ArrayList;
import java.util.List;

public class WishListEditorViewModel extends ViewModel {

    private GeneralRepository mRepository;
    Wishlist wishlist;
    String eventDate;

    public WishListEditorViewModel() {
        mRepository = GeneralRepository.getInstance();
    }

    void saveWishList(DatabaseReference.CompletionListener completionListener) {
        wishlist.owner = mRepository.getCurrentUserId();
        mRepository.pushWishListToFirebase(wishlist, completionListener);
    }

    LiveData<Wishlist> getWishlist(String wishlistId) {
        return mRepository.getWishlistById(wishlistId);
    }

    void addNewGiftToWishlist(Gift gift) {
        gift.id = FirebaseDatabase.getInstance().getReference().push().getKey();
        wishlist.gifts_list.put(gift.id, gift);
    }
}
