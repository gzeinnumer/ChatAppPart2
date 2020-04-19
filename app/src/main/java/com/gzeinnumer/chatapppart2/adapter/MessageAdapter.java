package com.gzeinnumer.chatapppart2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gzeinnumer.chatapppart2.R;
import com.gzeinnumer.chatapppart2.model.Chat;
import com.gzeinnumer.chatapppart2.model.User;

import java.util.List;

//todo 43
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyHolder> {
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private Context context;
    private List<Chat> mChat;
    private String imageURL;
    private FirebaseUser firebaseUser;

    public MessageAdapter(Context context, List<Chat> chats, String imageURL) {
        this.context = context;
        this.mChat = chats;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new MyHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.bind(context, mChat.get(position), imageURL, position);
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView showMessage;
        ImageView profileImage;
        TextView seen;
        MyHolder(@NonNull View itemView) {
            super(itemView);
            showMessage = itemView.findViewById(R.id.show_message);
            profileImage = itemView.findViewById(R.id.profile_image);
            seen = itemView.findViewById(R.id.txt_seen);
        }

        void bind(Context context, Chat chat, String imageURL, int position) {
            showMessage.setText(chat.getMessage());
            if(imageURL.equals("default")){
                profileImage.setImageResource(R.mipmap.ic_launcher);
            } else {
                Glide.with(context).load(imageURL).into(profileImage);
            }
            if(position == mChat.size()-1){
                if(chat.getIsseen()){
                    seen.setText("Seen");
                } else {
                    seen.setText("Delivered");
                }
            } else {
                seen.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }

    }
}
