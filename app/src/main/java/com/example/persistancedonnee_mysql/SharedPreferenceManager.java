package com.example.persistancedonnee_mysql;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SharedPreferenceManager {
    private static SharedPreferenceManager m_instance;
    private static Context m_ctx;

    private final static String SharedPrefName = "MyPrefs";
    private final static String keyUsername = "USER_USERNAME";
    private final static String keyEmail = "USER_EMAIL";
    private final static String keyBirthdate = "USER_BIRTHDATE";
    private final static String keyLocality = "USER_LOCALITY";

    private SharedPreferenceManager(Context ctx){
        m_ctx = ctx;
    }

    public static synchronized SharedPreferenceManager getInstance(Context ctx) {
        if (m_instance == null) m_instance = new SharedPreferenceManager(ctx);
        return m_instance;
    }

    //ACCESSORS
    public String getUsername(){
        SharedPreferences MySharedPreferences = m_ctx.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);
        return MySharedPreferences.getString(keyUsername, "");//should return username
    }
    public String getUserEmail(){
        SharedPreferences MySharedPreferences = m_ctx.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);
        return MySharedPreferences.getString(keyEmail, "");//should return Email
    }
    public String getUserBirthdate(){
        SharedPreferences MySharedPreferences = m_ctx.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);
        return MySharedPreferences.getString(keyBirthdate, ""); //should return birthdate
    }
    public String getUserLocality(){
        SharedPreferences MySharedPreferences = m_ctx.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);
        return MySharedPreferences.getString(keyLocality, ""); //should return locality
    }

    //a la connexion on écrit dans les shared preferences
    public boolean userLogin(String username, String email, String birthdate, String locality){
        SharedPreferences MySharedPreferences = m_ctx.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor MyEditor = MySharedPreferences.edit();

        MyEditor.putString(keyEmail, email);
        MyEditor.putString(keyUsername, username);
        MyEditor.putString(keyBirthdate, birthdate);
        MyEditor.putString(keyLocality, locality);
        MyEditor.apply();

        return true;
    }

    //utilisateur log ou non ?
    //lire les shared preferences
    public boolean isLoggedIn(){
        SharedPreferences MySharedPreferences = m_ctx.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);
        return !MySharedPreferences.getString(keyUsername, "").equals("");
    }

    //clear des shared preferences
    public boolean logout(Context ctx){
        SharedPreferences MySharedPreferences = m_ctx.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor MyEditor = MySharedPreferences.edit();

        MyEditor.clear();
        MyEditor.apply();

        Intent finishAndBackToLoginScreen = new Intent(ctx, MainActivity.class);
        ctx.startActivity(finishAndBackToLoginScreen);
        return true;
    }

    public boolean logout(Context ctx, String logoutMessage){
        SharedPreferences MySharedPreferences = m_ctx.getSharedPreferences(SharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor MyEditor = MySharedPreferences.edit();

        MyEditor.clear();
        MyEditor.apply();

        Intent finishAndBackToLoginScreen = new Intent(ctx, MainActivity.class);
        finishAndBackToLoginScreen.putExtra("EXTRA_LOGOUTMESSAGE", logoutMessage);
        ctx.startActivity(finishAndBackToLoginScreen);
        return true;
    }
}
