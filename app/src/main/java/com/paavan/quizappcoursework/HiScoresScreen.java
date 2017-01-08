package com.paavan.quizappcoursework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.lang.Integer;


public class HiScoresScreen extends AppCompatActivity
{
    //layout stuff
    private Button restartbutton;
    private ListView dblist;
    private Button multiplayerbutton;
    private Button finishbutton;

    //names,scores and game status
    private String username1;
    private String username2;
    private boolean multiplayer;
    private boolean player2;
    private int score1;
    private int score2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hi_scores_screen);

        //load attempts table
        DBHandler dbhandler = new DBHandler(this);
        List<Attempt> attempts = dbhandler.getAllAttempts();

        //setup buttons
        restartbutton = (Button) this.findViewById(R.id.restartbutton);
        multiplayerbutton = (Button) findViewById(R.id.multiplayerbutton);
        finishbutton = (Button) findViewById(R.id.finishbutton);

        //recieve and set names and scores and gamestatus
        Bundle data = getIntent().getExtras();
        final String username1 = data.getString("username1");
        final String username2 =  data.getString("username2");
        final boolean multiplayer = (boolean) data.get("multiplayer");
        boolean player2 = (boolean) data.get("player2");
        final int score = data.getInt("score");
        final int scoresaved = data.getInt("scoresaved");
        //Log.d("Value of scoresaved", Integer.toString(scoresaved)); fine here
        if (player2)
        {
            //if both players have gone only finish button available
            restartbutton.setVisibility(View.GONE);
            multiplayerbutton.setVisibility(View.GONE);
        }
        else
        {
            if (multiplayer)
            {
                Log.d("HiScore", "multiplayer = true");
                //if player1 finished, show player2 startbutton
                restartbutton.setVisibility(View.GONE);
                finishbutton.setVisibility(View.GONE);

            }
            else
            {
                //else show restart button
                multiplayerbutton.setVisibility(View.GONE);
                finishbutton.setVisibility(View.GONE);
            }
        }


        //Set Hi Score List
        dblist = (ListView) findViewById(R.id.dblist);
        ArrayList<Integer> values = dbhandler.getUsersAttempts(username1);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_list_item_1,values);
        dblist.setAdapter(adapter);
        dbhandler.close();

        //Set Restart Button

        restartbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent restart = new Intent(HiScoresScreen.this, StartScreen.class);
                finish();
                startActivity(restart);
            }
        });


        //set player2 start button
        multiplayerbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                Intent quiz2 = new Intent(HiScoresScreen.this, QuizScreen.class);
                //send names and p1 score
                quiz2.putExtra("multiplayer", true);
                quiz2.putExtra("username1", username2);
                quiz2.putExtra("username2",username1);
                quiz2.putExtra("player2", true);
                quiz2.putExtra("scoresaved",score);
                finish();
                startActivity(quiz2);
            }
        });

        //set up finish button
       finishbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent finish = new Intent(HiScoresScreen.this, Finish.class);
                //send names and scores
                finish.putExtra("username1",username2);
                finish.putExtra("score1",scoresaved);
                finish.putExtra("score2",score);
                finish.putExtra("username2",username1);
                finish();
                startActivity(finish);
            }
        });
    }
}
