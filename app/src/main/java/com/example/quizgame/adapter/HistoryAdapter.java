package com.example.quizgame.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizgame.model.History;
import com.example.quizgame.R;
import com.example.quizgame.databinding.RowHistoryBinding;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.LSLBViewHolder>{

    Context context;
    ArrayList<History> histories;

    public HistoryAdapter(Context context, ArrayList<History> histories) {
        this.context = context;
        this.histories = histories;
    }

    @NonNull
    @Override
    public LSLBViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_history, parent, false);
        return new LSLBViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LSLBViewHolder holder, int position) {
        History history = histories.get(position);
        holder.binding.categoryName.setText(history.getCategoryName());
        holder.binding.correctAnswer.setText("Correct answer: "+ String.valueOf(history.getCorrectAnswer()) + "/"
                + String.valueOf(history.getTotal()));
        holder.binding.time.setText("Date: " + history.getTime());

        if(history.getCategoryName().equals("History")){
            holder.binding.imageViewLslb.setImageResource(R.drawable.history);
        }else if(history.getCategoryName().equals("Science")){
            holder.binding.imageViewLslb.setImageResource(R.drawable.science);
        }else if(history.getCategoryName().equals("Maths")){
            holder.binding.imageViewLslb.setImageResource(R.drawable.mathematics);
        }else if(history.getCategoryName().equals("Language")){
            holder.binding.imageViewLslb.setImageResource(R.drawable.language);
        }else if(history.getCategoryName().equals("Animals")){
            holder.binding.imageViewLslb.setImageResource(R.drawable.bee);
        }else if(history.getCategoryName().equals("Sports")){
            holder.binding.imageViewLslb.setImageResource(R.drawable.footballplayers);
        }

        float value = (float) history.getCorrectAnswer() / history.getTotal();
        if(value >= 0.8){
            holder.binding.value.setText("GOOD" );
            holder.binding.value.setTextColor(Color.GREEN);
        }else if(value >= 0.5){
            holder.binding.value.setText("MEDIUM");
            holder.binding.value.setTextColor(Color.MAGENTA);
        }else if(value < 0.5){
            holder.binding.value.setText("SAD");
            holder.binding.value.setTextColor(Color.RED);
        }


    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    class LSLBViewHolder extends RecyclerView.ViewHolder{
        RowHistoryBinding binding;

        public LSLBViewHolder(@NonNull View itemView) {
            super(itemView);
           binding = RowHistoryBinding.bind(itemView);
        }
    }
}
