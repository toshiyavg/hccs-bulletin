package com.example.Caps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View.*;
import android.view.*;
import android.widget.*;
import android.content.*;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;

public class MainActivity extends AppCompatActivity {
    Button start;
    long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        start = findViewById(R.id.login_btn);
        start.setOnClickListener(clicked);
    }
    OnClickListener clicked  = new OnClickListener() {
        @Override
        public void onClick(View v) {
            updatee();
            SharedPreferences sharedPreferences = getSharedPreferences("com.example.Caps_login_status", MODE_PRIVATE);
            String check = sharedPreferences.getString("login_status", "off");

            if(check.equals("on")){
                Intent intent1 = new Intent(v.getContext(), homeForm.class);
                startActivity(intent1);
                finish();
            }
            else {
                Intent intent1 = new Intent(v.getContext(), loginForm.class);
                startActivity(intent1);
                finish();
            }

        }
    };

    @Override
    public void onBackPressed() {
        if (back_pressed + 1000 > System.currentTimeMillis()){
            finish();
            System.exit(0);
        }
        else{
            Toast.makeText(getBaseContext(),"Press once again to exit!", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

    public  void updatee(){
        AppUpdater appUpdater = new AppUpdater(this).setDisplay(Display.NOTIFICATION).setGitHubUserAndRepo("toshiyavg", "hccs-bulletin").showEvery(5);
        appUpdater.start();
        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(this)
                .setGitHubUserAndRepo("toshiyavg", "hccs-bulletin")
                .withListener(new AppUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(Update update, Boolean isUpdateAvailable) {
                        Log.d("Latest Version", update.getLatestVersion());
                        Log.d("Latest Version Code", String.valueOf(update.getLatestVersionCode()));
                        Log.d("Release notes", update.getReleaseNotes());
                        Log.d("URL", String.valueOf(update.getUrlToDownload()));
                        Log.d("Is update available?", Boolean.toString(isUpdateAvailable));
                    }

                    @Override
                    public void onFailed(AppUpdaterError error) {
                        Log.d("AppUpdater Error", "Something went wrong");
                    }
                });
        appUpdaterUtils.start();
    }

}
