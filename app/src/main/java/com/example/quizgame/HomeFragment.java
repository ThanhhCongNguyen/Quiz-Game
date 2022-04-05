package com.example.quizgame;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quizgame.databinding.FragmentHomeBinding;
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
        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), categoryModels);

        database.collection("categories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.d("er","Error:"+error.getMessage());
                        }else {
                            categoryModels.clear();
                            for(DocumentSnapshot snapshot : value.getDocuments()){
                                CategoryModel model = snapshot.toObject(CategoryModel.class);
                                model.setCategoryId(snapshot.getId());
                                categoryModels.add(model);
                            }
                            categoryAdapter.notifyDataSetChanged();
                        }

//                        categoryModels.clear();
//                        for(DocumentSnapshot snapshot : value.getDocuments()){
//                            CategoryModel model = snapshot.toObject(CategoryModel.class);
//                            model.setCategoryId(snapshot.getId());
//                            categoryModels.add(model);
//                        }
//                        categoryAdapter.notifyDataSetChanged();
                    }
                });

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.recyclerView.setAdapter(categoryAdapter);

        binding.spinwheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        return binding.getRoot();
    }
}