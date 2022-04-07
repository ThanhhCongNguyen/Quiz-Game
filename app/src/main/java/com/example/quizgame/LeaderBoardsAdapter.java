package com.example.quizgame;

//import static com.example.quizgame.WalletFragment.Email;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizgame.databinding.RowLeaderboardsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class LeaderBoardsAdapter extends RecyclerView.Adapter<LeaderBoardsAdapter.LeaderboardsViewHolder> {

    Context context;
    ArrayList<User> users;
    // ProgressDialog progressDialog;
    String email;


    public LeaderBoardsAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public LeaderboardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_leaderboards, parent,false);
        return new LeaderboardsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardsViewHolder holder, int position) {

        User user = users.get(position);
        holder.binding.index.setText(String.format("#%d",position + 1));
        holder.binding.name.setText(user.getName());
        holder.binding.coins.setText(String.valueOf(user.getCoins()));

//        if(user.getEmail().equals(email)){
//            holder.binding.userPlace.setVisibility(View.VISIBLE);
//        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class LeaderboardsViewHolder extends RecyclerView.ViewHolder{

        RowLeaderboardsBinding binding;

        public LeaderboardsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowLeaderboardsBinding.bind(itemView);
        }
    }
}