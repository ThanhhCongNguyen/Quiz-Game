package com.example.quizgame.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.quizgame.R;
import com.example.quizgame.databinding.ActivityLuckyNumberBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class LuckyNumberActivity extends AppCompatActivity {

    ActivityLuckyNumberBinding binding;
    Random random = new Random();
    int luckyNumber = 0;
    long userCoins;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLuckyNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarLuckyAct);
        binding.toolbarLuckyAct.setTitleTextColor(getResources().getColor(R.color.color_white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        database = FirebaseFirestore.getInstance();
        getUserCoins();


    }

    private void onClickedContinue(){
        binding.continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.luckyBox.setVisibility(View.VISIBLE);
                binding.congratsText.setVisibility(View.GONE);
                binding.continueButton.setVisibility(View.GONE);
                onClickedImage();
            }
        });
    }

    private void onClickedImage(){
        luckyNumber = random.nextInt(50) + 50;
        binding.luckyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShakeImage();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.luckyBox.setVisibility(View.GONE);
                        binding.congratsText.setText(String.valueOf(luckyNumber) + " coins");
                        binding.congratsText.setVisibility(View.VISIBLE);
                        binding.continueButton.setVisibility(View.VISIBLE);
                        onClickedContinue();
                        updateCoins(luckyNumber);

                    }
                },2000);

            }
        });
    }

    public void onShakeImage() {
        Animation shake;
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        binding.luckyBox.startAnimation(shake); // starts animation

    }

    public void updateCoins(int coins){
        int newCoins = coins - 50;
        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("coins", FieldValue.increment(newCoins))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("TAG", "successfully");
                        }
                    }
                });
    }

    private void getUserCoins(){
        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            userCoins = (long) task.getResult().getData().get("coins");
                            if(userCoins >= 50){
                                onClickedImage();
                            }else {
                                Toast.makeText(LuckyNumberActivity.this, "Bạn không đủ 50 coins", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Log.d("TAG", task.getException().toString());
                            Toast.makeText(LuckyNumberActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}