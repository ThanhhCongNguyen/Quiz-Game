package com.example.quizgame.fragment;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizgame.action.UpdatePositionLeaderBoard;
import com.example.quizgame.model.User;
import com.example.quizgame.adapter.LeaderBoardsAdapter;
import com.example.quizgame.databinding.FragmentLeaderboardsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LeaderboardsFragment extends Fragment {

    FragmentLeaderboardsBinding binding;
    LeaderBoardsAdapter leaderBoardsAdapter;

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

       FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("users")
                .orderBy("coins", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot documentSnapshot: task.getResult()){
                                String name = (String)documentSnapshot.getData().get("name");
                                long coins = (long)documentSnapshot.getData().get("coins");
                                String id = documentSnapshot.getId();
                                User user = new User(id, name, coins);
                                users.add(user);
                            }
                            leaderBoardsAdapter.notifyDataSetChanged();
                        }else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });




                leaderBoardsAdapter = new LeaderBoardsAdapter(getContext(), users, new UpdatePositionLeaderBoard() {
                    @Override
                    public void updatePosition(String email) {

                    }
                });

                binding.rycLeaderboard.setAdapter(leaderBoardsAdapter);
                binding.rycLeaderboard.setLayoutManager(new LinearLayoutManager(getContext()));


        return binding.getRoot();

            }

}