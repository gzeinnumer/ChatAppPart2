package com.gzeinnumer.chatapppart2.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.gzeinnumer.chatapppart2.R;
import com.gzeinnumer.chatapppart2.adapter.UserAdapter;
import com.gzeinnumer.chatapppart2.databinding.FragmentUsersBinding;
import com.gzeinnumer.chatapppart2.model.Chat;
import com.gzeinnumer.chatapppart2.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    public UsersFragment() {
        // Required empty public constructor
    }

    //todo 27
    FragmentUsersBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUsersBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //todo 28
        readUsers();

        //todo 64 part 13 start
        initSearchView();
    }

    //todo 29
    private UserAdapter userAdapter;
    private List<User> mUsers = new ArrayList<>();
    private void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!binding.searchUsers.getText().equals("")){
                    mUsers.clear();
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);

                        if(!user.getId().equals(firebaseUser.getUid())){
                            mUsers.add(user);
                        }
                    }
                    Log.d("MyZein", String.valueOf(mUsers.size()));

                    userAdapter = new UserAdapter(mUsers, true);
                    binding.rvData.setAdapter(userAdapter);
                    binding.rvData.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //todo 65
    private void initSearchView() {
        binding.searchUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(binding.searchUsers.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //todo 66
    private void searchUsers(String s) {
        Log.d("MyZein", s);
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search").startAt(s).endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert firebaseUser != null;
                    if(!user.getId().equals(firebaseUser.getUid())){
                        mUsers.add(user);
                    }
                }

                userAdapter = new UserAdapter(mUsers, true);
                binding.rvData.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
