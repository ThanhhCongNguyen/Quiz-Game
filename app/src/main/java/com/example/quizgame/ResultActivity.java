package com.example.quizgame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.quizgame.databinding.ActivityResultBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;
    final public int POINTS = 10;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int correctAnswers = getIntent().getIntExtra("correct",0);
        int totalQuestions = getIntent().getIntExtra("total",0);
        String catId = getIntent().getStringExtra("catId");
        String categoryName = getIntent().getStringExtra("categoryName");

        long points = correctAnswers * POINTS;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String formattedDate = dtf.format(now);

        History history = new History("", categoryName, formattedDate, correctAnswers, totalQuestions);

      //  User user = new User("Mai Linh", points);

        binding.score.setText(String.format("%d/%d", correctAnswers, totalQuestions));
        binding.earnedCoins.setText(String.valueOf(points));

//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        database.collection("users")
//                .document(FirebaseAuth.getInstance().getUid())
//                .update("coins", FieldValue.increment(points));

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ResultActivity.this)
                        .setTitle("Save")
                        .setMessage("Do you want to save?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("coins", FieldValue.increment(points));
                                map.put("histories", FieldValue.arrayUnion(history));
                                FirebaseFirestore database = FirebaseFirestore.getInstance();
                                database.collection("users")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .update(map);

//                                database.collection("users")
//                                        .document(FirebaseAuth.getInstance().getUid())
//                                        .collection("History")
//                                        .document(UUID.randomUUID().toString())
//                                        .set(history);

                               // .set(user);
                                Toast.makeText(ResultActivity.this, "Save Successful", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();


            }
        });

        binding.homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResultActivity.this, MainActivity.class));
                finish();
            }
        });


    }
}