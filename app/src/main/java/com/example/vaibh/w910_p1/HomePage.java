package com.example.vaibh.w910_p1;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    String uid;
    RelativeLayout home;
    Button btnAdd, btnSub, btnMul, btnDiv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        home = (RelativeLayout) findViewById(R.id.home);
        setSupportActionBar(toolbar);

        btnAdd = findViewById(R.id.btnAdd);
        btnSub = findViewById(R.id.btnSub);
        btnMul = findViewById(R.id.btnMul);
        btnDiv = findViewById(R.id.btnDiv);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;

                fragment = new DivisonGame();
                Bundle args = new Bundle();
                args.putString("mathType", Integer.toString(R.id.nav_add));
                //txtMessage.setVisibility(View.INVISIBLE);
                fragment.setArguments(args);

                if(fragment != null){
                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    home.setVisibility(View.INVISIBLE);
                    ft.replace(R.id.content_main, fragment);
                    ft.commit();
                }

            }
        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;

                fragment = new DivisonGame();
                Bundle args = new Bundle();
                args.putString("mathType", Integer.toString(R.id.nav_sub));
                //txtMessage.setVisibility(View.INVISIBLE);
                fragment.setArguments(args);

                if(fragment != null){
                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    home.setVisibility(View.INVISIBLE);
                    ft.replace(R.id.content_main, fragment);
                    ft.commit();
                }
            }
        });

        btnMul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;

                fragment = new DivisonGame();
                Bundle args = new Bundle();
                args.putString("mathType", Integer.toString(R.id.nav_mul));
                //txtMessage.setVisibility(View.INVISIBLE);
                fragment.setArguments(args);

                if(fragment != null){
                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    home.setVisibility(View.INVISIBLE);
                    ft.replace(R.id.content_main, fragment);
                    ft.commit();
                }
            }
        });

        btnDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;

                fragment = new DivisonGame();
                Bundle args = new Bundle();
                args.putString("mathType", Integer.toString(R.id.nav_div));
                //txtMessage.setVisibility(View.INVISIBLE);
                fragment.setArguments(args);

                if(fragment != null){
                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    home.setVisibility(View.INVISIBLE);
                    ft.replace(R.id.content_main, fragment);
                    ft.commit();
                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        currentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Name").getValue().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        // for division
        if (id == R.id.nav_div) {
            fragment = new DivisonGame();
            Bundle args = new Bundle();
            args.putString("mathType", Integer.toString(id));
            //txtMessage.setVisibility(View.INVISIBLE);
            fragment.setArguments(args);
        }
        else if (id == R.id.nav_add) {
            fragment = new DivisonGame();
            Bundle args = new Bundle();
            args.putString("mathType", Integer.toString(id));
            //txtMessage.setVisibility(View.INVISIBLE);
            fragment.setArguments(args);
        }
        else if (id == R.id.nav_sub) {
            fragment = new DivisonGame();
            Bundle args = new Bundle();
            args.putString("mathType", Integer.toString(id));
            //txtMessage.setVisibility(View.INVISIBLE);
            fragment.setArguments(args);
        }
        else if (id == R.id.nav_mul) {
            fragment = new DivisonGame();
            Bundle args = new Bundle();
            args.putString("mathType", Integer.toString(id));
            //txtMessage.setVisibility(View.INVISIBLE);
            fragment.setArguments(args);
        }
        else if (id == R.id.nav_data) {
            fragment = new DataPage();
            //txtMessage.setVisibility(View.INVISIBLE);
        }
        else if(id == R.id.nav_leaderboard){
            fragment = new LeaderBoard();
            //txtMessage.setVisibility(View.INVISIBLE);
        }


        if(fragment != null){
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            home.setVisibility(View.INVISIBLE);
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
