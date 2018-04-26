package com.mouris.mario.getme.ui.wishlist_editor_activity;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mouris.mario.getme.R;
import com.mouris.mario.getme.data.actors.Gift;
import com.mouris.mario.getme.data.actors.Wishlist;
import com.mouris.mario.getme.ui.adapters.GiftsAdapter;
import com.mouris.mario.getme.ui.gift_editor_dialog.GiftEditorDialog;
import com.mouris.mario.getme.utils.Utils;

import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WishListEditorActivity extends AppCompatActivity
        implements GiftEditorDialog.OnGiftSave {

    public static final String WISH_LIST_ID_EXTRA = "wish_list_extra_key";

    @BindView(R.id.event_type_spinner) Spinner mEventTypeSpinner;
    @BindView(R.id.date_textView) TextView mDateTv;
    @BindView(R.id.empty_placeholder) LinearLayout mGiftsEmptyLayout;
    @BindView(R.id.gifts_recyclerView) RecyclerView mGiftsRv;

    private GiftsAdapter mGiftsAdapter;
    private WishListEditorViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_editor);
        ButterKnife.bind(this);

        //Set title of activity
        setTitle(R.string.wishlist_editor_title);

        //Initiate ViewModel
        mViewModel = ViewModelProviders.of(this).get(WishListEditorViewModel.class);


        //Initiate gifts recyclerView
        mGiftsRv.setLayoutManager(new LinearLayoutManager(this));
        mGiftsAdapter = new GiftsAdapter(null, null, null);
        mGiftsRv.setAdapter(mGiftsAdapter);

        //Check if in editing or creation mode (Check if WishList is null first)
        if (mViewModel.wishlist == null) {
            if (getIntent().hasExtra(WISH_LIST_ID_EXTRA)) {
                //In editing mode
                mViewModel.getWishlist(getIntent()
                        .getStringExtra(WISH_LIST_ID_EXTRA)).observe(this, wishlist -> {

                            if (wishlist != null) {
                                mViewModel.wishlist = wishlist;
//                                mViewModel.giftsList = ListUtils.getGiftsListFromMap(wishlist.gifts_list);
                                mGiftsRv.smoothScrollToPosition(0);

                                //Set the values
                                mDateTv.setText(Utils.getDateStringFromMillis(wishlist.event_time));
                                mGiftsAdapter.setGiftsList(wishlist.gifts_list);
                                setEmptyPlaceholderState();

                                //Set the right index for the spinner
                                String[] typesArray =
                                        getResources().getStringArray(R.array.event_types);
                                mEventTypeSpinner.setSelection(
                                        Arrays.asList(typesArray).indexOf(wishlist.event_type));
                            }
                });

            } else {
                //In creation mode
                mViewModel.wishlist = new Wishlist();
            }
        } else {
            //Set previous values
            mGiftsAdapter.setGiftsList(mViewModel.wishlist.gifts_list);
        }

        //Set date string if not null
        if (mViewModel.eventDate != null) {
            mDateTv.setText(mViewModel.eventDate);
        }

        //Listen for number of items in adapter to show/hide emptyPlaceholder
        setEmptyPlaceholderState();

        //On item selected listener for spinner (Called when activity is created)
        mEventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mViewModel.wishlist.event_type = mEventTypeSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setEmptyPlaceholderState(){
        if (mGiftsAdapter.getItemCount() == 0) {
            mGiftsEmptyLayout.setVisibility(View.VISIBLE);
        } else {
            mGiftsEmptyLayout.setVisibility(View.GONE);
        }
    }

    private void saveWishList() {
        if (mViewModel.wishlist.event_type == null ||
                mViewModel.wishlist.event_time == 0L) {
            Toast.makeText(this, R.string.empty_event_fields_toast, Toast.LENGTH_LONG).show();
        } else if (mViewModel.wishlist.gifts_list.size() == 0) {
            Toast.makeText(this, R.string.empty_gifts_toast, Toast.LENGTH_LONG).show();
        } else {
            mViewModel.saveWishList((databaseError, databaseReference) -> {
                if (databaseError == null) {
                    Toast.makeText(this,
                            R.string.wishlist_saved_successfully, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this,
                            R.string.failed_to_save_wishlist, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @OnClick(R.id.add_gift_button)
    void addGiftButtonClicked() {
        GiftEditorDialog giftDialog = new GiftEditorDialog();
        giftDialog.setOnGiftSaveListener(this);
        giftDialog.show(getSupportFragmentManager(), GiftEditorDialog.DIALOG_TAG);
    }

    @OnClick(R.id.choose_date_button)
    void chooseDateButtonClicked() {

        Calendar pickerTime = Calendar.getInstance();
        if (mViewModel.wishlist.event_time != 0L) {
            pickerTime.setTimeInMillis(mViewModel.wishlist.event_time);
        }


        // Launch Time Picker Dialog
        new DatePickerDialog(this, (datePicker, year, month, dayOfMonth) -> {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);

            String dateString = dayOfMonth+"/"+(month+1)+"/"+year;
            mDateTv.setText(dateString);

            mViewModel.wishlist.event_time = calendar.getTimeInMillis();
            mViewModel.eventDate = dateString;

            Snackbar.make(mGiftsRv,
                    "Event time is set to " + dateString, Snackbar.LENGTH_LONG).show();
        }, pickerTime.get(Calendar.YEAR),
                pickerTime.get(Calendar.MONTH),
                pickerTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onGiftSaved(Gift gift) {
        mViewModel.addNewGiftToWishlist(gift);
        mGiftsAdapter.setGiftsList(mViewModel.wishlist.gifts_list);
        setEmptyPlaceholderState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wishlist_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save_wishlist) {
            saveWishList();
        }
        return super.onOptionsItemSelected(item);
    }
}
