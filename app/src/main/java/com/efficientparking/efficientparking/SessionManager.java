package com.efficientparking.efficientparking;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    //Session names
    public static final String SESSION_USERSESSION = "userLoginSession";
    public static final String SESSION_REMEMBERMESESSION = "rememberMe";

    private  static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_FULLNAME = "fullName";
    public static final String KEY_TARGA = "targa";
    public static final String KEY_PASSWORD = "password";

    //Remember Me variables
    private static final String IS_REMEMBERME = "IsRememberMe";
    public static final String KEY_SESSIONTARGA = "targa";
    public static final String KEY_SESSIONPASSWORD = "password";



    SessionManager(Context _context, String sessionName){
        context = _context;
        userSession = _context.getSharedPreferences("sessionName",Context.MODE_PRIVATE);
        editor = userSession.edit();
    }


    public void createLoginSession(String fullName, String targa, String password){

        editor.putBoolean(IS_LOGIN,true);

        editor.putString(KEY_FULLNAME,fullName);
        editor.putString(KEY_TARGA,targa);
        editor.putString(KEY_PASSWORD,password);

        editor.commit();
}

    public HashMap<String, String> getUsersDetailFromSession(){
    HashMap<String,String> userData = new HashMap<String, String>();

    userData.put(KEY_FULLNAME, userSession.getString(KEY_FULLNAME, null));
    userData.put(KEY_TARGA, userSession.getString(KEY_TARGA, null));
    userData.put(KEY_PASSWORD, userSession.getString(KEY_PASSWORD, null));

    return userData;
    }

    public boolean checkLogin(){
        if(userSession.getBoolean(IS_LOGIN, true)){
            return true;
        }
        else{
            return  false;
        }
    }

    public void logout(){
        editor.clear();
        editor.commit();
    }

    public void createRememberMeSession(String targa, String password){

        editor.putBoolean(IS_REMEMBERME,true);

        editor.putString(KEY_SESSIONTARGA,targa);
        editor.putString(KEY_SESSIONPASSWORD,password);

        editor.commit();
    }

    public HashMap<String, String> getRememberMeDetailFromSession(){
        HashMap<String,String> userData = new HashMap<String, String>();

        userData.put(KEY_SESSIONTARGA, userSession.getString(KEY_SESSIONTARGA, null));
        userData.put(KEY_SESSIONPASSWORD, userSession.getString(KEY_SESSIONPASSWORD, null));

        return userData;
    }

    public boolean checkRememberMe(){
        if(userSession.getBoolean(IS_REMEMBERME, false)){
            return true;
        }
        else{
            return  false;
        }
    }

}
