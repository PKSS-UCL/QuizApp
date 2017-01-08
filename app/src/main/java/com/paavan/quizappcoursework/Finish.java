package com.paavan.quizappcoursework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class Finish extends AppCompatActivity
{
    //names and scores
    private String username1 ="";
    private String username2 ="";
    private int score1;
    private int score2;

    //layout stuff
    private Button restartbutton;
    private TextView winner;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        winner = (TextView) findViewById(R.id.winner);


        //recieve scores and names
        Bundle data = getIntent().getExtras();
        username1 =(String) data.get("username1");
        score1 = (int) data.get("score1");
        username2 = (String) data.get("username2");
        score2 = (int) data.get("score2");


        // show result
        if (score1 > score2)
        {
            winner.setText(username1 + " wins!");
        }
        else if (score2 > score1)
        {
            winner.setText(username2 + " wins!");
        }
        else
        {
            winner.setText("Draw!!!");
        }


        //set up restart button
        Button restartbutton = (Button) this.findViewById(R.id.restartbutton);
        restartbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent restart = new Intent(Finish.this, StartScreen.class);
                finish();
                startActivity(restart);
            }
        });

    }
}
