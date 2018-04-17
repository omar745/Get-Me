package com.mouris.mario.getme.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mouris.mario.getme.R;
import com.mouris.mario.getme.data.actors.Gift;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.GiftViewHolder> {

    private List<Gift> mGiftsList;
    private GiftViewHolder.OnItemClickListener mListener;

    public GiftsAdapter(List<Gift> giftsList, GiftViewHolder.OnItemClickListener listener) {
        mGiftsList = giftsList;
        mListener = listener;
    }

    public void setGiftsList(List<Gift> giftsList) {
        mGiftsList = giftsList;
        notifyDataSetChanged();
    }

    public void setGiftsList(HashMap<String, Gift> gifts_list) {
        mGiftsList = (List<Gift>) gifts_list.values();
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

        public GiftViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public interface OnItemClickListener {

        }
    }

}
