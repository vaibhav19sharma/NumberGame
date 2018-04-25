package com.example.vaibh.w910_p1;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class DivisonGame extends Fragment implements ValueEventListener {

    private View fragDivison;
    private TextView txtNum, txtDeno, txtGameOver, txtGameScore, txtSign, txtAnswer;
    private EditText edtAnswer;
    private Button btnSubmit , btnRestart;
    private ImageButton imgAlmost, imgKeep, imgSuper;
    private int check=0;
    private int win = 0;
    private int ans;
    private int max = 10;
    private int pastHistoryCount = 5;
    private String uid, topScore, currentTopScore, userName;
    private String mathgame = "Addition";
    DatabaseReference mRootRef, mUserRef, mProgress;


    public DivisonGame() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragDivison = inflater.inflate(R.layout.fragment_divison_game, container, false);
        String value = getArguments().getString("mathType");
        txtNum = fragDivison.findViewById(R.id.txtNum);
        txtDeno = fragDivison.findViewById(R.id.txtDeno);
        edtAnswer = fragDivison.findViewById(R.id.edtAnswer);
        btnSubmit = fragDivison.findViewById(R.id.btnSubmit);
        btnRestart = fragDivison.findViewById(R.id.btnRestart);
        txtGameOver = fragDivison.findViewById(R.id.txtGameOver);
        txtGameScore = fragDivison.findViewById(R.id.txtGameScore);
        txtSign = fragDivison.findViewById(R.id.txtSign);
        txtAnswer = fragDivison.findViewById(R.id.txtAnswer);
        imgAlmost = fragDivison.findViewById(R.id.imgAlmost);
        imgKeep = fragDivison.findViewById(R.id.imgKeep);
        imgSuper = fragDivison.findViewById(R.id.imgSuper);

        getUsername();

        if (Integer.valueOf(value).equals(R.id.nav_add)){
            mathgame = "Addition";
            txtSign.setText(getString(R.string.add));
            txtGameOver.setText("Current Game is: " + mathgame);
        } else if (Integer.valueOf(value).equals(R.id.nav_sub)){
            mathgame = "Subtraction";
            txtSign.setText(getString(R.string.subtract));
            txtGameOver.setText("Current Game is: " + mathgame);
        } else if (Integer.valueOf(value).equals(R.id.nav_mul)) {
            mathgame = "Multiplication";
            txtSign.setText(getString(R.string.multiply));
            txtGameOver.setText("Current Game is: " + mathgame);
        } else if (Integer.valueOf(value).equals(R.id.nav_div)) {
            mathgame = "Division";
            txtSign.setText(getString(R.string.division));
            txtGameOver.setText("Current Game is: " + mathgame);
        }
        ans = newNumbers(mathgame);
        txtGameScore.setText((Integer.toString(win) + " out of " + Integer.toString(check) + ", " + Integer.toString(max - check) + " more"));



        //ans = newNumbers();

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        showStuff();

        final DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference().child("Users");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtAnswer.getText().toString().equals("")) {
                    Toast.makeText(getContext(),"Enter Your Answer!", Toast.LENGTH_SHORT).show();
                } else {

                    if (Integer.parseInt(edtAnswer.getText().toString()) == ans) {
                        win = win + 1;
                    }
                    check = check + 1;

                    if (check < max) {
                        ans = newNumbers(mathgame);
                        edtAnswer.setText("");
                        currentUser.child(uid).child(mathgame).child("score").setValue(win);
                        currentUser.child(uid).child(mathgame).child("tries").setValue(check);
                        // Log.i("Working:TopScore",currentTopScore);
                        getTopScore();

                        if (topScore == null) {
                            Log.i("Working", "NULL Returned");
                        } else {
                            currentUser.child(uid).child(mathgame).child("TopScore").setValue(Integer.parseInt(topScore));
                        }
                        Toast.makeText(getContext(), Integer.toString(win) + " out of " + Integer.toString(check), Toast.LENGTH_SHORT).show();
                        txtGameScore.setText((Integer.toString(win) + " out of " + Integer.toString(check) + ", " + Integer.toString(max - check) + " more"));
                    } else {
                        hideStuff();
                        if (win > 7) {
                            showSuper();
                        } else if (win > 4) {
                            showAlmost();
                        } else if (win > 0) {
                            showKeep();
                        }

                        getTopScore();


                        if (Integer.parseInt(topScore) < win) {
                            currentUser.child(uid).child(mathgame).child("TopScore").setValue(win);
                            currentUser.child(uid).child(mathgame).child("MathType").setValue(mathgame);
                        }
                        setWinHistory(currentUser, win);

                        getOverallTop(userName, mathgame, Integer.toString(win));

                        //Log.i("Working", currentTopScore + "current top score is");

                        txtGameScore.setText(Integer.toString(win) + " out of " + Integer.toString(check));
                        Toast.makeText(getContext(), Integer.toString(win) + " out of" + Integer.toString(check), Toast.LENGTH_SHORT).show();

                        imgSuper.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), HomePage.class);
                                intent.putExtra("name",userName);
                                startActivity(intent);
                            }
                        });

                        imgAlmost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), HomePage.class);
                                intent.putExtra("name",userName);

                                startActivity(intent);
                            }
                        });

                        imgKeep.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), HomePage.class);
                                intent.putExtra("name",userName);

                                startActivity(intent);
                            }
                        });

                    }
                }
            }
        });



        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStuff();
                win = 0;
                check = 0;
                ans = newNumbers(mathgame);

                imgKeep.setVisibility(View.INVISIBLE);
                imgAlmost.setVisibility(View.INVISIBLE);
                imgSuper.setVisibility(View.INVISIBLE);
            }
        });

        return fragDivison;
    }

    public int newNumbers(String mathgame){

        if (mathgame.equals("Addition")) {
            ans = newAddNumbers();
        } else if (mathgame.equals("Subtraction")) {
            ans = newSubNumbers();
        } else if (mathgame.equals("Multiplication")) {
            ans = newMultNumbers();
        } else if (mathgame.equals("Division")) {
            ans = newDivNumbers();
        }

        return ans;
    }

    private int newDivNumbers() {
        Random rand = new Random();
        int up = rand.nextInt(15);
        int down = rand.nextInt(15);

        txtNum.setText(Integer.toString(up*down));
        txtDeno.setText(Integer.toString(up));
        return down;
    }

    private int newMultNumbers() {
        Random rand = new Random();
        int up = rand.nextInt(15);
        int down = rand.nextInt(15);
        txtNum.setText(Integer.toString(up));
        txtDeno.setText(Integer.toString(down));

        return up*down;
    }

    private int newSubNumbers() {
        Random rand = new Random();
        int up = rand.nextInt(200);
        int down = rand.nextInt(200);
        txtNum.setText(Integer.toString(up+down));
        txtDeno.setText(Integer.toString(down));

        return up;
    }

    private int newAddNumbers() {
        Random rand = new Random();
        int up = rand.nextInt(200);
        int down = rand.nextInt(200);
        txtNum.setText(Integer.toString(up));
        txtDeno.setText(Integer.toString(down));

        return up + down;
    }



    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    void getTopScore(){
        DatabaseReference currentUser_read = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child(mathgame+"/TopScore");
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
                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child(mathgame).child("TopScore").setValue(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void getUsername() {
        DatabaseReference currentUser_read = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUser_read.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userName = dataSnapshot.child(uid).child("Name").getValue().toString();
                } else {
                    userName = "Default";
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void getOverallTop(final String Username, final String Mathtype, final String Userscore) {
        DatabaseReference currentUser_read = FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard");
        currentUser_read.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> Scores = new ArrayList<>(pastHistoryCount);
                    List<String> MathType = new ArrayList<>(pastHistoryCount);
                    List<String> Names = new ArrayList<>(pastHistoryCount);
                    String tempScore, tempScore2 = "", tempName, tempName2 = "", tempType, tempType2= "";

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //for (DataSnapshot postpostSnapshot : postSnapshot.getChildren()) {
                        String score = postSnapshot.child("score").getValue().toString();
                        String name = postSnapshot.child("name").getValue().toString();
                        String mathtype = postSnapshot.child("mathtype").getValue().toString();

                        Scores.add(score);
                        MathType.add(mathtype);
                        Names.add(name);
                        Log.i("Aya", "Okay here");
                    //    }
                    }
                    int count = 0;
                    while (count < pastHistoryCount && Integer.valueOf(Scores.get(count)) > Integer.valueOf(Userscore)) {
                        count += 1;
                        Log.i("Aya", "We successfully counted here. The value of Count is " + Integer.toString(count));
                    }
                    if (count < pastHistoryCount) {

                        for (int i = count; i < pastHistoryCount; i++) {
                            tempScore = dataSnapshot.child("User" + Integer.toString(i)).child("score").getValue().toString();
                            tempName = dataSnapshot.child("User" + Integer.toString(i)).child("name").getValue().toString();
                            tempType = dataSnapshot.child("User" + Integer.toString(i)).child("mathtype").getValue().toString();

                            Log.i("Aya", "We have successfully entered the for loop with " + tempScore + " " + tempName + " " + tempType + " " +Integer.toString(count));

                            if (i == count) {
                                FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User" + Integer.toString(i)).child("score").setValue(Userscore);
                                FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User" + Integer.toString(i)).child("name").setValue(Username);
                                FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User" + Integer.toString(i)).child("mathtype").setValue(Mathtype);

                            } else {
                                FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User" + Integer.toString(i)).child("score").setValue(tempScore2);
                                FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User" + Integer.toString(i)).child("name").setValue(tempName2);
                                FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User" + Integer.toString(i)).child("mathtype").setValue(tempType2);

                            }
                            tempScore2 = tempScore;
                            tempName2 = tempName;
                            tempType2 = tempType;

                        }
                    }

                } else {
                    FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User0").child("score").setValue(Userscore);
                    FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User1").child("score").setValue("NaN");

                    FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User2").child("score").setValue("NaN");
                    FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User3").child("score").setValue("NaN");
                    FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User4").child("score").setValue("NaN");

                    FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User0").child("name").setValue(Username);
                    FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User1").child("name").setValue("NaN");

                    FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User2").child("name").setValue("NaN");
                    FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User3").child("name").setValue("NaN");
                    FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User4").child("name").setValue("NaN");

                    FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User0").child("mathtype").setValue(Mathtype);
                    FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User1").child("mathtype").setValue("NaN");

                    FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User2").child("mathtype").setValue("NaN");
                    FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User3").child("mathtype").setValue("NaN");
                    FirebaseDatabase.getInstance().getReference().child("Users").child("LeaderBoard").child("User4").child("mathtype").setValue("NaN");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    void setWinHistory(final DatabaseReference currentUser, final int newscore) {
        DatabaseReference currentUser_read = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child(mathgame+"/ScoreHistory");
        currentUser_read.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    List<String> winScores = new ArrayList<>(5);
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String score = postSnapshot.getValue().toString();
                        winScores.add(score);
                        Log.i("Scores", "The value of the scores are: "+ score);
                    }

                    for (int i = 0; i < pastHistoryCount-1; i++) {
                        currentUser.child(uid).child(mathgame).child("ScoreHistory").child(Integer.toString(i)).setValue(Integer.valueOf(winScores.get(i+1)));
                    }
                    currentUser.child(uid).child(mathgame).child("ScoreHistory").child(Integer.toString(pastHistoryCount-1)).setValue(newscore);
                }
                else {
                    for (int j = 0; j < pastHistoryCount-1; j++) {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child(mathgame).child("ScoreHistory").child(Integer.toString(j)).setValue(0);
                    }
                    FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child(mathgame).child("ScoreHistory").child(Integer.toString(pastHistoryCount-1)).setValue(newscore);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    void showStuff(){
        txtNum.setVisibility(View.VISIBLE);
        txtDeno.setVisibility(View.VISIBLE);
        edtAnswer.setVisibility(View.VISIBLE);
        edtAnswer.setText("");
        txtSign.setVisibility(View.VISIBLE);
        btnSubmit.setVisibility(View.VISIBLE);
        btnRestart.setVisibility(View.INVISIBLE);
        txtAnswer.setVisibility(View.VISIBLE);
    }

    void hideStuff() {
        txtNum.setVisibility(View.INVISIBLE);
        txtDeno.setVisibility(View.INVISIBLE);
        edtAnswer.setVisibility(View.INVISIBLE);
        txtSign.setVisibility(View.INVISIBLE);
        btnSubmit.setVisibility(View.INVISIBLE);
        txtAnswer.setVisibility(View.INVISIBLE);
        btnRestart.setVisibility(View.VISIBLE);
    }

    void showSuper() {
        imgSuper.setVisibility(View.VISIBLE);
    }

    void showAlmost() {
        imgAlmost.setVisibility(View.VISIBLE);
    }

    void showKeep() {
        imgKeep.setVisibility(View.VISIBLE);
    }
}
