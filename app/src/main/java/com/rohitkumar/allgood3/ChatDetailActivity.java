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
import com.rohitkumar.allgood3.databinding.ActivityChatDetailBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate((getLayoutInflater()));
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        final String senderId = auth.getUid();
        String receiveId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");
        binding.userName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.man).into(binding.profileImage);

        binding.backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
            startActivity(intent);
        });

        final ArrayList<MessageModel> messageModels = new ArrayList<>();

        final ChatAdapter chatAdapter = new ChatAdapter(messageModels, this,receiveId);
        binding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);


        final String senderRoom = senderId + receiveId;
        final String receiverRoom = receiveId + senderId;

        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            MessageModel model = snapshot1.getValue(MessageModel.class);
                            assert model != null;
                            model.setMessageId(snapshot1.getKey());
                            messageModels.add(model);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        binding.send.setOnClickListener(v -> {

            String message = binding.etMessage.getText().toString();
            final MessageModel model = new MessageModel(senderId, message);
            model.setTimestamp(new Date().getTime());
            binding.etMessage.setText("");
            database.getReference().child("chats").child(senderRoom)
                    .push()
                    .setValue(model).addOnSuccessListener(unused -> database.getReference().child("chats")
                            .child(receiverRoom)
                            .push()
                            .setValue(model).addOnSuccessListener(unused1 -> {
                            }));

        });

    }
}