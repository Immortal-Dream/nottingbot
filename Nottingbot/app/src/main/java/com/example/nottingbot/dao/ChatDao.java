package com.example.nottingbot.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.nottingbot.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object that define the methods and queries needed to perform CRUD
 * (Create, Read, Update, and Delete) operations on the data, along with the necessary database
 * connection and transaction management code.
 *
 * @author Junhan LIU
 */
public class ChatDao extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "chat_db";

    public ChatDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public ChatDao(@Nullable Context context, @Nullable String name,
                   @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Create the database and create a table for chat message
     *
     * @param sqLiteDatabase the object of SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTables = "CREATE TABLE IF NOT EXISTS chat(chat_id INTEGER PRIMARY KEY," +
                "content VARCHAR(255),type INTEGER);";
        sqLiteDatabase.execSQL(createTables);
    }

    /**
     * The onUpgrade method is called when the database version number changes,
     * and it's used to upgrade the database schema to the latest version.
     *
     * @param sqLiteDatabase This is a reference to the SQLite database that needs to be upgraded.
     * @param i              This is an integer that represents the current version number of the database.
     * @param i1             This is an integer that represents the target version number of the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * Insert one message into chat table
     *
     * @param content the content of one message
     * @param type    the type of that message, 0-received 1-send
     * @return return newly inserted row id
     * @author Junhan LIU
     */
    public long insertChat(String content, Integer type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", content);
        values.put("type", type);
        // insert row
        long id = db.insert("chat", null, values);
        // close the database
        db.close();
        // return the id
        return id;
    }

    /**
     * Get all history message in database
     *
     * @return a list of Message that contains all history message
     */
    public List<Message> getMessage() {
        List<Message> messageList = new ArrayList<>();
        String selectQuery = "SELECT * FROM chat;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Message message = new Message();
                message.setContent(cursor.getString(1));
                message.setType(cursor.getInt(2));
                // add message to list
                messageList.add(message);
            } while (cursor.moveToNext());

        }
        return messageList;
    }

    /**
     * Clear all chat history in chat table
     */
    public void clearMessage() {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearStatement = "DELETE FROM chat;";
        db.execSQL(clearStatement);
    }
}
