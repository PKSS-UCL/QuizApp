package com.paavan.quizappcoursework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;

public class MultiplayerStartScreen extends AppCompatActivity
{
    //layout stuff
    private Button startbutton;
    private EditText userid;
    private EditText password;
    //names
    private String username1;
    private String username2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_start_screen);

        final DBHandler db = new DBHandler(this);

        Bundle data = getIntent().getExtras();
        if (data != null)
        {
            username1 = (String) data.get("username1");
        }
        else Log.d("From StartScreen", "Null P1 ");


        //set textfields
        userid = (EditText) findViewById(R.id.userid);
        password = (EditText) findViewById(R.id.password);

        //set start button
        startbutton = (Button) findViewById(R.id.startbutton);
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                //recieve edittexts
                username2 = userid.getText().toString().trim();
                String userpassword2 = password.getText().toString();

                if(username2 == null || userpassword2 == null)
                {
                    restart();
                }

                ArrayList<User> users = new ArrayList<User>();
                users = db.getAllUsers();
                for (User user : users)
                {
                    if (user.getUserName().equals(username2) && user.getPassword().equals(userpassword2))
                    {
                        goQuizScreen();
                    } else if (user.getUserName().equals(username2))
                    {
                        restart();
                    }
                }
                User newuser = new User(); //add new user if need too
                newuser.setPassword(userpassword2);
                newuser.setUserName(username2);
                db.addUser(newuser);
                db.close();
                goQuizScreen();
            }
        });

    }

    //go to quiz activity for player1
    private void goQuizScreen()
    {
        username1 = (String) getIntent().getStringExtra("username1");
        username2 = userid.getText().toString().trim();
        Intent QuizScreen = new Intent(MultiplayerStartScreen.this, QuizScreen.class);
        QuizScreen.putExtra("multiplayer",true); //send multiplayer test
        QuizScreen.putExtra("username1",username1);//send P1 name
        QuizScreen.putExtra("username2", username2); //send P2 name
        finish();
        startActivity(QuizScreen);
    }

    //restart app;
    private void restart()
    {
        Toast.makeText(MultiplayerStartScreen.this, "Invalid details", Toast.LENGTH_SHORT).show();
        Intent restart = new Intent(MultiplayerStartScreen.this, StartScreen.class);
        finish();
        startActivity(restart);
    }
}
