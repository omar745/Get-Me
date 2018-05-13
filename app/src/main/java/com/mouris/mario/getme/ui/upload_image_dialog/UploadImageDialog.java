package com.mouris.mario.getme.ui.upload_image_dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.mouris.mario.getme.R;
import com.mouris.mario.getme.data.actors.EventImage;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class UploadImageDialog extends DialogFragment {
    public static final String DIALOG_TAG = "upload_image_dialog_tag";
    private static final String LOG_TAG = UploadImageDialog.class.getSimpleName();

    @BindView(R.id.event_image_imageView) ImageView mEventImageIv;
    @BindView(R.id.caption_editText) EditText mCaptionEt;

    private UploadImageViewModel mViewModel;

    public UploadImageDialog() { }

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (getActivity() != null) {
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_upload_image, null);
            ButterKnife.bind(this, view);
            builder.setView(view);
        }

        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            getDialog().getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
            InsetDrawable inset = new InsetDrawable(back, 20);
            getDialog().getWindow().setBackgroundDrawable(inset);
        }

        mViewModel = ViewModelProviders.of(this).get(UploadImageViewModel.class);
    }

    @OnClick(R.id.upload_image_button)
    void onUploadButtonClicked() {
        if (mViewModel.pictureUri != null) {
            mViewModel.uploadEventImage(task -> {
                if (task.isSuccessful() && task.getResult().getDownloadUrl() != null) {
                    String imageUrl = task.getResult().getDownloadUrl().toString();
                    String caption = mCaptionEt.getText().toString();
                    EventImage eventImage =
                            new EventImage(mViewModel.getCurrentUserId(), caption, imageUrl);

                    mViewModel.pushEventImageToFirebase(eventImage, null);
                } else {
                    Snackbar.make(mEventImageIv,
                            R.string.error_uploading_image, Snackbar.LENGTH_SHORT).show();
                }
            });
            dismiss();
        } else {
            Snackbar.make(mEventImageIv,
                    R.string.empty_image_message, Snackbar.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.image_frameLayout)
    void onAddImageButtonClicked() {
        CropImage.activity()
                .start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mViewModel.pictureUri = result.getUri();
                Picasso.get().load(mViewModel.pictureUri)
                        .resize(400,400)
                        .centerCrop().into(mEventImageIv);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e(LOG_TAG, error.getMessage());
            }
        }
    }
}
