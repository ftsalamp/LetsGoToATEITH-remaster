package com.example.android.letsgotoateith;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.android.letsgotoateith.R.array.frequency;
import static com.example.android.letsgotoateith.R.array.people;

public class OwnAcarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Spinner peopleSpinner, departureSpinner, directionSpinner, daySpinner, frequencySpinner, areaSpinner;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_a_car);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(!Utils.isNetworkAvailable(this)){
            AlertDialog.Builder builder = new AlertDialog.Builder(OwnAcarActivity.this);
            builder.setMessage(R.string.internet_dialog_message)
                    .setTitle(R.string.internet_dialog_title);
            // Add the button
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener(){

                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });

            // Create the AlertDialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        mAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.getMenu().getItem(1).setChecked(true);
        }

        peopleSpinner = (Spinner) findViewById(R.id.peopleSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> peopleAdapter = ArrayAdapter.createFromResource(this,
                people, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        peopleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        peopleSpinner.setAdapter(peopleAdapter);

        departureSpinner = (Spinner) findViewById(R.id.departureSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> departureAdapter = ArrayAdapter.createFromResource(this,
                R.array.timeSpinner, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        departureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        departureSpinner.setAdapter(departureAdapter);

        areaSpinner = (Spinner) findViewById(R.id.areaSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> areaAdapter = ArrayAdapter.createFromResource(this,
                R.array.areaSpinner, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        areaSpinner.setAdapter(areaAdapter);

        directionSpinner = (Spinner) findViewById(R.id.directionSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> directionAdapter = ArrayAdapter.createFromResource(this,
                R.array.direction, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        directionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        directionSpinner.setAdapter(directionAdapter);

        daySpinner = (Spinner) findViewById(R.id.daySpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this,
                R.array.weekDaySpinner, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        daySpinner.setAdapter(dayAdapter);

        frequencySpinner = (Spinner) findViewById(R.id.frequencySpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> frequencyAdapter = ArrayAdapter.createFromResource(this,
                frequency, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        frequencySpinner.setAdapter(frequencyAdapter);

        Button saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int people =peopleSpinner.getSelectedItemPosition();
                int day=daySpinner.getSelectedItemPosition();
                int departure=departureSpinner.getSelectedItemPosition();
                int direction=directionSpinner.getSelectedItemPosition();
                int frequency=frequencySpinner.getSelectedItemPosition();
                int area=areaSpinner.getSelectedItemPosition();

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("transfer");

                // Creating new Transfer node, which returns the unique key value
                // new Transfer node would be /Transfer/$transferid/
                String transferId = mDatabase.push().getKey();

                // creating Transfer object
                Transfer transfer= new Transfer(people, day, departure, direction, frequency, area, mAuth.getCurrentUser().getUid());

                // pushing user to 'Transfer' node using the transferId
                mDatabase.child(transferId).setValue(transfer,new DatabaseReference.CompletionListener(){

                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError==null){
                            Toast.makeText(OwnAcarActivity.this,"Transfer saves successfully!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(OwnAcarActivity.this,"An error occurred, if you are offline your changes will be saved when you go back online.", Toast.LENGTH_SHORT).show();
                        }

                        finish();
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {
            finish();
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
}
