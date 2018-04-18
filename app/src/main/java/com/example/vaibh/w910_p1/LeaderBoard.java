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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderBoard extends Fragment {

    View fragLeader;
    Map<String,Integer> mMap = new HashMap<String, Integer>();


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

        usersdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot uniqueKey : dataSnapshot.getChildren()) {
                  // Log.i("Working","First Lopp " +uniqueKey.getKey() + uniqueKey.child("Name").getValue().toString());
                    for(DataSnapshot divisonKey : uniqueKey.getChildren()){
                    //    Log.i("Working", "For children:      "+ divisonKey.getKey() +"  "+divisonKey.getChildren().toString());
                        for(DataSnapshot divisonEntires: divisonKey.getChildren()){
                           // Log.i("Working", "For divison children"+divisonEntires.getKey() + "  "+ divisonEntires.getChildren().toString());
                                //Log.i("Working", "Values are:   " + divisonEntires.getValue() + " Key is:   " + divisonEntires.getKey().equals("TopScore"));
                                if(divisonEntires.getKey().equals("TopScore") == true){
                                    Log.i("WorkingM", divisonEntires.getValue()+ " "+ uniqueKey.child("Name").getValue());
                                    mMap.put(uniqueKey.child("Name").getValue().toString(),Integer.parseInt(divisonEntires.getValue().toString()));
                                }
                        }
                    }
                }
                ArrayList<String> finalValues = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : mMap.entrySet()){
                    Log.i("WorkingM","K,V     :"+entry.getKey() + entry.getValue());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.i("WorkingM","Here");

        return fragLeader;
    }

    

    public static class userInfo{
        public String score, tries, tscore;

        public userInfo(String tscore, String score,String tries){
            this.score = score;
            this.tries = tries;
            this.tscore = tscore;
        }

    }
}
