package com.mouris.mario.getme.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.mouris.mario.getme.R;
import com.mouris.mario.getme.data.actors.User;
import com.mouris.mario.getme.data.actors.Wishlist;
import com.mouris.mario.getme.utils.ListUtils;
import com.mouris.mario.getme.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WishListsAdapter extends RecyclerView.Adapter<WishListsAdapter.WishListViewHolder> {

    private List<Wishlist> mWishlists;
    private List<User> mUsersList;
    private boolean mShowGiftsRemaining;

    private WishListViewHolder.OnItemClickListener mListener;

    public WishListsAdapter(List<Wishlist> wishlists,
                            List<User> usersList,
                            boolean showGiftsRemaining,
                            WishListViewHolder.OnItemClickListener listener) {
        mWishlists = wishlists;
        mUsersList = usersList;
        mShowGiftsRemaining = showGiftsRemaining;
        mListener = listener;
    }

    public void setWishLists(List<Wishlist> wishlists) {
        mWishlists = wishlists;
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
        Wishlist wishlist = mWishlists.get(position);
        User user = ListUtils.searchListById(mUsersList, wishlist.owner);

        if (user != null) {
            wishListVh.userNameTv.setText(user.display_name);
            Picasso.get().load(user.profile_picture)
                    .placeholder(R.drawable.image_placeholder)
                    .resize(200,200)
                    .centerCrop()
                    .into(wishListVh.userPictureIv);
        }

        Context context = wishListVh.itemView.getContext();

        if (mShowGiftsRemaining) {
            wishListVh.remainingGiftsTv.setText(
                    context.getString(R.string.remaining_gifts,
                            String.valueOf(
                                    wishlist.gifts_list.size() - wishlist.getRemainingGiftsCount()),
                            String.valueOf(wishlist.gifts_list.size())));
        } else {
            wishListVh.remainingGiftsTv.setText(
                    context.getResources()
                            .getQuantityString(R.plurals.gifts_count, wishlist.gifts_list.size(),
                                    String.valueOf(wishlist.gifts_list.size())));
        }

        wishListVh.wishListStatementTv.setText(
                context.getString(R.string.wishlist_statement,
                        wishlist.event_type,
                        Utils.getDateStringFromMillis(wishlist.event_time)));

        wishListVh.itemView.setOnClickListener(view ->
                mListener.onWishlistClickListener(wishlist, wishListVh.userPictureIv));
    }

    @Override
    public int getItemCount() {
        if (mWishlists == null) return 0;
        return mWishlists.size();
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

        public interface OnItemClickListener {
            void onWishlistClickListener(Wishlist wishlist, ImageView profilePictureIv);
        }
    }

}
