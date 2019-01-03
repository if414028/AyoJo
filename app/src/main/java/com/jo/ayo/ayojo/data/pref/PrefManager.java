package com.jo.ayo.ayojo.data.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.jo.ayo.ayojo.data.model.PostData;
import com.jo.ayo.ayojo.data.model.Token;

public class PrefManager {

    private static final String MyPREF = "MyPref";
    private static final String HOUSEOWNERKEY = "HouseOwnerKey";
    private static final String FIRSTANSWERKEY = "FirstAnswerKey";
    private static final String SECONDANSWERKEY = "SecondAnswerKey";
    private static final String LATKEY = "LatKey";
    private static final String LNGKEY = "LngKey";
    private static final String TOKENKEY = "TokenKey";

    Context _context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public PrefManager(Context context) {
        this._context = context;
        sharedPreferences = _context.getSharedPreferences(MyPREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static void setHouseOwner(Context context, PostData postData) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(HOUSEOWNERKEY, postData.getHouseOwner());
        editor.putFloat(LATKEY, postData.getLat());
        editor.putFloat(LNGKEY, postData.getLng());
        editor.commit();
    }

    public static void setFirstanswer(Context context, PostData postData) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(FIRSTANSWERKEY, postData.getFirstAnsewr());
        editor.commit();
    }

    public static void setSecondanswerkey(Context _context, PostData postData) {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(MyPREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SECONDANSWERKEY, postData.getSecondAnswer());
        editor.commit();
    }

    public static void setToken(Context context, Token tokenData) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TOKENKEY, tokenData.getToken());
        editor.commit();
    }

    public static PostData getPostData(Context context) {
        PostData postData = new PostData();

        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPREF, Context.MODE_PRIVATE);

        postData.setHouseOwner(sharedPreferences.getString(HOUSEOWNERKEY, ""));
        postData.setLat(sharedPreferences.getFloat(LATKEY, 0.0f));
        postData.setLng(sharedPreferences.getFloat(LNGKEY, 0.0f));
        postData.setFirstAnsewr(sharedPreferences.getString(FIRSTANSWERKEY, ""));
        postData.setSecondAnswer(sharedPreferences.getString(SECONDANSWERKEY, ""));

        return postData;
    }

    public static Token getTokenData(Context context) {
        Token tokenData = new Token();

        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPREF, Context.MODE_PRIVATE);

        tokenData.setToken(sharedPreferences.getString(TOKENKEY, ""));

        return tokenData;
    }

}
