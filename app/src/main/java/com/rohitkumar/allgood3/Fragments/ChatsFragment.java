package com.rohitkumar.allgood3.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rohitkumar.allgood3.Adapters.UsersAdapter;
import com.rohitkumar.allgood3.Models.Users;
import com.rohitkumar.allgood3.databinding.FragmentChatsBinding;

import java.util.ArrayList;


public class ChatsFragment extends Fragment {



    public ChatsFragment() {
        // Required empty public constructor
    }


    FragmentChatsBinding binding;
    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase database;





    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();


        UsersAdapter adapter = new UsersAdapter(list, getContext());
        binding.chatRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Users users = dataSnapshot.getValue(Users.class);


                    assert users != null;
                    users.setUserId(dataSnapshot.getKey());

                    if(!users.getUserId().equals(FirebaseAuth.getInstance().getUid())){
                        list.add(users);
                    }




                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return binding.getRoot();

    }
}