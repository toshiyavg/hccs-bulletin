package com.example.Caps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Chat extends AppCompatActivity {
    EditText chatMessage;
    ImageButton send;
    String recieveID;
    DatabaseReference sref, rref, fref, lref;
    Toolbar toolbra;
    RecyclerView rec;
    final MyChat chat = new MyChat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbra = (Toolbar) findViewById(R.id.tools);
        this.setSupportActionBar(toolbra);

        chatMessage = findViewById(R.id.chat_edit);
        send = findViewById(R.id.send_btn);
        rec = findViewById(R.id.chatrec);


        SharedPreferences pref = this.getSharedPreferences("com.example.Caps_login_status", MODE_PRIVATE);
        final String save = pref.getString("idNumberr", null);
        Intent intent = getIntent();
        recieveID = intent.getStringExtra("recieveId");

        LinearLayoutManager ly = new LinearLayoutManager(this);
        rec.setLayoutManager(ly);

        toolbra.setTitle(recieveID);
        sref = FirebaseDatabase.getInstance().getReference().child("Chat").child(save).child(save + "_" + recieveID);
        rref = FirebaseDatabase.getInstance().getReference().child("Chat").child(recieveID).child(recieveID + "_" + save);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chatMessage.getText().toString().equals("")){
                    Toast.makeText(v.getContext(), "Message box is empty.", Toast.LENGTH_SHORT).show();
                }
                else{
                    String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()).trim();

                    chat.setChatId(String.valueOf(System.currentTimeMillis()));
                    chat.setChatMessage(chatMessage.getText().toString());
                    chat.setChatReceiver(recieveID);
                    chat.setChatSender(save);
                    chat.setChatTime(mydate);

                    sref.child(String.valueOf(System.currentTimeMillis())).setValue(chat);
                    rref.child(String.valueOf(System.currentTimeMillis())).setValue(chat);

                    chatMessage.setText("");
                    closeKeyboard();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        final SharedPreferences pref = this.getSharedPreferences("com.example.Caps_login_status", MODE_PRIVATE);
        final String save = pref.getString("idNumberr", null);
        Intent intent = getIntent();
        recieveID = intent.getStringExtra("recieveId");
        fref = FirebaseDatabase.getInstance().getReference("Chat").child(save).child(save + "_" + recieveID);
        FirebaseRecyclerAdapter<MyChat, ChatAdapter> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MyChat, ChatAdapter>(
                MyChat.class,
                R.layout.chat_rec,
                ChatAdapter.class,
                fref
        ) {
            @Override
            protected void populateViewHolder(ChatAdapter chatAdapter, MyChat myChat, int i) {
                chatAdapter.setDetails(getBaseContext(), myChat.getChatMessage(), myChat.getChatSender(), myChat.getChatTime(), myChat.getChatId(), myChat.getChatReceiver());
            }
        };
        rec.setAdapter(firebaseRecyclerAdapter);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
