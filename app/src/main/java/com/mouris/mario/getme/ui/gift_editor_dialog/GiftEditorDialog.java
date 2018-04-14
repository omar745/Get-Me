package com.mouris.mario.getme.ui.gift_editor_dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.mouris.mario.getme.R;

import butterknife.ButterKnife;

//TODO: Fix glitch when keyboard shows (Wrap content scrollview)
public class GiftEditorDialog extends DialogFragment {
    public static final String DIALOG_TAG = "gift_editor_dialog_tag";

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
    }
}
