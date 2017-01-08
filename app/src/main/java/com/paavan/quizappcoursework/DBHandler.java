package com.paavan.quizappcoursework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by waynetsui on 16/12/16.
 */


public class DBHandler extends SQLiteOpenHelper {
    public static final String TAG = "COMP211P";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Quiz";
    // Table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ATTEMPTS = "attempts";
    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_SCORE = "score";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT,"
                 + KEY_PASSWORD + " TEXT" +")";

        String CREATE_ATTEMPTS_TABLE = "CREATE TABLE " + TABLE_ATTEMPTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " TEXT,"
                + KEY_SCORE + " INTEGER" +")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_ATTEMPTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        // Creating tables again
        onCreate(db);
    }

    // Adding new user
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        // To ensure that there are no two users with the same username in User Table
        boolean USER_NOT_ADDED = !doesUserExist(user.getUserName());

        if (USER_NOT_ADDED) {
            ContentValues values = new ContentValues();
            // Primary key is auto-generated
            values.put(KEY_USERNAME, user.getUserName()); // Username

            values.put(KEY_PASSWORD, user.getPassword()); // User password

            // Inserting Row
            db.insert(TABLE_USERS, null, values);
            db.close(); // Closing database connection
        } else {
            Log.d(TAG, "addUser: " + user.getUserName() + " already exists in the database.");
        }
    }

    // Getting one user
    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID,
                        KEY_USERNAME,KEY_PASSWORD}, KEY_USERNAME + "=?",
                new String[]{username}, null, null, null, null);

        User user = null;

        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            user = new User();
            user.setUserName(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            cursor.close();
        }

        // return shop
        return user;
    }

    // Getting All Users
    public ArrayList<User> getAllUsers() {
        ArrayList<User> userList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUserName(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                // Adding contact to list
                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return userList;
    }

    // Getting users Count
    public int getUsersCount() {
        String countQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // Updating a user
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUserName());
        values.put(KEY_PASSWORD, user.getPassword());

        // updating row
        return db.update(TABLE_USERS, values, KEY_USERNAME + " = ?",
                new String[]{user.getUserName()});
    }

    // Deleting a user
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_USERNAME + " = ?",
                new String[] { user.getUserName() });
        db.close();
    }

    // Adding new attempt
    public void addAttempt(Attempt attempt) {
        SQLiteDatabase db = this.getWritableDatabase();

        boolean IS_VALID_ATTEMPT = doesUserExist(attempt.getUserName());

        if (IS_VALID_ATTEMPT && attempt != null) {
            ContentValues values = new ContentValues();
            values.put(KEY_USERNAME, attempt.getUserName()); // Username
            try
            {
                values.put(KEY_SCORE, attempt.getScore()); // User score
            } catch (ScoreException e)
            {
                e.printStackTrace();
            }
            // Inserting Row
            db.insert(TABLE_ATTEMPTS, null, values);
            db.close(); // Closing database connection
        } else {
            Log.d(TAG, "addAttempt: The username of the attempt made is not a valid username.");
        }

    }

    // Getting All Attempts
    public ArrayList<Attempt> getAllAttempts() {
        ArrayList<Attempt> attemptList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ATTEMPTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Attempt attempt = new Attempt();
                attempt.setUserName(cursor.getString(1));
                attempt.setScore(cursor.getInt(2));
                // Adding contact to list
                attemptList.add(attempt);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return attemptList;
    }

    // Getting attempts Count
    public int getAttemptsCount() {
        String countQuery = "SELECT * FROM " + TABLE_ATTEMPTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    private boolean doesUserExist(String username) {
        return !(getUser(username) == null);
    }

    public ArrayList<Integer> getUsersAttempts(String username)
    {
        ArrayList<Integer> scoreList = new ArrayList<>();
        //build query
        String usernameSQL = "'"+username+"'";
        String selectQuery = "SELECT * FROM " + TABLE_ATTEMPTS;
        String whereQuery =" WHERE username = ";
        String orderQuery=" ORDER BY score DESC";
        String fullQuery = selectQuery + whereQuery + usernameSQL + orderQuery;
        //open database and read data
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(fullQuery, null);
        if (cursor != null && cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                scoreList.add(cursor.getInt(cursor.getColumnIndex("score")));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        Log.d("HISCORES", "Finished opening database ");
        return scoreList;

    }

}
