package com.mouris.mario.getme.ui.gift_editor_dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.mouris.mario.getme.R;
import com.mouris.mario.getme.data.actors.Gift;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//TODO: Fix glitch when keyboard shows (Wrap content scrollview)
public class GiftEditorDialog extends DialogFragment {
    public static final String DIALOG_TAG = "gift_editor_dialog_tag";

    @BindView(R.id.gift_name_editText) EditText mGiftNameEt;
    @BindView(R.id.description_editText) EditText mDescriptionEt;
    @BindView(R.id.brand_editText) EditText mBrandEt;
    @BindView(R.id.web_link_editText) EditText mWebLinkEt;

    private GiftEditorViewModel mViewModel;
    private OnGiftSave mGiftSaveListener;

    public GiftEditorDialog() { }

    public void setOnGiftSaveListener(OnGiftSave listener) {
        mGiftSaveListener = listener;
    }

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (getActivity() != null) {
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_gift_editor, null);
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

        mViewModel = ViewModelProviders.of(this).get(GiftEditorViewModel.class);

        if (mViewModel.gift == null) {
            mViewModel.gift = new Gift();
        } else {
            mGiftNameEt.setText(mViewModel.gift.gift_name);
            mBrandEt.setText(mViewModel.gift.brand);
            mDescriptionEt.setText(mViewModel.gift.description);
            mWebLinkEt.setText(mViewModel.gift.web_link);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        mViewModel.gift.gift_name = getStringOrNull(mGiftNameEt);
        mViewModel.gift.brand = getStringOrNull(mBrandEt);
        mViewModel.gift.description = getStringOrNull(mDescriptionEt);
        mViewModel.gift.web_link = getStringOrNull(mWebLinkEt);
    }

    @OnClick(R.id.save_button)
    void saveButtonClicked() {
        if (mGiftNameEt.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), R.string.empty_gift_name_toast, Toast.LENGTH_LONG).show();
        } else {
            mViewModel.gift.gift_name = getStringOrNull(mGiftNameEt);
            mViewModel.gift.brand = getStringOrNull(mBrandEt);
            mViewModel.gift.description = getStringOrNull(mDescriptionEt);
            mViewModel.gift.web_link = getStringOrNull(mWebLinkEt);

            mGiftSaveListener.onGiftSaved(mViewModel.gift);

            Toast.makeText(getContext(), R.string.gift_saved_successfully, Toast.LENGTH_LONG).show();

            dismiss();
        }
    }

    private String getStringOrNull(EditText editText) {
        if (editText.getText().toString().isEmpty() || editText.getText() == null) {
            return null;
        } else {
            return editText.getText().toString();
        }
    }

    public interface OnGiftSave {
        void onGiftSaved(Gift gift);
    }
}
