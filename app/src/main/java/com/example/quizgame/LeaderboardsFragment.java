package com.example.quizgame;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quizgame.databinding.FragmentLeaderboardsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LeaderboardsFragment extends Fragment {

    FragmentLeaderboardsBinding binding;

    public LeaderboardsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLeaderboardsBinding.inflate(inflater, container, false);

        ArrayList<User> users = new ArrayList<>();
        LeaderBoardsAdapter leaderBoardsAdapter = new LeaderBoardsAdapter(getContext(),users);

        binding.rycLeaderboard.setAdapter(leaderBoardsAdapter);
        binding.rycLeaderboard.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("users")
                .orderBy("coins",Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    User user = snapshot.toObject(User.class);
                    users.add(user);
                }

                leaderBoardsAdapter.notifyDataSetChanged();

            }
        });

        return binding.getRoot();
    }
}