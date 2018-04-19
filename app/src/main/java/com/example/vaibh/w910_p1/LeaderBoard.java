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
    int mapSize ;
    String [] key= new String[5];


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
        for (int i=KeyList.size()-1;i>=0 && i >=KeyList.size()-5; i-- ){
            Log.d("KeyDebug",KeyList.get(i));
            //abc.setText(keyList.get(i)+Integer.toString(mMap.get(keyList.get(i)).intValue()));

        }
        for (Map.Entry<String, Integer> entry : mMap.entrySet()){
            Log.i("Sorted Map","K,V     :"+entry.getKey() + entry.getValue());
        }

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
