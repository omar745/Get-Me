package com.mouris.mario.getme.ui.wishlist_detail_activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.mouris.mario.getme.R;
import com.mouris.mario.getme.ui.adapters.GiftsAdapter;
import com.mouris.mario.getme.utils.Utils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WishlistDetailActivity extends AppCompatActivity
        implements GiftsAdapter.GiftViewHolder.OnItemClickListener{

    public static final String WISHLIST_ID_EXTRA = "wishlist_id_extra";

    @BindView(R.id.owner_name_textView) TextView mUserNameTv;
    @BindView(R.id.picture_imageView) CircularImageView mProfileIv;
    @BindView(R.id.event_type_textView) TextView mEventTypeTv;
    @BindView(R.id.date_textView) TextView mDateTv;
    @BindView(R.id.gifts_recyclerView) RecyclerView mGiftsRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_detail);
        ButterKnife.bind(this);

        setTitle(R.string.wishlist_detail_title);

        //Initialize ViewModel
        WishlistDetailViewModel viewModel = ViewModelProviders.of(this).get(WishlistDetailViewModel.class);

        //Initialize RecyclerView
        mGiftsRv.setLayoutManager(new LinearLayoutManager(this));
        GiftsAdapter giftsAdapter = new GiftsAdapter(null, this);
        mGiftsRv.setAdapter(giftsAdapter);

        //Get the passed wishlistId
        Intent currentIntent = getIntent();
        if (currentIntent.hasExtra(WISHLIST_ID_EXTRA)) {
            String wishlistId = currentIntent.getStringExtra(WISHLIST_ID_EXTRA);

            viewModel.getWishList(wishlistId).observe(this, wishList -> {
                if (wishList != null) {
                    mEventTypeTv.setText(wishList.event_type);
                    mDateTv.setText(Utils.getDateStringFromMillis(wishList.event_time));

                    giftsAdapter.setGiftsList(wishList.gifts_list);

                    viewModel.getUser(wishList.owner).observe(this, user -> {
                        if (user != null) {
                            mUserNameTv.setText(user.display_name);
                            Picasso.get().load(user.profile_picture)
                                    .resize(200,200)
                                    .centerCrop().into(mProfileIv);
                        }
                    });
                }
            });

        } else {
            throw new UnsupportedOperationException("Cannot launch wishlist detail without id");
        }
    }
}
