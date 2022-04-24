package com.example.quizgame.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.example.quizgame.R;
import com.example.quizgame.databinding.ActivitySpinWheelBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.ref.PhantomReference;
import java.security.SecureRandom;
import java.util.Random;

public class SpinWheelActivity extends AppCompatActivity {

    ActivitySpinWheelBinding binding;
    private static final String [] sectors = {"30", "60", "90", "120", "150"};
    private static final int [] sectorDegrees = new int[sectors.length];
    private static final Random random = new Random();
    private int degree = 0;
    private boolean isSpinning = false;
    FirebaseFirestore database;
    private long coinsUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpinWheelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarSpinAct);
        binding.toolbarSpinAct.setTitleTextColor(getResources().getColor(R.color.color_white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        database = FirebaseFirestore.getInstance();
        getDegreeForSector();
        getCoinsUser();



    }

    private void onClickedButton(){
        binding.spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isSpinning){
                    spin();
                    isSpinning = true;
                }
            }
        });
    }

    private void spin() {
      //  (360 * sectors.length) + sectorDegrees[degree]
        //degree = random.nextInt(sectors.length - 1);
        degree = random.nextInt(sectors.length);
//        Log.d("TAG", degree+"");
//        Log.d("TAG", sectorDegrees[degree]+"");
//        Log.d("TAG", sectors[sectors.length - (degree+ 1)]+"");
        RotateAnimation rotateAnimation = new RotateAnimation(0, (360 * sectors.length) + sectorDegrees[degree]
        , RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(3600);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());

        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//              sectors[sectors.length - (degree+ 1)]
                int coins = Integer.parseInt(sectors[sectors.length - (degree+ 1)]);
                Toast.makeText(SpinWheelActivity.this, "You've got "+ coins + "coins", Toast.LENGTH_LONG).show();
                isSpinning = false;
                updateCoins(coins);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        binding.spinwheelImage.startAnimation(rotateAnimation);
    }

    private void updateCoins(int coins) {
        int newCoins = coins - 50;
        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("coins", FieldValue.increment(newCoins))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("TAG", "Successfully");
                        }
                    }
                });
    }

    private void getDegreeForSector(){
        int sectorDegree = 360 / sectors.length;
//        Log.d("TAG", String.valueOf(sectorDegree));
//        Log.d("TAG", String.valueOf(sectors.length));
        for(int i = 0; i < sectors.length; i++){
            sectorDegrees[i] = (i + 1) * sectorDegree + 36;
          //  Log.d("TAG", String.valueOf(sectorDegrees[i]));
        }

    }

    private void getCoinsUser(){
        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            coinsUser = (long)task.getResult().getData().get("coins");
                            if(coinsUser >= 50){
                                onClickedButton();
                            }else {
                                Toast.makeText(SpinWheelActivity.this, "Bạn không đủ 50 coins", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Log.d("TAG", task.getException().toString());
                            Toast.makeText(SpinWheelActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}