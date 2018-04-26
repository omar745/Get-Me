package com.mouris.mario.getme.ui.buy_gift_dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mouris.mario.getme.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuyGiftDialog extends DialogFragment {
    public static final String DIALOG_TAG = "buy_gift_dialog_tag";
    private static final String GIFT_ID_ARG = "gift_id_arg";

    @BindView(R.id.sharing_checkBox) CheckBox mSharingCb;
    @BindView(R.id.sharing_layout) ConstraintLayout mSharingLayout;
    @BindView(R.id.sharers_count_seekBar) SeekBar mSharersCountSb;
    @BindView(R.id.number_of_sharers_textView) TextView mNumOfSharersTv;
    @BindView(R.id.confirm_button) Button mConfirmButton;

    private BuyGiftViewModel mViewModel;
    private String mGiftId;

    public BuyGiftDialog() {
    }

    public static BuyGiftDialog newInstance(String giftId) {
        BuyGiftDialog buyDialog = new BuyGiftDialog();

        Bundle args = new Bundle();
        args.putString(GIFT_ID_ARG, giftId);
        buyDialog.setArguments(args);

        return buyDialog;
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
            View view = inflater.inflate(R.layout.dialog_buy_gift, null);
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
            ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
            InsetDrawable inset = new InsetDrawable(back, 20);
            getDialog().getWindow().setBackgroundDrawable(inset);
        }

        if (getArguments() != null) {
            mGiftId = getArguments().getString(GIFT_ID_ARG);
        }

        //Initialize ViewModel
        mViewModel = ViewModelProviders.of(this).get(BuyGiftViewModel.class);

        //Restore data after rotation
        mNumOfSharersTv.setText(getString(R.string.number_of_sharers_text,
                String.valueOf(mViewModel.numberOfSharers)));
        if (mViewModel.sharingIsEnabled) {
            mSharingLayout.setVisibility(View.VISIBLE);
        } else {
            mSharingLayout.setVisibility(View.GONE);
        }

        //Make the confirm button green
        if (Build.VERSION.SDK_INT >= 21 && getContext() != null) {
            mConfirmButton.getBackground().setColorFilter(
                    ContextCompat.getColor(getContext(), R.color.colorPrimary),
                    PorterDuff.Mode.MULTIPLY);
        }


        //OnCheck listener for sharing checkbox
        mSharingCb.setOnCheckedChangeListener((compoundButton, sharingEnabled) -> {
            mViewModel.sharingIsEnabled = sharingEnabled;
            if (sharingEnabled) {
                mSharingLayout.setVisibility(View.VISIBLE);
            } else {
                mSharingLayout.setVisibility(View.GONE);
            }
        });

        mSharersCountSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int numberOfSharers, boolean b) {
                mViewModel.numberOfSharers = numberOfSharers;
                mNumOfSharersTv.setText(getString(R.string.number_of_sharers_text,
                        String.valueOf(numberOfSharers)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @OnClick(R.id.confirm_button)
    void onConfirmButtonClicked() {

    }

}
