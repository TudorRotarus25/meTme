package com.tudor.rotarus.unibuc.metme.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tudor.rotarus.unibuc.metme.pojos.responses.post.FriendsPostBody;

import java.util.ArrayList;

/**
 * Created by Tudor on 20.04.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private final String TAG = getClass().getSimpleName();  //"DatabaseHelper";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "metme";

    // Table Names
    private static final String TABLE_CONTACTS = "contacts";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // Contacts Table - column names
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_NAME = "name";
    private static final String KEY_INITIALS = "be_name";

    // Table Create Statements
    private static final String CREATE_TABLE_CONTACTS = "CREATE TABLE "
            + TABLE_CONTACTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_PHONE_NUMBER + " TEXT, "
            + KEY_NAME + " TEXT, "
            + KEY_INITIALS + " TEXT, "
            + KEY_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgraded DB from version " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public void insertContact(FriendsPostBody.Friend contact) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_ID, contact.getId());
            values.put(KEY_PHONE_NUMBER, contact.getPhoneNumber());
            values.put(KEY_NAME, contact.getName());
            values.put(KEY_INITIALS, contact.getInitials());

            db.insertOrThrow(TABLE_CONTACTS, null, values);
        } catch (SQLiteConstraintException e) {
            Log.e(TAG, "Could not insert into db");
        }
    }

    public void insertAllContacts(FriendsPostBody contacts) {

        ArrayList<FriendsPostBody.Friend> contactList = contacts.getFriends();

        for(int i = 0; i< contactList.size(); i++) {
            insertContact(contactList.get(i));
        }

    }

    public FriendsPostBody getAllFriends() {

        ArrayList<FriendsPostBody.Friend> friends = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS;

        Log.i(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do {
                FriendsPostBody.Friend friend = new FriendsPostBody().new Friend();

                friend.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                friend.setPhoneNumber(c.getString(c.getColumnIndex(KEY_PHONE_NUMBER)));
                friend.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                friend.setInitials(c.getString(c.getColumnIndex(KEY_INITIALS)));

                friends.add(friend);
            } while (c.moveToNext());
        }

        return new FriendsPostBody(friends);
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
