package com.example.quizgame.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.example.quizgame.model.User;
import com.example.quizgame.adapter.HistoryAdapter;
import com.example.quizgame.databinding.ActivityHistoryBinding;
import com.example.quizgame.model.History;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    ActivityHistoryBinding binding;
    ArrayList<History> histories;
    HistoryAdapter historyAdapter;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.historyToolbar);

        database = FirebaseFirestore.getInstance();

        histories = new ArrayList<>();
        historyAdapter = new HistoryAdapter(this, histories);

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            if(snapshot.exists()){
                                User user = snapshot.toObject(User.class);
                                histories.addAll(user.getHistories());
                                binding.recyclerviewLslb.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
                                binding.recyclerviewLslb.setAdapter(historyAdapter);

                            }else {
                                Log.d("TAG", "No such document", task.getException());
                            }
                        }else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });




    }
}