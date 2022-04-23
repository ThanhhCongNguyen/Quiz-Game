package com.example.quizgame.fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.quizgame.activity.SplashScreen.EMAIL_AND_PASSWORD;
import static com.example.quizgame.activity.SplashScreen.REMEMBER_CHECK;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizgame.activity.LoginActivity;
import com.example.quizgame.activity.MainActivity;
import com.example.quizgame.activity.PropertyActivity;
import com.example.quizgame.model.User;
import com.example.quizgame.activity.HistoryActivity;
import com.example.quizgame.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    FirebaseFirestore database;
    private String EMAIL_USER = "";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    // TODO: Rename and change types of parameters

    public ProfileFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        database = FirebaseFirestore.getInstance();

        database
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            if(snapshot.exists()){
                                User user = snapshot.toObject(User.class);
                                binding.nameText.setText(user.getName());
                                binding.gmailText.setText(user.getEmail());
                                EMAIL_USER = user.getEmail();
                                binding.coinsProfileText.setText(String.valueOf(user.getCoins()));
                                binding.giftProfileText.setText(String.valueOf(user.getGifts().size()));
                                getRankOfCurrentUser(EMAIL_USER);

                            }else {

                            }
                        }else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        binding.giftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PropertyActivity.class));
            }
        });

        binding.historyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), HistoryActivity.class));
            }
        });

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to exit ?");
                builder.setTitle("Alert!");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        preferences = getContext().getSharedPreferences(EMAIL_AND_PASSWORD, MODE_PRIVATE);
                        editor = preferences.edit();
                        editor.putBoolean(REMEMBER_CHECK, false);
                        editor.apply();

                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return binding.getRoot();
    }

    private void getRankOfCurrentUser(String email_user) {
        ArrayList<User> userArrayList = new ArrayList<>();
        database.collection("users")
                .orderBy("coins", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot snapshot = task.getResult();
                            for(DocumentSnapshot documentSnapshot : snapshot){
                                String gmail = (String) documentSnapshot.getData().get("email");
                                User user = new User(gmail);
                                userArrayList.add(user);
                            }
                            for(int i = 0; i < userArrayList.size(); i++){
                                if(email_user.equals(userArrayList.get(i).getEmail())){
                                    binding.rankProfileText.setText(String.valueOf(i + 1)+"/"+ userArrayList.size());
                                    break;
                                }

                            }

                        }else {

                        }
                    }
                });
    }
}