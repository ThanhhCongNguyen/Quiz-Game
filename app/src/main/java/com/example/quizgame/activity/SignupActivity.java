package com.example.quizgame.activity;

import static com.example.quizgame.activity.SplashScreen.EMAIL_AND_PASSWORD;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizgame.model.User;
import com.example.quizgame.databinding.ActivitySignupBinding;
import com.example.quizgame.model.Gift;
import com.example.quizgame.model.History;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    String email, pass, name;
    ArrayList<History> histories;
    ArrayList<Gift> gifts;
    long coins = 500;
    FirebaseFirestore database;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("We're creating an new account...");



        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = binding.emailBox.getText().toString();
                pass = binding.passwordBox.getText().toString();
                name = binding.nameBox.getText().toString();

                histories = new ArrayList<>();
                gifts = new ArrayList<>();


                if (isValid()) {
                    dialog.show();

                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String id = task.getResult().getUser().getUid();
                                final User user = new User(id, name, email, pass, coins, gifts, histories);
                                database
                                        .collection("users")
                                        .document(id)
                                        .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            preferences = getSharedPreferences(EMAIL_AND_PASSWORD, MODE_PRIVATE);
                                            editor = preferences.edit();
                                            editor.putBoolean(SplashScreen.REMEMBER_CHECK, true);
                                            editor.apply();
                                            dialog.dismiss();
                                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });
                            } else {
                                dialog.dismiss();
                                Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public boolean isValid(){
        boolean isValid = false;
        if(email.matches("") || pass.matches("") || name.matches("")){
            Toast.makeText(getApplicationContext(),"Please fill all information", Toast.LENGTH_LONG).show();
        }else {
            isValid = true;
        }

        return isValid;
    }


}