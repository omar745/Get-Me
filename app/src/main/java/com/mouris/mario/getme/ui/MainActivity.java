package com.mouris.mario.getme.ui;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mouris.mario.getme.R;
import com.mouris.mario.getme.ui.adapters.MainFragmentsPagerAdapter;
import com.mouris.mario.getme.ui.friends_fragment.FriendsFragment;
import com.mouris.mario.getme.ui.home_fragment.HomeFragment;
import com.mouris.mario.getme.ui.birthdays_fragment.BirthdaysFragment;

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
    }

    private void setupBottomNavigation() {
        MainFragmentsPagerAdapter adapter =
                new MainFragmentsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new BirthdaysFragment());
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
                        setTitle(R.string.birthdays_title);
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
}
