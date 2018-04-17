package com.mouris.mario.getme.data.repositories;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.google.firebase.database.DatabaseReference;
import com.mouris.mario.getme.data.actors.Gift;
import com.mouris.mario.getme.data.actors.User;
import com.mouris.mario.getme.data.actors.Wishlist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GeneralRepository {
    private static final String LOG_TAG = GeneralRepository.class.getSimpleName();

    private UsersRepository mUsersRepository;
    private WishListsRepository mWishListsRepository;

    private static GeneralRepository sInstance;

    public static GeneralRepository getInstance() {
        if (sInstance == null) {
            sInstance = new GeneralRepository();
        }
        return sInstance;
    }

    private GeneralRepository() {
        mUsersRepository = UsersRepository.getInstance();
        mWishListsRepository = WishListsRepository.getInstance();
    }


    //----------------------------------------------------------------------------------------------
    //Users repository code
    public LiveData<List<User>> getUsers() {
        return mUsersRepository.getUsersLiveData();
    }

    public LiveData<List<User>> getUsersByIdsList(Set<String> listOfIds) {
        return mUsersRepository.getUsersByIdsList(listOfIds);
    }

    public LiveData<User> getUserById(String userId) {
        return mUsersRepository.getUserLiveDataById(userId);
    }

    public void deleteUser(String userId, DatabaseReference.CompletionListener completionListener) {
        mUsersRepository.deleteUserFromFirebase(userId, completionListener);
    }

    public void createNewUser(String userId, User user,
                              DatabaseReference.CompletionListener completionListener) {
        mUsersRepository.createNewUser(userId, user, completionListener);
    }

    public void pushFriendsList(String userId, HashMap<String, Boolean> friendsList,
                                DatabaseReference.CompletionListener completionListener) {
        mUsersRepository.pushFriendsList(userId, friendsList, completionListener);
    }

    public void setNotificationsAllowedForFriend(String friendId, boolean notificationsAllowed,
                                                 DatabaseReference.CompletionListener
                                                         completionListener) {
        mUsersRepository.setNotificationsAllowedForFriend(getCurrentUserId(), friendId,
                notificationsAllowed, completionListener);
    }


    //----------------------------------------------------------------------------------------------
    //WishLists repository Code
    public LiveData<List<Wishlist>> getFriendsWishLists(Set<String> friendsIds) {
        return mWishListsRepository.getFriendsWishListsLiveData(friendsIds);
    }

    public LiveData<List<Wishlist>> getMyWishListsLiveData(String currentUserId) {
        return mWishListsRepository.getMyWishListsLiveData(currentUserId);
    }

    public LiveData<Wishlist> getWishListById(String wishListId) {
        return mWishListsRepository.getWishListLiveDataById(wishListId);
    }

    public void deleteWishListFromFirebase(String wishListId,
                                           DatabaseReference.CompletionListener completionListener) {
        mWishListsRepository.deleteWishListFromFirebase(wishListId, completionListener);
    }

    public void pushWishListToFirebase(Wishlist wishlist, List<Gift> listOfGifts,
                                       DatabaseReference.CompletionListener completionListener) {
        String wishListId =
                mWishListsRepository.pushWishListToFirebase(wishlist, completionListener);
        if (listOfGifts != null) {
            for (Gift gift : listOfGifts) {
                mWishListsRepository.addGiftToWishList(wishListId, gift, null);
            }
        }
    }


    //----------------------------------------------------------------------------------------------
    //Facebook Code
    public void updateFacebookFriends() {

        if (Profile.getCurrentProfile() == null) {
            //TODO: Handle error
            return;
        }

        AccessToken token = AccessToken.getCurrentAccessToken();
        String facebookUserId = Profile.getCurrentProfile().getId();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                token,
                "/"+ facebookUserId +"/friends", response -> {

                    HashMap<String, Boolean> friendsList = new HashMap<>();

                    if (response.getError() != null) {
                        //TODO: Handle error gracefully
                        Log.e(LOG_TAG, response.getError().getErrorMessage());
                        return;
                    }

                    try {
                        JSONArray listOfFriendsJson = response.getJSONObject()
                                .getJSONArray("data");
                        Log.i("TEST", "Found " + listOfFriendsJson.length() + " friends");

                        for (int i=0 ; i < listOfFriendsJson.length() ; i++) {
                            JSONObject friendJson = listOfFriendsJson.getJSONObject(i);
                            friendsList.put(friendJson.getString("id"), true);
                        }

                        pushFriendsList(getCurrentUserId(), friendsList,
                                (databaseError, databaseReference) -> {
                            if (databaseError != null) {
                                Log.e(LOG_TAG, "There was an error pushing friends list",
                                        databaseError.toException());
                            } else {
                                Log.i(LOG_TAG, "Pushed friends list successfully");
                            }
                        });
                    } catch (JSONException e) {
                        Log.e(LOG_TAG, "Error parsing response of GraphRequest", e);
                        //TODO: Handle error gracefully
                    }

                });

        request.executeAsync();
    }


    //----------------------------------------------------------------------------------------------
    //Authorization Code
    public String getCurrentUserId() {
        Profile currentProfile = Profile.getCurrentProfile();
        if (currentProfile != null) {
            return currentProfile.getId();
        }
        return null;
    }

}
