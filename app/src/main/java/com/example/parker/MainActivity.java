package com.example.parker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Calendar;
import java.util.Date;

/*Implements NavigationItemSelectedListener*/
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    String userId;
boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.draw_layout);
        toolbar = findViewById(R.id.toolbar);
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        updatenavheader();
        /*Creating the toggle*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        /*For the fragment to be selected by default*/
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new aboutus()).commit();
            navigationView.setCheckedItem(R.id.one);
        }
        fstore.collection("userparkings").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists())
                {
                    DocumentReference documentReference = fstore.collection("userparkings").document(userId);

                    documentReference.addSnapshotListener(MainActivity.this, new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            Date currentTime = Calendar.getInstance().getTime();
                            Toast.makeText(MainActivity.this, currentTime.toString(), Toast.LENGTH_SHORT).show();
                            String time=currentTime.toString().substring(10,16);

                            value.get("outime").toString().equals(time);
                        }
                    });



                }
                else {
                    Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /*Creating a switch for the navigation slider*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.one:
                Intent i = new Intent(getApplicationContext(),
                        MapActivity.class);
                startActivity(i);
                break;
            case R.id.two:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirmation PopUp!").
                        setMessage("Are you sure, that you want to logout?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.getInstance().signOut();
                                Intent i = new Intent(getApplicationContext(),
                                        signup.class);
                                startActivity(i);
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
                break;
            case R.id.three:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_1()).commit();

                break;
            case R.id.two2:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new aboutus()).commit();
                break;
            case R.id.three3:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new feedback()).commit();
                break;
            case R.id.one1:
                Intent intentInvite = new Intent(Intent.ACTION_SEND);
                intentInvite.setType("text/plain");
                String body = "Hey there I'm using this super cool app that helps me find parking spaces with just a few clicks";
                String subject = "Download it from here ";
                intentInvite.putExtra(Intent.EXTRA_SUBJECT, subject);
                intentInvite.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(intentInvite, "Share using"));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /*Back presses*/
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    public void updatenavheader()
    {
        navigationView = findViewById(R.id.nav_view);
View headerView=navigationView.getHeaderView(0);
TextView navUsername=headerView.findViewById(R.id.email);
        final TextView navemail=headerView.findViewById(R.id.eee);
        userId = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fstore.collection("users").document(userId);
         documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                navemail.setText(value.get("emailname").toString());
            }
        });
    }
    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOk:checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());
        if (available == ConnectionResult.SUCCESS) {
            //Everything is fine and the user can make map request
//            Log.d(TAG, "isServicesOk:Gogle play services working fine");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //Error resolvable
            Log.d(TAG, "isServiceOk:Error is resolvable we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else
        {
            Toast.makeText(this, "We cannot make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}