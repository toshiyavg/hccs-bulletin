package com.example.Caps;


import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.*;

import com.example.Caps.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.*;
import com.google.firebase.storage.*;
import com.squareup.picasso.Picasso;


import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    DatabaseReference ref;
    TextView name, course, yr;
    CircleImageView profimg;
    String imgurl;
    Uri imguri, downloadUri, defuri;
    StorageReference storef;
    Button done,cancel, logout;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Dialog dialog;
    public static String userpic, save;
    ImageView profpic;
    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        storef = FirebaseStorage.getInstance().getReference("Profile Picture");

        SharedPreferences pref = getActivity().getSharedPreferences("com.example.Caps_login_status", MODE_PRIVATE);
        save = pref.getString("idNumberr", null);

        done = v.findViewById(R.id.done_btn);
        cancel = v.findViewById(R.id.cancel_btn);
        name = v.findViewById(R.id.name_txt);
        course = v.findViewById(R.id.course_txt);
        profimg = v.findViewById(R.id.prof_pic);
        yr = v.findViewById(R.id.year_txt);
        logout = v.findViewById(R.id.logout_btn);

        loadProfile();

        profimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu  = new PopupMenu(getActivity(), profimg);
                menu.getMenuInflater().inflate(R.menu.prof_menu, menu.getMenu());
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.change_prof){
                            Filechooser();
                            done.setVisibility(View.VISIBLE);
                            cancel.setVisibility(View.VISIBLE);
                        }
                        else{
                            profpic_view.imgurl = imgurl;
                            Intent intent = new Intent(getActivity(), profpic_view.class);
                            startActivity(intent);
                        }
                        return false;
                    }
                });
            }
        });
        done.setOnClickListener(clickdone);
        cancel.setOnClickListener(clickcancel);
        logout.setOnClickListener(logoutclick);

        return v;
    }

    public void loadProfile(){
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(save);
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
                Picasso.get().load(imgurl).into(profimg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.post){
            Fileuploader();
        }
        return super.onOptionsItemSelected(item);
    }

    private void Filechooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private String getExtension(Uri uri){
        ContentResolver cr= getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void Fileuploader(){
        if (imguri != null){
            final StorageReference ref = storef.child(System.currentTimeMillis()+"."+getExtension(imguri));
            ref.putFile(imguri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    SharedPreferences pref = getActivity().getSharedPreferences("com.example.Caps_login_status", MODE_PRIVATE);
                                    deleteImage();
                                    downloadUri = uri;
                                    DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Users");
                                    reff.child(pref.getString("idNumberr", null)).child("imglink").setValue(downloadUri.toString());
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "Upload Success", Toast.LENGTH_SHORT).show();
                                    userpic = downloadUri.toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                        }
                    });
        }
        else{
            Toast.makeText(getActivity(), "this " + imguri.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!= null && data.getData() != null){
            imguri = data.getData();
            profimg.setImageURI(imguri);
        }
    }
    View.OnClickListener clickdone = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog = new Dialog(v.getContext());
            dialog.setContentView(R.layout.loading_popup);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            done.setVisibility(View.INVISIBLE);
            cancel.setVisibility(View.INVISIBLE);
            Fileuploader();
        }
    };

    View.OnClickListener clickcancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            done.setVisibility(View.INVISIBLE);
            cancel.setVisibility(View.INVISIBLE);
            Picasso.get().load(imgurl).into(profimg);
        }
    };

    public void deleteImage(){
        if (imgurl.equals("https://firebasestorage.googleapis.com/v0/b/my-application-ae158.appspot.com/o/Profile%20Picture%2Fdefault_picture.png?alt=media&token=43fbbd9a-cec1-44ae-850c-decf604ecd4e")){

        }
        else{
            StorageReference photoRef = storage.getReferenceFromUrl(imgurl);
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
        }

    }

    View.OnClickListener logoutclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences pref = getActivity().getSharedPreferences("com.example.Caps_login_status", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("login_status", "off");
            editor.commit();
            Intent intent1 = new Intent(v.getContext(), MainActivity.class);
            startActivity(intent1);
        }
    };
}
