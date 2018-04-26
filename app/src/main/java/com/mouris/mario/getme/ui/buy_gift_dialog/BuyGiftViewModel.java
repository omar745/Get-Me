package com.mouris.mario.getme.ui.buy_gift_dialog;

import android.arch.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.mouris.mario.getme.data.actors.Buyer;
import com.mouris.mario.getme.data.repositories.GeneralRepository;

public class BuyGiftViewModel extends ViewModel {

    private GeneralRepository mRepository;

    boolean sharingIsEnabled;
    int numberOfSharers = 1;

    public BuyGiftViewModel() {
        mRepository = GeneralRepository.getInstance();
    }

    private String getCurrentUserId() {
        return mRepository.getCurrentUserId();
    }

    void confirmBuyingGift(String wishlistId, String giftId,
                           DatabaseReference.CompletionListener completionListener) {
        Buyer buyer = new Buyer();
        buyer.buyer_id = getCurrentUserId();
        buyer.sharing_enabled = sharingIsEnabled;
        buyer.number_of_sharers_allowed = numberOfSharers;

        mRepository.setBuyerForGift(wishlistId, giftId, buyer, completionListener);
    }

}
