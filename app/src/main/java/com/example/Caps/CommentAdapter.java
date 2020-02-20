package com.example.Caps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class CommentAdapter extends RecyclerView.ViewHolder {

    View cView;

    public CommentAdapter(@NonNull View itemView) {
        super(itemView);
        cView = itemView;
    }

    public void setDetails(final Context c, String imglink, String userName, String userComm, String datetime, final String uid){
        CircleImageView img = cView.findViewById(R.id.com_pic);
        final TextView user = cView.findViewById(R.id.user_txt);
        TextView comm = cView.findViewById(R.id.comment_txt);
        TextView date = cView.findViewById(R.id.time_txt);

        Picasso.get().load(imglink).into(img);
        user.setText(userName);
        comm.setText(userComm);
        date.setText(datetime);
        final String id = uid;

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences pref = v.getContext().getSharedPreferences("com.example.Caps_login_status", MODE_PRIVATE);
                final String save = pref.getString("idNumberr", null);
                Intent intent = new Intent(v.getContext(), ProfPop.class);
                if(save.equals(id)){

                }
                else{
                    ProfPop.id = id;
                    v.getContext().startActivity(intent);
                }
            }
        });
    }
}
