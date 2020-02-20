package com.example.Caps;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.ViewHolder{

    View view;
    public ChatAdapter(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }

    public void setDetails(Context c, String chatMessage, String chatSender, String chatTime, String chatId, String chatReciever){
        TextView sender = view.findViewById(R.id.sender_name);
        TextView message = view.findViewById(R.id.message);
        TextView date = view.findViewById(R.id.time);

        sender.setText(chatSender);
        message.setText(chatMessage);
        date.setText(chatTime);

    }
}
