package com.mouris.mario.getme.ui.adapters;

import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mouris.mario.getme.R;
import com.mouris.mario.getme.data.actors.Gift;
import com.mouris.mario.getme.utils.ListUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.GiftViewHolder> {

    private List<Gift> mGiftsList;
    private String mCurrentUserId;
    private GiftViewHolder.OnItemClickListener mListener;

    public GiftsAdapter(List<Gift> giftsList, String currentUserId, GiftViewHolder.OnItemClickListener listener) {
        mGiftsList = giftsList;
        mCurrentUserId = currentUserId;
        mListener = listener;
    }

    public void setGiftsList(HashMap<String, Gift> gifts_list) {
        mGiftsList = ListUtils.getGiftsListFromMap(gifts_list);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.item_gift, parent, false);

        return new GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftViewHolder giftVh, int position) {
        Gift gift = mGiftsList.get(position);

        setTextOrHide(giftVh.giftNameTv, gift.gift_name);
        setTextOrHide(giftVh.brandTv, gift.brand);
        setTextOrHide(giftVh.descriptionTv, gift.description);
        setTextOrHide(giftVh.webLinkTv, gift.web_link);

        if (mCurrentUserId != null) {
            giftVh.buyButton.setVisibility(View.VISIBLE);
            giftVh.setButtonState(gift, mCurrentUserId);
        } else {
            giftVh.buyButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (mGiftsList == null) return 0;
        return mGiftsList.size();
    }

    private void setTextOrHide(TextView textView, String string) {
        if (string != null) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(string);
        } else {
            textView.setVisibility(View.GONE);
        }
    }


    //----------------------------------------------------------------------------------------------

    public static class GiftViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.gift_name_textView) TextView giftNameTv;
        @BindView(R.id.brand_textView) TextView brandTv;
        @BindView(R.id.description_textView) TextView descriptionTv;
        @BindView(R.id.web_link_textView) TextView webLinkTv;
        @BindView(R.id.buy_gift_button) Button buyButton;

        BuyButtonState buyButtonState;

        GiftViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setButtonState(Gift gift, String userId) {

            buyButton.setVisibility(View.VISIBLE);

            if (gift.isBought()) {
                //Gift is already bough
                //First, check if user is the original buyer
                if (gift.isBuyer(userId)) {
                    //This user is the original buyer, show cancel button
                    buyButtonState = BuyButtonState.buyer_state;
                    buyButton.setText(R.string.cancel_button);
                    if (Build.VERSION.SDK_INT >= 21) {
                        buyButton.getBackground().setColorFilter(
                                ContextCompat.getColor(itemView.getContext(), R.color.colorAccent),
                                PorterDuff.Mode.MULTIPLY);
                    }
                } else {
                    //Check to see if user is sharer
                    if (gift.isSharer(userId)) {
                        //This user is a sharer, show cancel button
                        buyButtonState = BuyButtonState.sharer_state;
                        buyButton.setText(R.string.cancel_button);
                        if (Build.VERSION.SDK_INT >= 21) {
                            buyButton.getBackground().setColorFilter(
                                    ContextCompat.getColor(itemView.getContext(), R.color.colorAccent),
                                    PorterDuff.Mode.MULTIPLY);
                        }
                    } else {
                        //Check to see if gift is open for sharing
                        if (gift.isOpenForSharing()) {
                            buyButtonState = BuyButtonState.open_for_sharing_state;
                            buyButton.setText(R.string.share_in_gift_button);
                            if (Build.VERSION.SDK_INT >= 21) {
                                buyButton.getBackground().setColorFilter(
                                        ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary),
                                        PorterDuff.Mode.MULTIPLY);
                            }
                        } else {
                            //Hide this button, you have nothing to do with this gift
                            buyButtonState = BuyButtonState.hidden_state;
                            buyButton.setVisibility(View.GONE);
                        }
                    }
                }

            } else {
                //Gift is still available for buying
                buyButtonState = BuyButtonState.open_for_buying_state;
                buyButton.setText(R.string.buy_button);
                if (Build.VERSION.SDK_INT >= 21) {
                    buyButton.getBackground().setColorFilter(
                            ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary),
                            PorterDuff.Mode.MULTIPLY);
                }
            }
        }

        public interface OnItemClickListener {

        }

        enum BuyButtonState {
            buyer_state,
            sharer_state,
            open_for_sharing_state,
            open_for_buying_state,
            hidden_state
        }
    }

}
