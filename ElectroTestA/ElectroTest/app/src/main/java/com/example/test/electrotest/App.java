package com.example.test.electrotest;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.example.test.electrotest.Activities.MainActivity;
import com.example.test.electrotest.ServerInteract.APIService;
import com.example.test.electrotest.ServerInteract.AccountInteract;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/// Дефолт приложение при запускеD
public class App extends Application {

    private static APIService apiService;
    private Retrofit retrofit;
    private static String preferenceName = "ElectroTestShPref";
    private static String preferenceLogin = "login";
    private static String preferencePassword = "password";
    private static String preferenceImage = "image";
    private static String imagePath = "img.png";

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.137.1:5000/Home/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        apiService = retrofit.create(APIService.class);
    }

    public static void SetLoginPasswordShPref(String login, String password, AppCompatActivity activity) {
        SharedPreferences preferences = activity.getApplicationContext().getSharedPreferences(preferenceName, 0);
        SharedPreferences.Editor editor = preferences.edit();
        String loginPref = preferences.getString(preferenceLogin, "");
        String passwordPref = preferences.getString(preferencePassword, "");
        if (!loginPref.equals(""))
            editor.remove(preferenceLogin);
        if (!passwordPref.equals(""))
            editor.remove(preferencePassword);
        editor.putString(preferenceLogin, login);
        editor.putString(preferencePassword, password);
        editor.commit();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static void SaveImages(Bitmap path, Activity activity) {
        File pathFile = activity.getFilesDir();
        File dir = new File(pathFile, imagePath);
        try (OutputStream out = new FileOutputStream(dir)) {
            path.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap GetActualBitmap(Activity activity) {
        SharedPreferences preferences = activity.getApplicationContext().getSharedPreferences(preferenceName, 0);
        String image = preferences.getString(preferenceImage, "");
        Bitmap bitmap;
        if (image == "")
            bitmap = BitmapFactory.decodeResource(activity.getApplicationContext().getResources(),
                    R.drawable.user);
        else {
            File path = new File(activity.getFilesDir(), imagePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeFile(path.getPath(), options);
        }
        return bitmap;
    }

    public static void SetImage(Bitmap path, Activity activity) {
        SharedPreferences preferences = activity.getApplicationContext().getSharedPreferences(preferenceName, 0);
        SharedPreferences.Editor editor = preferences.edit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SaveImages(path, activity);
        }
        String image = preferences.getString(preferenceImage, "");
        if (!image.equals(""))
            editor.remove(preferenceImage);
        editor.putString(preferenceImage, imagePath);
        editor.commit();
    }

    public static void RemoveLoginInfo(Activity mainActivity) {
        SharedPreferences preferences = mainActivity.getApplicationContext().getSharedPreferences(preferenceName, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(preferenceLogin);
        editor.remove(preferencePassword);
        editor.remove(preferenceImage);
        editor.commit();
    }

    public static void TryToLoginStart(MainActivity mainActivity) {
        SharedPreferences preferences = mainActivity.getApplicationContext().getSharedPreferences(preferenceName, 0);
        String login = preferences.getString(preferenceLogin, "");
        String password = preferences.getString(preferencePassword, "");
        if (login.equals("") || password.equals(""))
            return;
        ((EditText) mainActivity.findViewById(R.id.textBoxLogin)).setText(login);
        ((EditText) mainActivity.findViewById(R.id.textBoxPassword)).setText(password);
        new AccountInteract((TextView) mainActivity.findViewById(R.id.textViewInfo)).login(login, password, mainActivity);
    }

    public static APIService getApi() {
        return apiService;
    }
}
