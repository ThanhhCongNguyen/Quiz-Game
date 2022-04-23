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

import com.example.quizgame.R;
import com.example.quizgame.databinding.ActivityLuckyNumberBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class LuckyNumberActivity extends AppCompatActivity {

    ActivityLuckyNumberBinding binding;
    Random random = new Random();
    int luckyNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLuckyNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        luckyNumber = random.nextInt(50) + 50;

        binding.luckyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShakeImage();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.luckyBox.setVisibility(View.GONE);
                        binding.congratsText.setText("Bạn nhận được "+ luckyNumber+ " coins");
                        binding.congratsText.setVisibility(View.VISIBLE);
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
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("coins", FieldValue.increment(coins))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("TAG", "successfully");
                        }
                    }
                });
    }

}