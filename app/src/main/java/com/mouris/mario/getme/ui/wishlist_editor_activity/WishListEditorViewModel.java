package com.mouris.mario.getme.ui.wishlist_editor_activity;

import android.arch.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.mouris.mario.getme.data.actors.Gift;
import com.mouris.mario.getme.data.actors.Wishlist;
import com.mouris.mario.getme.data.repositories.GeneralRepository;

import java.util.ArrayList;
import java.util.List;

public class WishListEditorViewModel extends ViewModel {

    private GeneralRepository mRepository;
    Wishlist wishlist;
    List<Gift> giftsList;
    String eventDate;

    public WishListEditorViewModel() {
        mRepository = GeneralRepository.getInstance();
        giftsList = new ArrayList<>();
    }

    void saveWishList(DatabaseReference.CompletionListener completionListener) {
        wishlist.owner = mRepository.getCurrentUserId();
        mRepository.pushWishListToFirebase(wishlist, giftsList, completionListener);
    }
}
