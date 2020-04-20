package com.gzeinnumer.chatapppart2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gzeinnumer.chatapppart2.MessageActivity;
import com.gzeinnumer.chatapppart2.R;
import com.gzeinnumer.chatapppart2.databinding.UserItemBinding;
import com.gzeinnumer.chatapppart2.model.Chat;
import com.gzeinnumer.chatapppart2.model.User;

import java.util.List;

//todo 26
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyHolder> {
    private Context context;
    private List<User> listUser;
    private boolean isChat;

    public UserAdapter(List<User> listUser, boolean isChat) {
        this.listUser = listUser;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserItemBinding binding = UserItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        context = parent.getContext();
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.bind(context, listUser.get(position), isChat);
        //todo 73 part 17 start
        if (isChat){
            lastMessage(listUser.get(position).getId(), holder.binding.lastMsg);
        } else {
            holder.binding.lastMsg.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        UserItemBinding binding;

        MyHolder(@NonNull UserItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void bind(final Context context, final User user, boolean isChat) {
            binding.username.setText(user.getUsername());
            if (user.getImageURL().equals("default")) {
                binding.profileImage.setImageResource(R.mipmap.ic_launcher);
            } else {
                Glide.with(context).load(user.getImageURL()).into(binding.profileImage);
            }

            if(isChat){
                if(user.getStatus().equals("online")){
                    binding.imgOn.setVisibility(View.VISIBLE);
                    binding.imgOff.setVisibility(View.GONE);
                } else {
                    binding.imgOn.setVisibility(View.GONE);
                    binding.imgOff.setVisibility(View.VISIBLE);
                }
            } else {
                binding.imgOn.setVisibility(View.GONE);
                binding.imgOff.setVisibility(View.INVISIBLE);
            }

            //todo 32
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MessageActivity.class);
                    intent.putExtra("userId", user.getId());
                    context.startActivity(intent);
                }
            });
            //end todo 32
        }

    }

    //todo 74
    String theLastMessage;
    private void lastMessage(final String userId, final TextView lastMsg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    assert firebaseUser != null;
                    assert chat != null;
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId) ||
                            chat.getReceiver().equals(userId) && chat.getSender().equals(firebaseUser.getUid())){
                        theLastMessage = chat.getMessage();
                    }
                }

                if ("default".equals(theLastMessage)) {
                    lastMsg.setText("No Mesaage");
                } else {
                    lastMsg.setText(theLastMessage);
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
