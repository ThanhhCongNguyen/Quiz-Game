package com.example.quizgame.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.quizgame.R;
import com.example.quizgame.databinding.ActivityQuizBinding;
import com.example.quizgame.fragment.HomeFragment;
import com.example.quizgame.model.Question;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding;
    ArrayList<Question> questions;
    ArrayList<Question> allQuestions;
    int index = 0;
    Question question;
    CountDownTimer timer;
    FirebaseFirestore database;
    int correctAnswer = 0;
    String catId;
    String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseFirestore.getInstance();
        catId = getIntent().getStringExtra("catId");
        categoryName = getIntent().getStringExtra("categoryName");

        Log.d("TAG", catId);
        Log.d("TAG", categoryName);

       // final String catIdAgain = getIntent().getStringExtra("catIdAgain");
        questions = new ArrayList<>();
        allQuestions = new ArrayList<>();

        database.collection("categories")
                .document(catId)
                .collection("questions")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    Log.d("Firebase", "List is empty");
                    return;
                }else {
                    for(DocumentSnapshot snapshot : queryDocumentSnapshots){
                        Question question = snapshot.toObject(Question.class);
                        allQuestions.add(question);
                    }
                    Collections.shuffle(allQuestions);
                    for(int i = 0; i < 5; i++){
                        questions.add(allQuestions.get(i));
                    }
                    setNextQuestion();
                    //Log.d("Fire", queryDocumentSnapshots.toString());
                }
            }
        });

        binding.quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(QuizActivity.this)
                        .setTitle("Thoát?")
                        .setMessage("Bạn có chắc muốn thoát không?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(QuizActivity.this, MainActivity.class));
                            }
                        }).show();
            }
        });

        resetTimer();
        setNextQuestion();
    }

    void setNextQuestion(){

        if(timer != null){
            timer.cancel();
        }

        timer.start();
        if(index < questions.size()){
            binding.questionCount.setText(String.format("%d/%d",(index+1),questions.size()));
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            Collections.shuffle(question.getOptions());
            binding.option1.setText(question.getOptions().get(0));
            binding.option2.setText(question.getOptions().get(1));
            binding.option3.setText(question.getOptions().get(2));
            binding.option4.setText(question.getOptions().get(3));

        }
    }

    void resetTimer(){
        timer = new CountDownTimer(20000,1000) {
            @Override
            public void onTick(long l) {
                binding.txtTime.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {
                if(index < questions.size() - 1){
                    reset();
                    index++;
                    setNextQuestion();
                }else {
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("catId", catId);
                    intent.putExtra("categoryName", categoryName);
                    intent.putExtra("correct", correctAnswer);
                    intent.putExtra("total", questions.size());
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    void checkAnswer(TextView textView){
        String selectedAnswer = textView.getText().toString();
        if(selectedAnswer.equals(question.getAnswer())){
            correctAnswer++;
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
            textView.setEnabled(false);
        }else {
            showAnswer();
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
        }
    }

    void reset(){
        binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option1.setEnabled(true);
        binding.option2.setEnabled(true);
        binding.option3.setEnabled(true);
        binding.option4.setEnabled(true);
    }

    void showAnswer(){
        if(question.getAnswer().equals(binding.option1.getText().toString())){
            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_right));
            binding.option1.setEnabled(false);
            binding.option2.setEnabled(false);
            binding.option3.setEnabled(false);
            binding.option4.setEnabled(false);
        }else if(question.getAnswer().equals(binding.option2.getText().toString())){
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_right));
            binding.option1.setEnabled(false);
            binding.option2.setEnabled(false);
            binding.option3.setEnabled(false);
            binding.option4.setEnabled(false);
        }else if(question.getAnswer().equals(binding.option3.getText().toString())){
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_right));
            binding.option1.setEnabled(false);
            binding.option2.setEnabled(false);
            binding.option3.setEnabled(false);
            binding.option4.setEnabled(false);
        }else if(question.getAnswer().equals(binding.option4.getText().toString())){
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_right));
            binding.option1.setEnabled(false);
            binding.option2.setEnabled(false);
            binding.option3.setEnabled(false);
            binding.option4.setEnabled(false);
        }
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:
                if(timer != null){
                    timer.cancel();
                }
                TextView selected = (TextView) view;
                checkAnswer(selected);
                break;
            case R.id.next_btn:
                if(index < questions.size() - 1){
                    reset();
                    index++;
                    setNextQuestion();
                }else {
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("catId", catId);
                    intent.putExtra("categoryName", categoryName);
                    intent.putExtra("correct", correctAnswer);
                    intent.putExtra("total", questions.size());
                    startActivity(intent);
                    finish();
                }
                break;
            }
    }

}