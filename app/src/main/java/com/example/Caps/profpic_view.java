package com.example.Caps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class profpic_view extends AppCompatActivity {

    public static String imgurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profpic_view);

        ImageView profimg = findViewById(R.id.prof);
        Picasso.get().load(imgurl).into(profimg);
    }
}
