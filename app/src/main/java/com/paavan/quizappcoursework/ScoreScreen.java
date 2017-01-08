package com.paavan.quizappcoursework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreScreen extends AppCompatActivity
{
    //layout stuff
    private TextView ScoreText1;
    private TextView nameText1;

    //names and scores and gamestatus
    private String username1;
    private String username2;
    private int score1;
    private int scoresaved;
    private Button hiscoresbutton;
    private boolean multiplayer;
    private boolean player2;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_screen);

        //recieve all variables
        Bundle data = getIntent().getExtras();
        final boolean multiplayer = (boolean) data.get("multiplayer");
        final String username1 = data.getString("username1");
        final String username2 = (String) data.get("username2");
        player2 = data.getBoolean("player2",false);
        final int score1 = data.getInt("score1");
        final int scoresaved = data.getInt("scoresaved");




        //set up textviews
        TextView ScoreText1 = (TextView)findViewById(R.id.ScoreText1);
        ScoreText1.setText(String.valueOf(score1));
        TextView nameText1 = (TextView) findViewById(R.id.nameText1);
        nameText1.setText(username1);

        //set hiscores button
        hiscoresbutton = (Button) this.findViewById(R.id.hiscoresbutton);
        hiscoresbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goto hiscores
                Intent hiscores = new Intent(ScoreScreen.this, HiScoresScreen.class);
                hiscores.putExtra("username1",username1);
                hiscores.putExtra("username2",username2);
                hiscores.putExtra("multiplayer",multiplayer);
                hiscores.putExtra("player2",player2);
                hiscores.putExtra("score",score1);
                hiscores.putExtra("scoresaved",scoresaved);
                startActivity(hiscores);
            }
        });
    }
}
