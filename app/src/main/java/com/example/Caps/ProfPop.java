package com.example.Caps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfPop extends AppCompatActivity {

    CircleImageView profpic;
    TextView name, course, yr;
    Button message;
    DatabaseReference ref;
    String imgurl;
    public static String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_prof_pop);
        profpic = findViewById(R.id.prof_pic);
        name = findViewById(R.id.name_txt1);
        course = findViewById(R.id.course_txt1);
        yr = findViewById(R.id.year_txt1);
        message = findViewById(R.id.message_btn);

        loadProfile();

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(), Chat.class);
                intent1.putExtra("recieveId", id);
                startActivity(intent1);
            }
        });
    }

    public void  loadProfile(){
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imgurl = dataSnapshot.child("imglink").getValue().toString();
                name.setText(dataSnapshot.child("name").getValue().toString());
                course.setText(dataSnapshot.child("course").getValue().toString());
                switch (dataSnapshot.child("year").getValue().toString()){
                    case "1":
                        yr.setText(dataSnapshot.child("year").getValue().toString() + "st Year");
                        break;
                    case "2":
                        yr.setText(dataSnapshot.child("year").getValue().toString() + "nd Year");
                        break;
                    case "3":
                        yr.setText(dataSnapshot.child("year").getValue().toString() + "rd Year");
                        break;
                    case "4":
                        yr.setText(dataSnapshot.child("year").getValue().toString() + "th Year");
                        break;
                }
                Picasso.get().load(imgurl).into(profpic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
