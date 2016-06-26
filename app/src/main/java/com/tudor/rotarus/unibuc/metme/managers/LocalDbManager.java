package com.tudor.rotarus.unibuc.metme.managers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tudor.rotarus.unibuc.metme.db.DatabaseHelper;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.db.FriendsDbListener;
import com.tudor.rotarus.unibuc.metme.pojos.responses.post.FriendsPostBody;

/**
 * Created by Tudor on 21.04.2016.
 */
public class LocalDbManager {

    private final String TAG = getClass().getSimpleName();

    private static LocalDbManager instance;

    private LocalDbManager() {

    }

    public static LocalDbManager getInstance() {
        if(instance == null) {
            instance = new LocalDbManager();
        }
        return instance;
    }

    public void getFriends(Context context, FriendsDbListener callback) {

        new FriendsTask(context, callback).execute();

    }

    private class FriendsTask extends AsyncTask<Void, Void, FriendsPostBody> {

        private Context context;
        private DatabaseHelper db;
        private FriendsDbListener callback;

        public FriendsTask(Context context, FriendsDbListener callback) {
            this.context = context;
            this.callback = callback;
        }

        @Override
        protected FriendsPostBody doInBackground(Void... params) {

            db = new DatabaseHelper(context);
            return db.getAllFriends();
        }

        @Override
        protected void onPostExecute(FriendsPostBody friendsPostBody) {
            super.onPostExecute(friendsPostBody);

            if(friendsPostBody != null) {
                callback.onFriendsDbGetSuccess(friendsPostBody);
            } else {
                Log.e(TAG, "Null response when getting all friends from local DB");
                callback.onFriendsDbGetFailed();
            }

            db.close();
        }
    }

}
