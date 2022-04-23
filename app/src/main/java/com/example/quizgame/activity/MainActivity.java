package com.example.quizgame.activity;

import static com.example.quizgame.activity.SplashScreen.EMAIL_AND_PASSWORD;
import static com.example.quizgame.activity.SplashScreen.REMEMBER_CHECK;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.example.quizgame.databinding.ActivityMainBinding;
import com.example.quizgame.fragment.ProfileFragment;
import com.example.quizgame.R;
import com.example.quizgame.fragment.WalletFragment;
import com.example.quizgame.fragment.HomeFragment;
import com.example.quizgame.fragment.LeaderboardsFragment;

import me.ibrahimsn.lib.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout2, new HomeFragment());
        transaction.commit();

        binding.bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (i){
                    case 0:
                        transaction.replace(R.id.frameLayout2, new HomeFragment());
                        transaction.commit();
                        break;
                    case 1:
                        transaction.replace(R.id.frameLayout2, new LeaderboardsFragment());
                        transaction.commit();
                        break;
                    case 2:
                        transaction.replace(R.id.frameLayout2, new WalletFragment());
                        transaction.commit();
                        break;
                    case 3:
                        transaction.replace(R.id.frameLayout2, new ProfileFragment());
                        transaction.commit();
                        break;
                }
                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.logout){

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Do you want to exit ?");
            builder.setTitle("Alert!");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    preferences = getSharedPreferences(EMAIL_AND_PASSWORD, MODE_PRIVATE);
                    editor = preferences.edit();
                    editor.putBoolean(REMEMBER_CHECK, false);
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else {
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}