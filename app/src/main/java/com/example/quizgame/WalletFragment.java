package com.example.quizgame;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quizgame.databinding.FragmentWalletBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class WalletFragment extends Fragment {
    
    FragmentWalletBinding binding;
    FirebaseFirestore database;
    ArrayList<Gift> giftArrayList;
    User user;
    long coins = 0;
    GiftAdapter giftAdapter;



    public WalletFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWalletBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(User.class);
                        EMAIL = user.getEmail();
                        coins = user.getCoins();
                        binding.currentCoints.setText(String.valueOf(coins));


                        giftArrayList = new ArrayList<>();
                        giftAdapter = new GiftAdapter(getContext(), giftArrayList, coins);
                        binding.recyclerviewGift.setAdapter(giftAdapter);
                        binding.recyclerviewGift.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                });


        database.collection("Gifts")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                            Gift gift = snapshot.toObject(Gift.class);
                            giftArrayList.add(gift);
                        }
                        giftAdapter.notifyDataSetChanged();

                    }
                });



        return binding.getRoot();
    }
}
