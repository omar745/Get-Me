package com.mouris.mario.getme.ui;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.mouris.mario.getme.R;
import com.mouris.mario.getme.data.repositories.GeneralRepository;
import com.mouris.mario.getme.ui.adapters.MainFragmentsPagerAdapter;
import com.mouris.mario.getme.ui.friends_fragment.FriendsFragment;
import com.mouris.mario.getme.ui.home_fragment.HomeFragment;
import com.mouris.mario.getme.ui.my_wishlists_fragment.MyWishListsFragment;
import com.mouris.mario.getme.ui.welcome_screens.FacebookLoginActivity;
import com.mouris.mario.getme.ui.wishlist_editor_activity.WishListEditorActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.viewpager) ViewPager mViewPager;
    @BindView(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Initialize the bottom navigation
        setupBottomNavigation();

        //Update facebook friends list
        GeneralRepository generalRepository = GeneralRepository.getInstance();
        generalRepository.updateFacebookFriends();
    }

    private void setupBottomNavigation() {
        MainFragmentsPagerAdapter adapter =
                new MainFragmentsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new MyWishListsFragment());
        adapter.addFragment(new FriendsFragment());

        mViewPager.setAdapter(adapter);

        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.action_birthdays:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.action_friends:
                    mViewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        setTitle(R.string.home_title);
                        break;
                    case 1:
                        setTitle(R.string.my_wishlists_title);
                        break;
                    case 2:
                        setTitle(R.string.friends_title);
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBottomNavigationView.setSelectedItemId(R.id.action_home);
                        break;
                    case 1:
                        mBottomNavigationView.setSelectedItemId(R.id.action_birthdays);
                        break;
                    case 2:
                        mBottomNavigationView.setSelectedItemId(R.id.action_friends);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            Intent facebookLoginIntent = new Intent(this, FacebookLoginActivity.class);
            facebookLoginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(facebookLoginIntent);
            finish();

            return true;
        } else if (item.getItemId() == R.id.action_add_wishlist) {
            Intent addWishListIntent = new Intent(this, WishListEditorActivity.class);
            startActivity(addWishListIntent);
        } else if (item.getItemId() == R.id.action_upload_image) {

        }
        
        return super.onOptionsItemSelected(item);
    }
}
