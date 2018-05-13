package com.mouris.mario.getme.ui.upload_image_dialog;

import android.arch.lifecycle.ViewModel;
import android.net.Uri;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;
import com.mouris.mario.getme.data.actors.EventImage;
import com.mouris.mario.getme.data.repositories.GeneralRepository;

public class UploadImageViewModel extends ViewModel {

    private GeneralRepository mRepository;
    Uri pictureUri;

    public UploadImageViewModel() {
        mRepository = GeneralRepository.getInstance();
    }

    void uploadEventImage(OnCompleteListener<UploadTask.TaskSnapshot> listener) {
        mRepository.uploadEventImage(pictureUri, listener);
    }

    void pushEventImageToFirebase(EventImage eventImage,
                                  DatabaseReference.CompletionListener completionListener) {
        mRepository.pushEventImageToFirebase(eventImage, completionListener);
    }

    String getCurrentUserId() {
        return mRepository.getCurrentUserId();
    }
}
