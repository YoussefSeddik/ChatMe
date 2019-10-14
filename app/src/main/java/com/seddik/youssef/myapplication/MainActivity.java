package com.seddik.youssef.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;
import com.seddik.youssef.myapplication.fragments.ChatFragment;
import com.seddik.youssef.myapplication.fragments.ProfileFragment;
import com.seddik.youssef.myapplication.fragments.UsersFragment;
import com.seddik.youssef.myapplication.model.User;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    MyApplication myApplication;
    CircleImageView profile_image;
    TextView username;
    Button button_logout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    BottomNavigationView mainBottomNav;
    MenuItem prevItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myApplication = MyApplication.getInstance();
        OneSignal.startInit(myApplication.getApplicationContext()).init();
        OneSignal.setSubscription(true);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("notificationKey").setValue(userId);
            }
        });
        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification);


//        final Task<InstanceIdResult> tkn = FirebaseInstanceId.getInstance().getInstanceId();
//        Toast.makeText(MainActivity.this, "Current token ["+tkn+"]",
//                Toast.LENGTH_LONG).show();

//        firebaseUser  = FirebaseAuth.getInstance().getCurrentUser();
//        reference = FirebaseDatabase.getInstance().getReference("Token").child(firebaseUser.getUid());
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
//                    Token token = snapshot.getValue(Token.class);
//                    if(token.getId().equals(firebaseUser.getUid())){
//                        HashMap<String,Object> hashMap = new HashMap<>();
//                        hashMap.put("id",firebaseUser.getUid());
//                        hashMap.put("apiKey",tkn);
//                        reference.setValue(hashMap);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        replaceFragment(new ChatFragment());

        viewPager = findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new ChatFragment(), "Chats");
        viewPagerAdapter.addFragments(new UsersFragment(), "Users");
        viewPagerAdapter.addFragments(new ProfileFragment(), "Profile");
        viewPager.setAdapter(viewPagerAdapter);

        mainBottomNav = findViewById(R.id.main_bottom_nav);
        mainBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.bottom_action_chats:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.bottom_action_users:
                        viewPager.setCurrentItem(1);

                        return true;
                    case R.id.bottom_action_profile:
                        viewPager.setCurrentItem(2);

                        return true;
                    default:
                        return false;
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (prevItem != null) {
                    prevItem.setChecked(false);
                } else {
                    mainBottomNav.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                mainBottomNav.getMenu().getItem(position).setChecked(true);
                prevItem = mainBottomNav.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        button_logout = findViewById(R.id.button_logout);
        profile_image = findViewById(R.id.activity_main_profile_picture);
        username = findViewById(R.id.activity_main_username);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                OneSignal.setSubscription(false);
                startActivity(new Intent(MainActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

    }


    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragmentList;
        private ArrayList<String> titles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragmentList = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragments(Fragment fragment, String title) {
            fragmentList.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }


}

