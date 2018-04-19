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
    Map<String,Integer> mMap = new HashMap<String, Integer>();
    int mapSize;
    int iterator;
    String [] key= new String[5];
    private int[] txtName = new int[]{R.id.txtName1,R.id.txtName2,R.id.txtName3,R.id.txtName4,R.id.txtName5};
    private int[] txtScore = new int[]{R.id.txtScore1,R.id.txtScore2,R.id.txtScore3,R.id.txtScore4,R.id.txtScore5};
    private ProgressBar pBar;


    public LeaderBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragLeader =  inflater.inflate(R.layout.fragment_leader_board, container, false);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        int[] txtName = new int[]{R.id.txtName1,R.id.txtName2,R.id.txtName3,R.id.txtName4,R.id.txtName5};
        int[] txtScore = new int[]{R.id.txtScore1,R.id.txtScore2,R.id.txtScore3,R.id.txtScore4,R.id.txtScore5};
        pBar = fragLeader.findViewById(R.id.pBar);
        DatabaseReference usersdRef = rootRef.child("Users");
        hideStuff();

        usersdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot uniqueKey : dataSnapshot.getChildren()) {
                    for(DataSnapshot divisonKey : uniqueKey.getChildren()){
                        for(DataSnapshot divisonEntires: divisonKey.getChildren()){
                            if(divisonEntires.getKey().equals("TopScore") == true){
                                    Log.i("WorkingM", divisonEntires.getValue()+ " "+ uniqueKey.child("Name").getValue());
                                    mMap.put(uniqueKey.child("Name").getValue().toString(),Integer.parseInt(divisonEntires.getValue().toString()));
                                }
                                mapSort();
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

    public void mapSort(){
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(mMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        /*
        //classic iterator example
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }*/


        mMap = sortedMap;
        mapSize=mMap.size();
       /* if(mMap.size() <5){
            for(int i=0;i<(5-mMap.size());i++){
                mMap.put(" ",0);
            }
        }*/
        List<String> KeyList = new ArrayList<String>(mMap.keySet());
        iterator=0;
        for (int i=KeyList.size()-1;i>=0 && i >=KeyList.size()-5; i-- ){
            Log.d("KeyDebug",KeyList.get(i) +"    "+ Integer.toString(i) +"   iterator: " + txtName[iterator]);
                if(iterator>4){
                    iterator=0;
                }
            TextView tempName = fragLeader.findViewById(txtName[iterator]);
            TextView tempScore = fragLeader.findViewById(txtScore[iterator]);
            tempName.setText("    "+Integer.toString(iterator+1) + ".  " + KeyList.get(i));
            tempScore.setText((mMap.get(KeyList.get(i))).toString());
             iterator= iterator+1;
            showStuff();

        }
        for (Map.Entry<String, Integer> entry : mMap.entrySet()){
            Log.i("Sorted Map","K,V     :"+entry.getKey() + entry.getValue());
        }

    }

    void hideStuff(){
        for(int i=0;i<5;i++){
            TextView hideName = fragLeader.findViewById(txtName[i]);
            TextView hideScore = fragLeader.findViewById(txtScore[i]);
            hideName.setVisibility(View.INVISIBLE);
            hideScore.setVisibility(View.INVISIBLE);
        }
        pBar.setVisibility(View.VISIBLE);
    }

    void showStuff(){
        for(int i=0;i<5;i++){
            TextView hideName = fragLeader.findViewById(txtName[i]);
            TextView hideScore = fragLeader.findViewById(txtScore[i]);
            hideName.setVisibility(View.VISIBLE);
            hideScore.setVisibility(View.VISIBLE);
        }
        pBar.setVisibility(View.INVISIBLE);
    }
}
