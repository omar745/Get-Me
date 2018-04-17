package com.mouris.mario.getme.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.mouris.mario.getme.R;
import com.mouris.mario.getme.data.actors.User;
import com.mouris.mario.getme.data.actors.WishList;
import com.mouris.mario.getme.utils.ListUtils;
import com.mouris.mario.getme.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WishListsAdapter extends RecyclerView.Adapter<WishListsAdapter.WishListViewHolder> {

    private List<WishList> mWishLists;
    private List<User> mUsersList;
    private boolean mShowGiftsRemaining;

    public WishListsAdapter(List<WishList> wishLists,
                            List<User> usersList,
                            boolean showGiftsRemaining) {
        mWishLists = wishLists;
        mUsersList = usersList;
        mShowGiftsRemaining = showGiftsRemaining;
    }

    public void setWishLists(List<WishList> wishLists) {
        mWishLists = wishLists;
        notifyDataSetChanged();
    }

    public void setUsersList(List<User> usersList) {
        mUsersList = usersList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.item_wishlist, parent, false);

        return new WishListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListViewHolder wishListVh, int position) {
        WishList wishList = mWishLists.get(position);
        User user = ListUtils.searchListById(mUsersList, wishList.owner);

        if (user != null) {
            wishListVh.userNameTv.setText(user.display_name);
            Picasso.get().load(user.profile_picture)
                    .resize(200,200)
                    .centerCrop()
                    .into(wishListVh.userPictureIv);
        }

        Context context = wishListVh.itemView.getContext();

        if (mShowGiftsRemaining) {
            wishListVh.remainingGiftsTv.setText(
                    context.getString(R.string.remaining_gifts,
                            String.valueOf(wishList.gifts_list.size()),
                            String.valueOf(wishList.gifts_list.size())));
        } else {
            wishListVh.remainingGiftsTv.setText(
                    context.getResources()
                            .getQuantityString(R.plurals.gifts_count, wishList.gifts_list.size(),
                                    String.valueOf(wishList.gifts_list.size())));
        }

        wishListVh.wishListStatementTv.setText(
                context.getString(R.string.wishlist_statement,
                        wishList.event_type,
                        Utils.getDateStringFromMillis(wishList.event_time)));
    }

    @Override
    public int getItemCount() {
        if (mWishLists == null) return 0;
        return mWishLists.size();
    }

    //----------------------------------------------------------------------------------------------
    public static class WishListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_name_textView) TextView userNameTv;
        @BindView(R.id.picture_imageView) CircularImageView userPictureIv;
        @BindView(R.id.remaining_gifts_textView) TextView remainingGiftsTv;
        @BindView(R.id.wishlist_statement_textView) TextView wishListStatementTv;

        WishListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
