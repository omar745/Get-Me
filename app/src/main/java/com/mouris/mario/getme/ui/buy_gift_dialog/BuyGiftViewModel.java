package com.mouris.mario.getme.ui.buy_gift_dialog;

import android.arch.lifecycle.ViewModel;

import com.mouris.mario.getme.data.repositories.GeneralRepository;

public class BuyGiftViewModel extends ViewModel {

    private GeneralRepository mRepository;

    boolean sharingIsEnabled;
    int numberOfSharers;

    public BuyGiftViewModel() {
        mRepository = GeneralRepository.getInstance();
    }


}
