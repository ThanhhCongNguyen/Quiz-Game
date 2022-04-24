package com.example.quizgame.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.quizgame.activity.SplashScreen.EMAIL_AND_PASSWORD;
import static com.example.quizgame.activity.SplashScreen.REMEMBER_CHECK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.quizgame.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    boolean rememberPass = false;

//    public static final String EMAIL_AND_PASSWORD = "EMAIL_AND_PASSWORD";
//    public static final String REMEMBER_CHECK = "rememberCheck";

    String email, pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Logging in...");


        preferences = getSharedPreferences(EMAIL_AND_PASSWORD, MODE_PRIVATE);
        editor = preferences.edit();

        boolean rememberPass = preferences.getBoolean(REMEMBER_CHECK, false);

        if(rememberPass){
            binding.rememberPasswordCheckbox.setChecked(true);
        }else{
            binding.rememberPasswordCheckbox.setChecked(false);
        }
        //  boolean rememberPass = preferences.getBoolean(REMEMBER_CHECK, false);

//        if(auth.getCurrentUser() != null && rememberPass == true){
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//        }


        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = binding.emailBox.getText().toString();
                pass = binding.passwordBox.getText().toString();

                if (isValid()) {
                    dialog.show();

                    //Toast.makeText(LoginActivity.this,email + pass, Toast.LENGTH_LONG).show();

                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
//                                new AlertDialog.Builder(LoginActivity.this)
//                                        .setTitle("Lưu?")
//                                        .setMessage("Bạn có muốn lưu thông tin tài khoản và mật khẩu không?")
//                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                editor.putBoolean(REMEMBER_CHECK, true);
//                                                editor.apply();
//                                                dialog.dismiss();
//                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                            }
//                                        })
//                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                                dialog.dismiss();
//                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                            }
//                                        }).show();
                                if(binding.rememberPasswordCheckbox.isChecked()){
                                    editor.putBoolean(REMEMBER_CHECK, true);
                                    editor.apply();
                                    dialog.dismiss();
                                }
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }


            }
        });

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

    }

    private boolean isValid() {
        boolean isValid = false;
        if (email.matches("") || pass.matches("")) {
            Toast.makeText(getApplicationContext(), "Email or Password is not null", Toast.LENGTH_LONG).show();
        } else {
            isValid = true;
        }
        return isValid;
    }
}