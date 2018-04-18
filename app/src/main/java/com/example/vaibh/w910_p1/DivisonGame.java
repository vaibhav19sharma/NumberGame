package com.example.vaibh.w910_p1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class DivisonGame extends Fragment implements ValueEventListener {

    private View fragDivison;
    private TextView txtNum, txtDeno, txtGameOver;
    private EditText edtAnswer;
    private Button btnSubmit , btnRestart;
    private int check=1;
    private int win;
    private int ans;
    private String uid, topScore, currentTopScore;
    DatabaseReference mRootRef, mUserRef, mProgress;


    public DivisonGame() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragDivison = inflater.inflate(R.layout.fragment_divison_game, container, false);

        txtNum = fragDivison.findViewById(R.id.txtNum);
        txtDeno = fragDivison.findViewById(R.id.txtDeno);
        edtAnswer = fragDivison.findViewById(R.id.edtAnswer);
        btnSubmit = fragDivison.findViewById(R.id.btnSubmit);
        btnRestart = fragDivison.findViewById(R.id.btnRestart);
        txtGameOver = fragDivison.findViewById(R.id.txtGameOver);

        ans = newNumbers();

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        showStuff();

        final DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("Users");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(edtAnswer.getText().toString()) == ans) {
                    win = win + 1;
                }
                if (check != 4 && check <4) {
                    ans = newNumbers();
                    edtAnswer.setText("");
                    check = check + 1;
                    currentUser.child(uid).child("Divison").child("score").setValue(win);
                    currentUser.child(uid).child("Divison").child("tries").setValue(check);
                   // Log.i("Working:TopScore",currentTopScore);
                    getTopScore();

                    if(topScore == null){
                        Log.i("Working","NULL Returned");
                    }
                    else {
                        currentUser.child(uid).child("Divison").child("TopScore").setValue(Integer.parseInt(topScore));
                    }
                    Toast.makeText(getContext(), Integer.toString(win) + " out of 10, " + Integer.toString(check), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    getTopScore();
                    if(Integer.parseInt(topScore) < win){
                        currentUser.child(uid).child("Divison").child("TopScore").setValue(Integer.toString(win));
                    }

                    Log.i("Working",currentTopScore + "current top score is" );

                    hideStuff();
                    txtGameOver.setText(Integer.toString(win) + " out of "+ Integer.toString(check));
                    txtGameOver.setTextSize(24);

                    Toast.makeText(getContext(), Integer.toString(win) + " out of 10", Toast.LENGTH_SHORT).show();
                }
            }
        });



        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStuff();
                win = 0;
                check = 1;
                ans = newNumbers();
            }
        });

        return fragDivison;
    }
    public int newNumbers(){
        Random rand = new Random();
        int up = rand.nextInt(169);
        final int[] array = new int[up];
        int temp =0;
        for(int j=1; j<(up/2);j++) {
            if (up % j == 0 && j !=0) {
                array[temp] = j;
                Log.v("Working", Integer.toString(j));
                temp += 1;
            }
        }
        if(temp == 0){
            temp +=1;
        }
        int rnd = new Random().nextInt(temp);
        Log.v("Working",Integer.toString(rnd));
        //   Log.v("Working",Integer.toString(array[rnd]));

        txtNum.setText(Integer.toString(up));
        txtDeno.setText(Integer.toString(array[rnd]));
        int ans=up/array[rnd];
        return ans;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    void getTopScore(){
        DatabaseReference currentUser_read = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Divison/TopScore");
        currentUser_read.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              //      userInfo obj = dataSnapshot.getValue(userInfo.class);
                Log.i("WorkingT", "Working uptil here;   ");
                if(dataSnapshot.exists()) {
                    topScore = dataSnapshot.getValue().toString();
                    Log.i("WorkingT", "Working uptil here;   " + dataSnapshot.getValue());
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Divison").child("TopScore").setValue(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void hideStuff(){
        txtNum.setVisibility(View.INVISIBLE);
        txtDeno.setVisibility(View.INVISIBLE);
        edtAnswer.setVisibility(View.INVISIBLE);
        btnSubmit.setVisibility(View.INVISIBLE);
        btnRestart.setVisibility(View.VISIBLE);
        txtGameOver.setVisibility(View.VISIBLE);
    }

    void showStuff(){

        txtNum.setVisibility(View.VISIBLE);
        txtDeno.setVisibility(View.VISIBLE);
        edtAnswer.setVisibility(View.VISIBLE);
        btnSubmit.setVisibility(View.VISIBLE);
        btnRestart.setVisibility(View.INVISIBLE);
        txtGameOver.setVisibility(View.INVISIBLE);
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
