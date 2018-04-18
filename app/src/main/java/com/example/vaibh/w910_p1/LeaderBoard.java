package com.example.vaibh.w910_p1;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.service.autofill.Dataset;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderBoard extends Fragment {

    View fragLeader;


    public LeaderBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragLeader =  inflater.inflate(R.layout.fragment_leader_board, container, false);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference usersdRef = rootRef.child("Users");
        HashMap<Integer,String> mMap = new HashMap<Integer, String>();
        usersdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot uniqueKey : dataSnapshot.getChildren()) {
                   // Log.i("Working", uniqueKey.getKey() + uniqueKey.getChildren().toString());
                    for(DataSnapshot divisonKey : uniqueKey.getChildren()){
                       // Log.i("Working", "For children:      "+ divisonKey.getKey() +"  "+divisonKey.getChildren().toString());
                        for(DataSnapshot divisonEntires: divisonKey.getChildren()){
                           // Log.i("Working", "For divison children"+divisonEntires.getKey() + "  "+ divisonEntires.getChildren().toString());
                                Log.i("Working", "Values are:   " + divisonEntires.getValue().toString() + " Key is:   " + divisonEntires.getKey());

                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return fragLeader;
    }

}
