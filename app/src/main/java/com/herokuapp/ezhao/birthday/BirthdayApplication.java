package com.herokuapp.ezhao.birthday;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class BirthdayApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "c9b3kviRzkvktRQqbuim7CX8AacgFhwe6BvjA2rn", "iSRw0VXJYFQQy2oXK6nbLX7eWIsJcrAduAtTb97J");
    }
}
