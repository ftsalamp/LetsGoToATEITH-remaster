package com.example.android.letsgotoateith;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<String> driverDataset =new ArrayList<String>();
    private ArrayList<String> passengerDataset =new ArrayList<String>();
    private ListView driverListView, passengerListView;
    private ArrayAdapter<String> driverAdapter,passengerAdapter;
    private FirebaseAuth mAuth;
    private boolean first = true;
    private DatabaseReference mUsersTable;
    private FloatingActionButton fab,fab2, fab3;
    private boolean isFABOpen=false;
    private LinearLayout fabLayout2, fabLayout3;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) { // if the user is not signed in start the sign in activity
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
            finish();
        }
        mUsersTable = FirebaseDatabase.getInstance().getReference("users");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {

                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    if (first) {
                        final TextView username = (TextView) findViewById(R.id.userName);
                        final TextView email = (TextView) findViewById(R.id.usersEmail);

                        mUsersTable.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);
                                        username.setText("Welcome " + user.getName());
                                        email.setText(user.getEmail());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(MainActivity.this, R.string.userInfo_fail, Toast.LENGTH_SHORT);
                                    }
                                });


                        email.setText(mAuth.getCurrentUser().getEmail());
                        username.setText(mAuth.getCurrentUser().getDisplayName());
                        first = false;
                    }
                }

                @Override
                public void onDrawerClosed(View drawerView) {

                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            });
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.getMenu().getItem(0).setChecked(true);
        }

        //driverDataset = new String[]{"a", "b", "c"};
        driverListView = (ListView) findViewById(R.id.manageTrDriver_recycler_view);
        driverAdapter = new ArrayAdapter<>(this, R.layout.card_view, R.id.info_text, driverDataset);
        driverListView.setAdapter(driverAdapter);

        passengerListView = (ListView) findViewById(R.id.manageTrPassenger_recycler_view);
        passengerAdapter = new ArrayAdapter<>(this, R.layout.card_view, R.id.info_text, passengerDataset);
        passengerListView.setAdapter(passengerAdapter);

        fabLayout2 =(LinearLayout)findViewById(R.id.fabLayout2);
        fabLayout3=(LinearLayout)findViewById(R.id.fabLayout3);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });
        fabLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regOwn = new Intent(MainActivity.this,OwnAcarActivity.class);
                startActivity(regOwn);
            }
        });

        DatabaseReference mTransferTable= FirebaseDatabase.getInstance().getReference("transfer");
        mTransferTable.orderByChild("userId").equalTo(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                driverDataset.removeAll(driverDataset);
                driverAdapter.notifyDataSetChanged();
                for (DataSnapshot transferSnapshot: dataSnapshot.getChildren()) {
                    Transfer tr= (Transfer) transferSnapshot.getValue(Transfer.class);
                    driverAdapter.add(tr.toString());
                    driverAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(isFABOpen) {
            closeFABMenu();
        }else{
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ownAcar) {
            Intent regOwn = new Intent(MainActivity.this,OwnAcarActivity.class);
            startActivity(regOwn);

        } else if (id == R.id.nav_looking) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
            finish();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFABMenu(){
        isFABOpen=true;

        fabLayout2.setVisibility(View.VISIBLE);
        fabLayout3.setVisibility(View.VISIBLE);

        fab.animate().rotationBy(180);
        fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabLayout3.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (navigationView != null)
            navigationView.getMenu().getItem(0).setChecked(true);
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab.animate().rotationBy(-180);
        fabLayout2.animate().translationY(0);
        fabLayout3.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!isFABOpen){
                    fabLayout2.setVisibility(View.GONE);
                    fabLayout3.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });



        fab.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
    }
}
