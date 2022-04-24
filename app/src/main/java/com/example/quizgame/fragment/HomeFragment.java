package com.example.quizgame.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizgame.action.IntentToQuizActivity;
import com.example.quizgame.activity.HistoryActivity;
import com.example.quizgame.activity.LuckyNumberActivity;
import com.example.quizgame.activity.QuizActivity;
import com.example.quizgame.activity.SpinWheelActivity;
import com.example.quizgame.adapter.CategoryAdapter;
import com.example.quizgame.databinding.FragmentHomeBinding;
import com.example.quizgame.model.CategoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    FirebaseFirestore database;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();

        ArrayList<CategoryModel> categoryModels = new ArrayList<>();
        CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), categoryModels, new IntentToQuizActivity() {
            @Override
            public void intentToQuizActivity(String catId, String categoryName) {
                Intent intent = new Intent(getContext(), QuizActivity.class);
                intent.putExtra("catId", catId);
                intent.putExtra("categoryName", categoryName);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

//        database.collection("categories")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if(error != null){
//                            Log.d("er","Error:"+error.getMessage());
//                        }else {
//                            categoryModels.clear();
//                            for(DocumentSnapshot snapshot : value.getDocuments()){
//                                CategoryModel model = snapshot.toObject(CategoryModel.class);
//                                model.setCategoryId(snapshot.getId());
//                                categoryModels.add(model);
//                            }
//                            categoryAdapter.notifyDataSetChanged();
//                        }
//
////                        categoryModels.clear();
////                        for(DocumentSnapshot snapshot : value.getDocuments()){
////                            CategoryModel model = snapshot.toObject(CategoryModel.class);
////                            model.setCategoryId(snapshot.getId());
////                            categoryModels.add(model);
////                        }
////                        categoryAdapter.notifyDataSetChanged();
//                    }
//                });

        database.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot snapshot: task.getResult()){
                                CategoryModel model = snapshot.toObject(CategoryModel.class);
                                model.setCategoryId(snapshot.getId());
                                categoryModels.add(model);
                            }
                            categoryAdapter.notifyDataSetChanged();

                        }else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.recyclerView.setAdapter(categoryAdapter);

        binding.spinwheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SpinWheelActivity.class));

            }
        });

        binding.luckyNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), LuckyNumberActivity.class));
            }
        });

        return binding.getRoot();
    }
}