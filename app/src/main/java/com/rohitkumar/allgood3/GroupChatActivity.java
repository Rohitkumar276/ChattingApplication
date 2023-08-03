package com.rohitkumar.allgood3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rohitkumar.allgood3.Adapters.ChatAdapter;
import com.rohitkumar.allgood3.Models.MessageModel;
import com.rohitkumar.allgood3.databinding.ActivityGroupChatBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class GroupChatActivity extends AppCompatActivity {
    ActivityGroupChatBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.backArrow.setOnClickListener(view -> {
            Intent intent = new Intent(GroupChatActivity.this, MainActivity.class);
            startActivity(intent);
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final String senderId = FirebaseAuth.getInstance().getUid();
        binding.userName.setText("Global Chat");

        final ChatAdapter adapter = new ChatAdapter(messageModels, this);
        binding.chatRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        database.getReference().child("Group Chat")
                        .addValueEventListener(new ValueEventListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                messageModels.clear();
                                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                    MessageModel model = dataSnapshot.getValue(MessageModel.class);
                                    messageModels.add(model);

                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
        binding.send.setOnClickListener(view -> {
            final String message = binding.etMessage.getText().toString();
            final MessageModel model = new MessageModel(senderId,message);
            model.setTimestamp(new Date().getTime());
            binding.etMessage.setText("");
            database.getReference().child("Group Chat")
                    .push()
                    .setValue(model).addOnSuccessListener(unused -> {

                    });
        });
    }
}