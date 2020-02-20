package com.example.Caps;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.ColorSpace;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.*;

import com.example.Caps.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Bulletin extends Fragment {


    public Bulletin() {
        // Required empty public constructor
    }
    DatabaseReference ref;
    Query sref;
    RecyclerView rec;
    LinearLayoutManager ly;
    SearchView serch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_bulletin, container, false);
        Toolbar tool = v.findViewById(R.id.toolbarr);
        ((AppCompatActivity)getActivity()).setSupportActionBar(tool);
        setHasOptionsMenu(true);
        serch = v.findViewById(R.id.search);

        rec = v.findViewById(R.id.recycler);
        ly = new LinearLayoutManager(getActivity());
        ly.setReverseLayout(true);
        ly.setStackFromEnd(true);
        rec.setLayoutManager(ly);

        ref = FirebaseDatabase.getInstance().getReference("Post");

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tool_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search){
            try{
                serch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });

            }catch (NullPointerException e){

            }
        }
        else if(id == R.id.sgeneral){
            Query query = FirebaseDatabase.getInstance().getReference("Post").orderByChild("type").equalTo("General");
            FirebaseRecyclerAdapter<Poster, MyAdapter> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Poster, MyAdapter>(
                    Poster.class,
                    R.layout.recycler_layout,
                    MyAdapter.class,
                    query
            ) {
                @Override
                protected void populateViewHolder(MyAdapter myAdapter, Poster poster, int i) {
                    myAdapter.setDetails(getContext(), poster.getTitle(), poster.getAuth(), poster.getDate(), poster.getType(), poster.getImglink(), poster.getPostid(), poster.getDetails());
                }
            };
            rec.setAdapter(firebaseRecyclerAdapter);
        }
        else if(id == R.id.scollege){
            Query query = FirebaseDatabase.getInstance().getReference("Post").orderByChild("type").equalTo("College");
            FirebaseRecyclerAdapter<Poster, MyAdapter> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Poster, MyAdapter>(
                    Poster.class,
                    R.layout.recycler_layout,
                    MyAdapter.class,
                    query
            ) {
                @Override
                protected void populateViewHolder(MyAdapter myAdapter, Poster poster, int i) {
                    myAdapter.setDetails(getContext(), poster.getTitle(), poster.getAuth(), poster.getDate(), poster.getType(), poster.getImglink(), poster.getPostid(), poster.getDetails());
                }
            };

            rec.setAdapter(firebaseRecyclerAdapter);
        }
        else if(id == R.id.sdepartment){
            SharedPreferences pref = getActivity().getSharedPreferences("com.example.Caps_login_status", MODE_PRIVATE);
            Query query = FirebaseDatabase.getInstance().getReference("Post").orderByChild("type").equalTo(pref.getString("coursesave", null));
            FirebaseRecyclerAdapter<Poster, MyAdapter> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Poster, MyAdapter>(
                    Poster.class,
                    R.layout.recycler_layout,
                    MyAdapter.class,
                    query
            ) {
                @Override
                protected void populateViewHolder(MyAdapter myAdapter, Poster poster, int i) {
                    myAdapter.setDetails(getContext(), poster.getTitle(), poster.getAuth(), poster.getDate(), poster.getType(), poster.getImglink(), poster.getPostid(), poster.getDetails());
                }
            };
            rec.setAdapter(firebaseRecyclerAdapter);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = FirebaseDatabase.getInstance().getReference("Post").orderByChild("type").equalTo("General");
        FirebaseRecyclerAdapter<Poster, MyAdapter> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Poster, MyAdapter>(
                Poster.class,
                R.layout.recycler_layout,
                MyAdapter.class,
                query
        ) {
            @Override
            protected void populateViewHolder(MyAdapter myAdapter, Poster poster, int i) {
                myAdapter.setDetails(getContext(), poster.getTitle(), poster.getAuth(), poster.getDate(), poster.getType(), poster.getImglink(), poster.getPostid(), poster.getDetails());
            }
        };
        rec.setAdapter(firebaseRecyclerAdapter);
    }
}
