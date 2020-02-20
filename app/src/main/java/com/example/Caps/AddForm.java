package com.example.Caps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.view.View.*;
import com.google.firebase.database.*;

public class AddForm extends AppCompatActivity {

    EditText firstname, lastname, idnumber, year, pass;
    Spinner course;
    Button done;
    DatabaseReference ref;
    Member member;
    ToggleButton status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_form);

        idnumber = findViewById(R.id.id_txt1);
        firstname = findViewById(R.id.first_txt);
        lastname = findViewById(R.id.last_txt);
        year = findViewById(R.id.year_txt);
        done = findViewById(R.id.done_btn);
        pass = findViewById(R.id.pass_txt);
        status = findViewById(R.id.toggleButton);

        course = findViewById(R.id.spinner2);
        String items[] = new String[] {"BSBA", "BSED", "BSHRM", "BST", "BSIT"};
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, 0, items);
        course.setAdapter(itemAdapter);

        member = new Member();
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = Long.parseLong(idnumber.getText().toString());
                String name = firstname.getText().toString() + " " + lastname.getText().toString();
                int yr = Integer.parseInt(year.getText().toString());


                member.setIdNumber(id);
                member.setPassword(pass.getText().toString());
                member.setName(name);
                member.setCourse(course.getSelectedItem().toString());
                member.setYear(yr);
                member.setImglink("https://firebasestorage.googleapis.com/v0/b/my-application-ae158.appspot.com/o/Profile%20Picture%2Fdefault_picture.png?alt=media&token=43fbbd9a-cec1-44ae-850c-decf604ecd4e");
                member.setStatus(status.getText().toString());

                ref.child(String.valueOf(id)).setValue(member);
                Toast.makeText(v.getContext(), "Successful", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
