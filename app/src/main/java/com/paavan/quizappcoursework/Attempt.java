package com.paavan.quizappcoursework;


public class Attempt
{
    private String userName;
    private int score;

    public Attempt() {
    }

    public Attempt(String userName, int score) {
        this.userName = userName;
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public int getScore() throws ScoreException
    {
        if (score > 10)
        {
            throw new ScoreException("Score too high! Cheater!");
        }
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }

}