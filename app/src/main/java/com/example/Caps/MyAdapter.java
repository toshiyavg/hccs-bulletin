package com.example.Caps;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;


public class MyAdapter extends RecyclerView.ViewHolder{

    View mView;
    Dialog dia;
    public static String iddd;

    public MyAdapter(@NonNull View itemView) {
        super(itemView);

        mView = itemView;
    }

    public void setDetails(Context c, final String title, String auth, String date, String type,
                            final String imglink, final String postid, final String details){
        final ImageView img1 = itemView.findViewById(R.id.post_img);
        final TextView title1 = itemView.findViewById(R.id.title_txt);
        final TextView auth1 = itemView.findViewById(R.id.author_txt);
        final TextView date1 = itemView.findViewById(R.id.postdate_txt);
        TextView type1 = itemView.findViewById(R.id.type_txt);

        title1.setText(title);
        auth1.setText(auth);
        date1.setText(date);
        type1.setText(type);
        Picasso.get().load(imglink).into(img1);

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iddd = postid;
                Intent intent = new Intent(v.getContext(), PopupPost.class);
                intent.putExtra("imgurl", imglink);
                intent.putExtra("ptitle", title1.getText().toString());
                intent.putExtra("pdetails", details);
                intent.putExtra("postid", iddd);
                intent.putExtra("postdate", date1.getText().toString());
                intent.putExtra("authname", auth1.getText().toString());
                Toast.makeText(v.getContext(), "" + iddd, Toast.LENGTH_SHORT).show();
                v.getContext().startActivity(intent);
            }
        });
    }
}
