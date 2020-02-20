package com.example.Caps;


import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.View.*;
import android.view.*;
import android.webkit.MimeTypeMap;
import android.widget.*;

import com.example.Caps.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.widget.Toast;


import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Post extends Fragment {

    public Uri imguri, downloadUri;
    StorageReference storef;
    ImageView imgg;
    EditText tit,det;
    Spinner combo;
    DatabaseReference ref;
    Poster pst;
    Dialog dialog;
    ImageButton btn;
    public Post() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_post, container, false);
        storef = FirebaseStorage.getInstance().getReference("Posts");

        btn = v.findViewById(R.id.imgbtn);
        imgg = v.findViewById(R.id.img);
        tit = v.findViewById(R.id.title_txt);
        det = v.findViewById(R.id.details_txt);

        Toolbar tool = v.findViewById(R.id.tbb);
        ((AppCompatActivity)getActivity()).setSupportActionBar(tool);
        setHasOptionsMenu(true);


        SharedPreferences pref = getActivity().getSharedPreferences("com.example.Caps_login_status", MODE_PRIVATE);

        combo = v.findViewById(R.id.spinner);
        String items[] = new String[] {"General", "College", pref.getString("coursesave", null)};
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, 0, items);
        combo.setAdapter(itemAdapter);

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Filechooser();
            }
        });
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.post_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.post){
            dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.loading_popup);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
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
                                    downloadUri = uri;
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "Upload Success", Toast.LENGTH_SHORT).show();
                                    getPost();
                                    Bulletin bullet = new Bulletin();
                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.framelay, bullet);
                                    fragmentTransaction.commit();
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
            imgg.setImageURI(imguri);
        }
    }

    public  void getPost(){
        SharedPreferences pref = getActivity().getSharedPreferences("com.example.Caps_login_status", MODE_PRIVATE);
        String save = pref.getString("idNumberr", null);
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        pst = new Poster();
        ref = FirebaseDatabase.getInstance().getReference().child("Post");

                pst.setTitle(tit.getText().toString());
                pst.setDetails(det.getText().toString());
                pst.setType(combo.getSelectedItem().toString());
                pst.setDate(mydate);
                pst.setPostid(String.valueOf(System.currentTimeMillis()));
                pst.setImglink(downloadUri.toString());
                pst.setAuth(save);

                ref.child(String.valueOf(System.currentTimeMillis())).setValue(pst);
                Toast.makeText(getActivity(), "Post Done", Toast.LENGTH_SHORT).show();
    }


}
