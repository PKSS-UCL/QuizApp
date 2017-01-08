package com.paavan.quizappcoursework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import static java.lang.String.valueOf;


public class QuizScreen extends AppCompatActivity
{
    //layout stuff
    private Button trueButton;
    private Button falseButton;
    private Button skipButton;
    private Button cheatButton;
    private TextView questionTextView;
    private TextView cheatView;
    private TextView currentPlayer;

    //scores, names, gamestatus
    private int score;
    private int scoresaved;
    private Attempt attempt;
    private boolean multiplayer;
    private String username1;
    private String username2;
    private boolean player2;

    //list of questions
    private Question[] questionBank = new Question[] {
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
            new Question(R.string.question_uk,false),
            new Question(R.string.question_europe,true),
            new Question(R.string.question_waterfall,false),
            new Question(R.string.question_giraffes,false),
            new Question(R.string.question_penguins,true),
    };

    //current question status
    private int currentIndex = 0;
    private boolean hasCheated = false;


    private void updateQuestion() //increment question
    {
        int question = questionBank[currentIndex].getTextResId();
        questionTextView.setText(question);
        if (cheatView != null)
        {
            cheatView.setText("No points for cheating..."); //reset cheatview
        }
        hasCheated = false; //reset cheat variable
    }



    private void checkAnswer(boolean userPressedTrue) //compare input and answer to question and react accordingly
    {
        boolean answerIsTrue = questionBank[currentIndex].isAnswerTrue();

        int messageResId = 0;

        if (userPressedTrue != answerIsTrue)
        {
            messageResId = R.string.incorrect_toast;
        } else if(hasCheated)
        {
            messageResId = R.string.cheat_toast;
        }
        else
        {
            messageResId = R.string.correct_toast;
            score++;
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    public void goScoreScreen()
    {
        //goto score screen


        //player details
        Bundle data = getIntent().getExtras();
        username1 = (String) data.get("username1");
        player2 = data.getBoolean("player2", false);
        scoresaved = data.getInt("scoresaved");

        multiplayer =(boolean) data.getBoolean("multiplayer",false);

        if (multiplayer && !player2) //edit later
        {
            String username2 = (String) data.get("username2");
        }


        //register attempt in database
        attempt = new Attempt();
        attempt.setUserName(username1);
        attempt.setScore(score);
        if (attempt != null && username1 != null)
        {
            DBHandler db = new DBHandler(this);
            db.addAttempt(attempt);
            db.close();
        }
        else
        {
            Log.d("NA","Null Attempt");
        }



        //to scorescreen
        Log.d("Score is:", valueOf(score));
        Intent scorescreen=new Intent(QuizScreen.this,ScoreScreen.class);
        //send variables
        scorescreen.putExtra("multiplayer", multiplayer);
        scorescreen.putExtra("username1", username1);
        scorescreen.putExtra("score1", score);
        scorescreen.putExtra("player2", player2);
        scorescreen.putExtra("scoresaved",scoresaved);
        scorescreen.putExtra("username2", username2);


        finish();
        startActivity(scorescreen); //goto score screen
    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_screen);

        //set textviews
        questionTextView = (TextView) this.findViewById(R.id.question_text_view);
        cheatView = (TextView) this.findViewById(R.id.cheatView);
        currentPlayer = (TextView) this.findViewById(R.id.currentplayer);
        score = 0;






        //set Textfield at top as their username1
        Bundle bundle = getIntent().getExtras();
        username1 =  (String) bundle.get("username1");
        multiplayer = (boolean) bundle.get("multiplayer");
        currentPlayer.setText(username1);


        //2player stuff
        player2 = bundle.getBoolean("player2",false);
        username2 = (String) bundle.get("username2");
        if (player2)
        {
            scoresaved = (int) bundle.get("scoresaved");
        }


        //buttons

        trueButton = (Button) this.findViewById(R.id.true_button);
        trueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkAnswer(true);
                if (currentIndex == 9)
                {
                    goScoreScreen();
                }
                else
                {
                    currentIndex = (currentIndex + 1) % questionBank.length;
                    updateQuestion();
                }
            }
        });
        falseButton = (Button) this.findViewById(R.id.false_button);
        falseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkAnswer(false);
                if (currentIndex == 9)
                {
                    goScoreScreen();
                }
                else
                {
                    currentIndex = (currentIndex + 1) % questionBank.length;
                    updateQuestion();
                }
            }
        });

        skipButton = (Button) this.findViewById(R.id.skip_button);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex == 9)
                {
                    goScoreScreen();
                }
                else
                {
                    currentIndex = (currentIndex + 1) % questionBank.length;
                    updateQuestion();
                }
            }
        });

        cheatButton = (Button) this.findViewById(R.id.cheatbutton);
        cheatButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Display answer to current question
                Log.d("Cheat", "Button Pressed: ");
                boolean answerIsTrue = questionBank[currentIndex].isAnswerTrue();
                String str = valueOf(answerIsTrue); //convert to string
                if (cheatView != null)
                {
                    cheatView.setText(str);
                }
                else
                {
                    Log.d("Cheat", "Null Textview");
                }
                hasCheated = true;
            }
        });

        updateQuestion();

    }



}
