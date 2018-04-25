package com.example.vaibh.w910_p1;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.service.autofill.Dataset;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderBoard extends Fragment {

    View fragLeader;
    String [] key= new String[5];
    private int[] txtName = new int[]{R.id.txtName1,R.id.txtName2,R.id.txtName3,R.id.txtName4,R.id.txtName5};
    private int[] txtScore = new int[]{R.id.txtScore1,R.id.txtScore2,R.id.txtScore3,R.id.txtScore4,R.id.txtScore5};
    private int[] txtMath = new int[]{R.id.txtMath1, R.id.txtMath2, R.id.txtMath3, R.id.txtMath4, R.id.txtMath5};
    //private ProgressBar pBar;


    public LeaderBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragLeader =  inflater.inflate(R.layout.fragment_leader_board, container, false);
        DatabaseReference currentUser_read = FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard");

        currentUser_read.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    TextView tempName = fragLeader.findViewById(txtName[count]);
                    TextView tempScore = fragLeader.findViewById(txtScore[count]);
                    TextView tempMath = fragLeader.findViewById(txtMath[count]);
                    tempName.setText(postSnapshot.child("name").getValue().toString());
                    tempScore.setText(postSnapshot.child("score").getValue().toString());
                    tempMath.setText(postSnapshot.child("mathtype").getValue().toString());
                    count += 1;
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.i("WorkingM","Here");

        return fragLeader;
    }

}
