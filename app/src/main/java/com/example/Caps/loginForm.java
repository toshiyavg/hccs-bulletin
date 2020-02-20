package com.example.Caps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.View.*;
import android.view.*;
import android.widget.*;
import com.google.firebase.database.*;

public class loginForm extends AppCompatActivity {

    Button login;
    EditText idNumber, pass;
    String password="admin", id="0000", statuss;
    DatabaseReference ref;
    public static String idd, coursee, name, imgg;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        /**FirebaseDatabase.getInstance().setPersistenceEnabled(true);**/

        idNumber = findViewById(R.id.id_txt1);
        pass = findViewById(R.id.pass_txt1);


        login = findViewById(R.id.login_btn);
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.loading_popup);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                idd = idNumber.getText().toString();


                if ((id.equals(idNumber.getText().toString())) && (password.equals(pass.getText().toString()))){
                    Intent intent1 = new Intent(v.getContext(), AddForm.class);
                    startActivity(intent1);
                    finish();
                }
                else{
                    ref = FirebaseDatabase.getInstance().getReference("Users").child(idNumber.getText().toString());
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String passWord = dataSnapshot.child("password").getValue().toString();
                                coursee = dataSnapshot.child("course").getValue().toString();
                                name = dataSnapshot.child("name").getValue().toString();
                                imgg = dataSnapshot.child("imglink").getValue().toString();
                                statuss = dataSnapshot.child("status").getValue().toString();
                                if (passWord.equals(pass.getText().toString())){
                                    Intent intent1 = new Intent(v.getContext(), homeForm.class);
                                    dialog.dismiss();
                                    Store();
                                    startActivity(intent1);
                                    finish();
                                }
                                else{
                                        dialog.dismiss();
                                        Toast.makeText(v.getContext(), "Invalid ID or Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                dialog.dismiss();
                                Toast.makeText(v.getContext(), "Invalid ID or Password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            dialog.dismiss();
                            Toast.makeText(v.getContext(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void Store(){
        SharedPreferences pref = getSharedPreferences("com.example.Caps_login_status", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("login_status", "on");
        editor.putString("idNumberr", idd);
        editor.putString("coursesave", coursee);
        editor.putString("status", statuss);
        editor.putString("name", name);
        editor.putString("profpic", imgg);
        editor.commit();
    }
}
