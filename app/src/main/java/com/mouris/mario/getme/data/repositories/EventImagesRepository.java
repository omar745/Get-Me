package com.mouris.mario.getme.data.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mouris.mario.getme.data.FirebaseQueryLiveData;
import com.mouris.mario.getme.data.actors.EventImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventImagesRepository {
    private DatabaseReference mEventImagesDbReference;

    //A hash map to keep track of existing live data objects not to create new ones when unnecessary
    private Map<String, LiveData<EventImage>> mActiveLiveDataObjects = new HashMap<>();

    //The liveData object that holds all the friends images
    private LiveData<List<EventImage>> mFriendImagesLiveData;
    private Set<String> mFriendsIds;


    private static EventImagesRepository sInstance;

    static EventImagesRepository getInstance() {
        if (sInstance == null) {
            sInstance = new EventImagesRepository();
        }
        return sInstance;
    }

    private EventImagesRepository() {
        //Initiate the EventImage database reference
        mEventImagesDbReference = FirebaseDatabase.getInstance()
                .getReference().child(EventImage.ROOT_REF_NAME);
        mEventImagesDbReference.keepSynced(true);
    }


    //A method to retrieve a LiveData containing all the Images for my friends.
    // (It creates a new object if null)
    LiveData<List<EventImage>> getFriendsImagesLiveData(Set<String> friendsIds) {
        if (mFriendImagesLiveData == null || !friendsIds.equals(mFriendsIds)) {
            mFriendsIds = friendsIds;
            //Create a liveData that returns a DataSnapshot
            FirebaseQueryLiveData dataSnapshotsLiveData =
                    new FirebaseQueryLiveData(mEventImagesDbReference);
            mFriendImagesLiveData = Transformations.map(dataSnapshotsLiveData, dataSnapshots -> {
                if (dataSnapshots != null) {
                    List<EventImage> eventImages = new ArrayList<>();
                    for (DataSnapshot imageSnapshot: dataSnapshots.getChildren()) {
                        EventImage image = imageSnapshot.getValue(EventImage.class);
                        if (image != null && friendsIds.contains(image.owner)) {
                            image.id = imageSnapshot.getKey();
                            eventImages.add(image);
                        }

                    }
                    return eventImages;
                } else {
                    return null;
                }
            });
        }

        return mFriendImagesLiveData;
    }

    void pushEventImageToFirebase(EventImage eventImage,
                                  DatabaseReference.CompletionListener completionListener) {
        if (eventImage.id == null) {
            eventImage.id = mEventImagesDbReference.push().getKey();
        }
        mEventImagesDbReference.child(eventImage.id).setValue(eventImage, completionListener);
    }
}
