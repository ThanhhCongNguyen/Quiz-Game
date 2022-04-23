package com.example.quizgame.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.quizgame.adapter.PropertyAdapter;
import com.example.quizgame.R;
import com.example.quizgame.model.User;
import com.example.quizgame.model.Gift;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PropertyActivity extends AppCompatActivity {

    ArrayList<Gift> giftArrayList;
    PropertyAdapter propertyAdapter;
    FirebaseFirestore database;
    RecyclerView recyclerViewProperty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

        database = FirebaseFirestore.getInstance();
        giftArrayList = new ArrayList<>();
        recyclerViewProperty = findViewById(R.id.recyclerview_property);

        database
                .collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()){
                                User user = documentSnapshot.toObject(User.class);
                                Log.d("TAG", "Data "+ user.getGifts().toString());
                                giftArrayList.addAll(user.getGifts());
                               // propertyAdapter.notifyDataSetChanged();
                                propertyAdapter = new PropertyAdapter(giftArrayList, PropertyActivity.this);
                                recyclerViewProperty.setAdapter(propertyAdapter);
                                recyclerViewProperty.setLayoutManager(new LinearLayoutManager(PropertyActivity.this));
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