package com.mouris.mario.getme.ui.wishlist_editor_activity;

import android.app.DatePickerDialog;
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

import com.mouris.mario.getme.R;
import com.mouris.mario.getme.data.actors.WishList;
import com.mouris.mario.getme.ui.adapters.GiftsAdapter;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WishListEditorActivity extends AppCompatActivity
        implements GiftsAdapter.GiftViewHolder.OnItemClickListener {

    public static final String WISH_LIST_EXTRA = "wish_list_extra_key";

    @BindView(R.id.event_type_spinner) Spinner mEventTypeSpinner;
    @BindView(R.id.date_textView) TextView mDateTv;
    @BindView(R.id.empty_placeholder) LinearLayout mGiftsEmptyLayout;
    @BindView(R.id.gifts_recyclerView) RecyclerView mGiftsRv;

    private GiftsAdapter mGiftsAdapter;
    private WishList mWishList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_editor);
        ButterKnife.bind(this);

        //Set title of activity
        setTitle(R.string.wishlist_editor_title);


        //Initiate gifts recyclerView
        mGiftsRv.setLayoutManager(new LinearLayoutManager(this));
        mGiftsAdapter = new GiftsAdapter(null, this);
        mGiftsRv.setAdapter(mGiftsAdapter);


        //Check if in editing or creation mode
        if (getIntent().hasExtra(WISH_LIST_EXTRA)) {
            //In editing mode

        } else {
            //In creation mode
            mWishList = new WishList();
        }


        //On item selected listener for spinner (Called when activity is created)
        mEventTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mWishList.event_type = mEventTypeSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick(R.id.add_gift_button)
    void addGiftButtonClicked() {
        
    }

    @OnClick(R.id.choose_date_button)
    void chooseDateButtonClicked() {

        Calendar currentTime = Calendar.getInstance();

        // Launch Time Picker Dialog
        new DatePickerDialog(this, (datePicker, year, month, dayOfMonth) -> {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);

            mWishList.event_time = calendar.getTimeInMillis();

            String dateString = "("+dayOfMonth+"/"+(month+1)+"/"+year+")";
            mDateTv.setText(dateString);
            Snackbar.make(mGiftsRv,
                    "Event time is set to " + dateString, Snackbar.LENGTH_LONG).show();
        }, currentTime.get(Calendar.YEAR),
                currentTime.get(Calendar.MONTH),
                currentTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wishlist_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
