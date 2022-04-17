package com.example.quizgame;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quizgame.databinding.FragmentLeaderboardsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
      //  users.add(new User("Thanh",1000));

//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        database.collection("users")
//               // .orderBy("coins",Query.Direction.DESCENDING)
//                .get()




                leaderBoardsAdapter = new LeaderBoardsAdapter(getContext(), users, new UpdatePositionLeaderBoard() {
                    @Override
                    public void updatePosition(String email) {

                    }
                });

                binding.rycLeaderboard.setAdapter(leaderBoardsAdapter);
                binding.rycLeaderboard.setLayoutManager(new LinearLayoutManager(getContext()));


        return binding.getRoot();

            }
      //  });

      //  return binding.getRoot();
   // }

}