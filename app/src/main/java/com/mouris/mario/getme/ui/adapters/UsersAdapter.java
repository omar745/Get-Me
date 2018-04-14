package com.mouris.mario.getme.ui.adapters;

import android.content.Context;
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

import com.mikhaellopez.circularimageview.CircularImageView;
import com.mouris.mario.getme.R;
import com.mouris.mario.getme.data.actors.User;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<User> mUsersList;
    private HashMap<String, Boolean> mNotificationsList;
    private UserViewHolder.OnItemClickListener mItemListener;

    public UsersAdapter(List<User> usersList, UserViewHolder.OnItemClickListener itemListener) {
        mUsersList = usersList;
        mItemListener = itemListener;
    }

    public void setUsersList(List<User> usersList) {
        mUsersList = usersList;
        notifyDataSetChanged();
    }

    public void setNotificationsList(HashMap<String, Boolean> notificationsList) {
        mNotificationsList = notificationsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.item_user, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userVh, int position) {
        User user = mUsersList.get(position);
        boolean notificationsAllowed = true;

        if (mNotificationsList != null && mNotificationsList.containsKey(user.id)) {
            notificationsAllowed = mNotificationsList.get(user.id);
        }

        userVh.setUpMuteButton(notificationsAllowed);
        userVh.nameTv.setText(user.display_name);
        Picasso.get().load(user.profile_picture)
                .resize(200, 200)
                .centerCrop()
                .placeholder(R.drawable.image_placeholder).into(userVh.pictureIv);
        userVh.itemView.setOnClickListener(view -> mItemListener.onItemClicked(user));

        boolean finalNotificationsAllowed = notificationsAllowed;
        userVh.muteButton.setOnClickListener(view ->
                mItemListener.onMuteButtonClicked(user.id, !finalNotificationsAllowed));
    }

    @Override
    public int getItemCount() {
        if (mUsersList == null) return 0;
        return mUsersList.size();
    }


    //----------------------------------------------------------------------------------------------

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.picture_imageView) CircularImageView pictureIv;
        @BindView(R.id.name_textView) TextView nameTv;
        @BindView(R.id.muteButton) Button muteButton;

        private Context mContext;


        UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mContext = itemView.getContext();
        }

        void setUpMuteButton(boolean notificationsAllowed) {
            if (notificationsAllowed) {
                muteButton.setText(R.string.mute_button);
                if (Build.VERSION.SDK_INT >= 21) {
                    muteButton.getBackground().setColorFilter(
                            ContextCompat.getColor(mContext, R.color.colorAccent),
                            PorterDuff.Mode.MULTIPLY);
                }
            } else {
                muteButton.setText(R.string.unmute_button);
                if (Build.VERSION.SDK_INT >= 21) {
                    muteButton.getBackground().setColorFilter(
                            ContextCompat.getColor(mContext, R.color.colorPrimary),
                            PorterDuff.Mode.MULTIPLY);
                }
            }
        }

        public interface OnItemClickListener {
            void onItemClicked(User user);
            void onMuteButtonClicked(String friendId, boolean notificationsAllowed);
        }
    }
}
