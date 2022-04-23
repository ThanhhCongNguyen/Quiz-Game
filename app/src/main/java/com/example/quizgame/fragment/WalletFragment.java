package com.example.quizgame.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizgame.action.UpdateCurrentCoins;
import com.example.quizgame.adapter.GiftAdapter;
import com.example.quizgame.databinding.FragmentWalletBinding;
import com.example.quizgame.model.Gift;
import com.example.quizgame.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
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
        Log.d("TAG", FirebaseAuth.getInstance().getUid());
        database = FirebaseFirestore.getInstance();

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(User.class);
                        long coins = user.getCoins();
                        binding.currentCoins.setText(String.valueOf(coins));


                        giftArrayList = new ArrayList<>();
                        giftAdapter = new GiftAdapter(getContext(), giftArrayList, new UpdateCurrentCoins() {
                            @Override
                            public void updateCoins(long coins) {
                                binding.currentCoins.setText(String.valueOf(coins));

                            }
                        });
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
