package com.mouris.mario.getme.ui.wishlist_editor_activity;

import android.arch.lifecycle.ViewModel;

import com.mouris.mario.getme.data.actors.WishList;
import com.mouris.mario.getme.data.repositories.GeneralRepository;

public class WishListEditorViewModel extends ViewModel {

    private GeneralRepository mRepository;
    WishList wishList;

    public WishListEditorViewModel() {
        mRepository = GeneralRepository.getInstance();
    }


}
