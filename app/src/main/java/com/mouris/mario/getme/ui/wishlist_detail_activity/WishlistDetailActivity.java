package com.mouris.mario.getme.ui.wishlist_detail_activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.mouris.mario.getme.R;
import com.mouris.mario.getme.ui.adapters.GiftsAdapter;
import com.mouris.mario.getme.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WishlistDetailActivity extends AppCompatActivity
        implements GiftsAdapter.GiftViewHolder.OnItemClickListener{

    public static final String WISHLIST_ID_EXTRA = "wishlist_id_extra";

    @BindView(R.id.event_type_textView) TextView mEventTypeTv;
    @BindView(R.id.date_textView) TextView mDateTv;
    @BindView(R.id.gifts_recyclerView) RecyclerView mGiftsRv;

    private String mWishlistId;
    private WishlistsDetailViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_detail);
        ButterKnife.bind(this);

        //Initialize ViewModel
        mViewModel = ViewModelProviders.of(this).get(WishlistsDetailViewModel.class);

        //Initialize RecyclerView
        mGiftsRv.setLayoutManager(new LinearLayoutManager(this));
        GiftsAdapter giftsAdapter = new GiftsAdapter(null, this);
        mGiftsRv.setAdapter(giftsAdapter);

        //Get the passed wishlistId
        Intent currentIntent = getIntent();
        if (currentIntent.hasExtra(WISHLIST_ID_EXTRA)) {
            mWishlistId = currentIntent.getStringExtra(WISHLIST_ID_EXTRA);

            mViewModel.getWishList(mWishlistId).observe(this, wishList -> {
                if (wishList != null) {
                    mEventTypeTv.setText(wishList.event_type);
                    mDateTv.setText(Utils.getDateStringFromMillis(wishList.event_time));

                    giftsAdapter.setGiftsList(wishList.gifts_list);
                }
            });

        } else {
            throw new UnsupportedOperationException("Cannot launch wishlist detail without id");
        }
    }
}
