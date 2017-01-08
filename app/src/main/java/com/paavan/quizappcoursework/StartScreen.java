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

public class StartScreen extends AppCompatActivity
{
    //layout stuff
    private Button startbutton;
    private EditText userid;
    private EditText password;

    //variables
    private String username1;
    private boolean multiplayer;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        final DBHandler db = new DBHandler(this); //get database


        //set textfields
        userid = (EditText) findViewById(R.id.userid);
        password = (EditText) findViewById(R.id.password);

        //set start button
        startbutton = (Button) findViewById(R.id.startbutton);
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String name = userid.getText().toString().trim();
                String userpassword = password.getText().toString();

                //catch empty password or name
                if(username1 == null || userpassword == null)
                {
                    restart();
                }

                //check login
                ArrayList<User> users = new ArrayList<User>();
                users = db.getAllUsers();
                for (User user : users)
                {
                    if (user.getUserName().equals(name) && user.getPassword().equals(userpassword))
                    {
                        goQuizScreen();
                    } else if (user.getUserName().equals(name))
                    {
                        restart();
                        //return;
                    }
                }
                //create new user in database if need to
                User newuser = new User();
                newuser.setPassword(userpassword);
                newuser.setUserName(name);
                db.addUser(newuser);
                db.close();
                goQuizScreen();
            }
        });


        //setup multiplayer button
        Button multiplayerbutton = (Button) findViewById(R.id.multiplayerbutton);
        multiplayerbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                username1 = userid.getText().toString().trim();
                String userpassword = password.getText().toString();

                //catch empty password or name
                if(username1 == null|| userpassword == null)
                {
                    restart();
                }

                //check login details
                ArrayList<User> users = new ArrayList<User>();
                users = db.getAllUsers();
                for (User user : users)
                {
                    if (user.getUserName().equals(username1) && user.getPassword().equals(userpassword))
                    {
                        db.close();
                        goMultiplayer();
                    } else if (user.getUserName().equals(username1))
                    {
                        restart();
                    }
                }

                User newuser = new User(); //add new user if need too
                newuser.setPassword(userpassword);
                newuser.setUserName(username1);
                db.addUser(newuser);
                db.close();
                goMultiplayer();
            }
        });

    }

    //start quiz activity
    private void goQuizScreen()
    {
        username1 = userid.getText().toString().trim();
        Intent QuizScreen = new Intent(StartScreen.this, QuizScreen.class);
        QuizScreen.putExtra("multiplayer",false); //send multiplayer test
        QuizScreen.putExtra("username1",username1);//send P1 name
        finish();
        startActivity(QuizScreen);
    }

    //goto multiplayer startscreen activity
    private  void goMultiplayer()
    {
        username1 = userid.getText().toString().trim();
        Intent QuizScreen = new Intent(StartScreen.this, MultiplayerStartScreen.class);
        QuizScreen.putExtra("multiplayer",true); //send multiplayer test
        QuizScreen.putExtra("username1",username1);//send P1 name
        finish();
        startActivity(QuizScreen);
    }

    //restart app
    private void restart()
    {
        Toast.makeText(StartScreen.this, "Invalid details", Toast.LENGTH_SHORT).show();
        Intent restart = new Intent(StartScreen.this, StartScreen.class);
        finish();
        startActivity(restart);
    }
}
