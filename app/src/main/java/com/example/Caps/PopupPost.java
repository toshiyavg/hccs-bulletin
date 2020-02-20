package com.example.Caps;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class PopupPost extends AppCompatActivity {

    DatabaseReference ref, iref, pref, cref;
    Comment com;
    EditText comment;
    ImageButton comm, delete;
    RecyclerView rec;
    private Context context;
    LinearLayoutManager ly;
    Context context1 = this;
    Dialog dialog;
    String authimglnk, authname, status, popid;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_post);

        final Intent openIntent = getIntent();
        ref = FirebaseDatabase.getInstance().getReference().child("Comment");
        iref = FirebaseDatabase.getInstance().getReference().child("Users");

        ImageView pimage = findViewById(R.id.pop_image);
        TextView ptitle = findViewById(R.id.pop_title);
        TextView pdetails = findViewById(R.id.pop_details);
        TextView podate = findViewById(R.id.date_txt);
        final TextView pauth = findViewById(R.id.author_txt);
        final CircleImageView authimg = findViewById(R.id.auth_img);
        authimg.setOnClickListener(viewprof);
        comment = findViewById(R.id.comment_edit);
        comm = findViewById(R.id.comment_btn);
        delete = findViewById(R.id.delete_btn);

        dialog = new Dialog(context1);
        dialog.setContentView(R.layout.loading_popup);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        rec = findViewById(R.id.comrec);
        ly = new LinearLayoutManager(this);
        ly.setReverseLayout(true);
        ly.setStackFromEnd(true);
        rec.setLayoutManager(ly);

        final SharedPreferences pref = this.getSharedPreferences("com.example.Caps_login_status", MODE_PRIVATE);
        final String save = pref.getString("idNumberr", null);

        iref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                authimglnk = dataSnapshot.child(openIntent.getStringExtra("authname")).child("imglink").getValue().toString();
                authname = dataSnapshot.child(openIntent.getStringExtra("authname")).child("name").getValue().toString();
                Picasso.get().load(authimglnk).into(authimg);
                pauth.setText(authname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ptitle.setText(openIntent.getStringExtra("ptitle"));
        pdetails.setText(openIntent.getStringExtra("pdetails"));
        podate.setText(openIntent.getStringExtra("postdate"));
        Picasso.get().load(openIntent.getStringExtra("imgurl")).into(pimage);
        popid = openIntent.getStringExtra("authname");

        comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment.getText().toString().equals("")){
                    Toast.makeText(v.getContext(), "Comment box is empty.", Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences spref = getSharedPreferences("com.example.Caps_login_status", MODE_PRIVATE);
                    dialog.show();
                    closeKeyboard();
                    String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                    com = new Comment();
                    ref = FirebaseDatabase.getInstance().getReference().child("Comment").child(openIntent.getStringExtra("postid"));

                    com.setImglink(spref.getString("profpic", null));
                    com.setUcomment(comment.getText().toString());
                    com.setUname(spref.getString("name", null));
                    com.setPid(MyAdapter.iddd);
                    com.setDate(mydate);
                    com.setUid(save);

                    ref.child(String.valueOf(System.currentTimeMillis())).setValue(com);
                    comment.setText("");
                }
            }
        });
        deleteButton();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = FirebaseDatabase.getInstance().getReference("Comment").child(MyAdapter.iddd);
        FirebaseRecyclerAdapter<Comment, CommentAdapter> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Comment, CommentAdapter>(
                Comment.class,
                R.layout.comment_layout,
                CommentAdapter.class,
                query
        ) {
            @Override
            protected void populateViewHolder(CommentAdapter commentAdapter, Comment comment, int i) {
                commentAdapter.setDetails(getBaseContext(), comment.getImglink(), comment.getUname(), comment.getUcomment(), comment.getDate(), comment.getUid());
                dialog.dismiss();
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

    public void deleteButton(){
        SharedPreferences pref = getSharedPreferences("com.example.Caps_login_status", MODE_PRIVATE);
        status = pref.getString("status", null);
        if(status.equals("Student")){
            delete.setVisibility(View.INVISIBLE);
        }
        else{
            delete.setVisibility(View.VISIBLE);
        }
    }
    
    public void deletePost(){
        final Intent openIntent = getIntent();
        pref = FirebaseDatabase.getInstance().getReference("Post").child(openIntent.getStringExtra("postid"));
        pref.removeValue();
        StorageReference photoRef = storage.getReferenceFromUrl(openIntent.getStringExtra("imgurl"));
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
        deleteComment();
        Toast.makeText(getBaseContext(), "Delete Successful", Toast.LENGTH_SHORT).show();
    }

    public void deleteComment(){
        Intent openIntent = getIntent();
        cref = (DatabaseReference) FirebaseDatabase.getInstance().getReference("Comment").child(openIntent.getStringExtra("postid"));
        cref.removeValue();

    }

    View.OnClickListener viewprof = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final SharedPreferences pref = v.getContext().getSharedPreferences("com.example.Caps_login_status", MODE_PRIVATE);
            final String save = pref.getString("idNumberr", null);
            Intent intent = new Intent(v.getContext(), ProfPop.class);
            Intent intent1 = getIntent();
            String s = intent1.getStringExtra("authname");
            if(s.equals(save)){

            }
            else{
                ProfPop.id = popid;
                v.getContext().startActivity(intent);
            }
        }
    };
}
