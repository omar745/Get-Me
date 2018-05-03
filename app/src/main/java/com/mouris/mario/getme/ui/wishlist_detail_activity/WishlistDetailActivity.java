package com.mouris.mario.getme.ui.wishlist_detail_activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.mouris.mario.getme.R;
import com.mouris.mario.getme.ui.adapters.GiftsAdapter;
import com.mouris.mario.getme.ui.buy_gift_dialog.BuyGiftDialog;
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

    private WishlistDetailViewModel mViewModel;
    private String mWishlistId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_detail);
        ButterKnife.bind(this);

        setTitle(R.string.wishlist_detail_title);

        //Set enter and exit animation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide explode = new Slide();
            explode.excludeTarget(mProfileIv, true);
            explode.excludeTarget(android.R.id.navigationBarBackground, true);

            getWindow().setEnterTransition(explode);
            getWindow().setExitTransition(explode);
        }

        //Initialize ViewModel
        mViewModel = ViewModelProviders.of(this)
                .get(WishlistDetailViewModel.class);

        //Initialize RecyclerView
        mGiftsRv.setLayoutManager(new LinearLayoutManager(this));
        GiftsAdapter giftsAdapter = new GiftsAdapter(null, mViewModel.getCurrentUserId(),this);
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

                    mViewModel.getUser(wishList.owner).observe(this, user -> {
                        if (user != null) {
                            mUserNameTv.setText(user.display_name);
                            Picasso.get().load(user.profile_picture)
                                    .placeholder(R.drawable.image_placeholder)
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

    //Gift management buttons
    @Override
    public void onBuyButtonClicked(String giftId) {
        DialogFragment buyGiftDialog = BuyGiftDialog.newInstance(mWishlistId, giftId);
        buyGiftDialog.show(getSupportFragmentManager(), BuyGiftDialog.DIALOG_TAG);
    }

    @Override
    public void onShareButtonClicked(String giftId) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.sharing_confirmation_title)
                .setMessage(R.string.sharing_confirmation_message)
                .setPositiveButton(R.string.share_positive_button, (dialog, which) ->

                        mViewModel.shareInGift(mWishlistId, giftId,
                        ((databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        Snackbar.make(mGiftsRv,
                                R.string.shared_in_gift_successfully,
                                Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(mGiftsRv,
                                R.string.failed_to_share_in_gift,
                                Snackbar.LENGTH_LONG).show();
                    }
                })))
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    public void onBuyCancelButtonClicked(String giftId) {
        mViewModel.cancelBuyingGift(mWishlistId, giftId, ((databaseError, databaseReference) -> {
            if (databaseError == null) {
                Snackbar.make(mGiftsRv,
                        R.string.canceled_buying_gift_successfully,
                        Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(mGiftsRv,
                        R.string.error_canceling,
                        Snackbar.LENGTH_LONG).show();
            }
        }));
    }

    @Override
    public void onShareCancelButtonClicked(String giftId) {
        mViewModel.cancelSharingGift(mWishlistId, giftId, ((databaseError, databaseReference) -> {
            if (databaseError == null) {
                Snackbar.make(mGiftsRv,
                        R.string.canceled_sharing_gift_successfully,
                        Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(mGiftsRv,
                        R.string.error_canceling,
                        Snackbar.LENGTH_LONG).show();
            }
        }));
    }
}
