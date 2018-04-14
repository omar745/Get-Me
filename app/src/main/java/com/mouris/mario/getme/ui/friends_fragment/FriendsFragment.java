package com.mouris.mario.getme.ui.friends_fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mouris.mario.getme.R;
import com.mouris.mario.getme.data.actors.User;
import com.mouris.mario.getme.ui.adapters.UsersAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendsFragment extends Fragment
        implements UsersAdapter.UserViewHolder.OnItemClickListener {

    @BindView(R.id.friends_recyclerView) RecyclerView mFriendsRv;

    private FriendsViewModel mViewModel;

    public FriendsFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, rootView);

        mViewModel = ViewModelProviders.of(this).get(FriendsViewModel.class);

        mFriendsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        UsersAdapter usersAdapter = new UsersAdapter(null, this);
        mFriendsRv.setAdapter(usersAdapter);

        mViewModel.getCurrentUser().observe(this, currentUser -> {
            if (currentUser != null) {
                usersAdapter.setNotificationsList(currentUser.friends_list);
                mViewModel.getFriendsList(currentUser).observe(this, friendsList -> {
                    if (friendsList != null) {
                        usersAdapter.setUsersList(friendsList);
                    }
                });
            }
        });

        return rootView;
    }

    @Override
    public void onItemClicked(User user) {

    }

    @Override
    public void onMuteButtonClicked(String friendId, boolean notificationsAllowed) {
        mViewModel.setNotificationsAllowedForFriend(friendId, notificationsAllowed,
                (databaseError, databaseReference) -> {
            if (databaseError == null) {
                if (!notificationsAllowed) {
                    Snackbar.make(mFriendsRv, R.string.muted_friend_successfully,
                            Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(mFriendsRv, R.string.unmuted_friend_successfully,
                            Snackbar.LENGTH_SHORT).show();
                }
            } else {
                if (!notificationsAllowed) {
                    Snackbar.make(mFriendsRv, R.string.failed_to_mute_friend,
                            Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(mFriendsRv, R.string.failed_to_unmute_friend,
                            Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}
