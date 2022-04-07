package com.example.quizgame;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quizgame.databinding.FragmentWalletBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.GiftViewHolder> {

    Context context;
    ArrayList<Gift> giftArrayList;
    long coins;
    FragmentWalletBinding binding;
    FirebaseFirestore database;
    private ArrayList<String> list = new ArrayList<>();
    UpdateCurrentCoins updateCurrentCoins;


    public GiftAdapter(Context context, ArrayList<Gift> giftArrayList, UpdateCurrentCoins updateCurrentCoins) {
        this.context = context;
        this.giftArrayList = giftArrayList;
        this.updateCurrentCoins = updateCurrentCoins;
    }

    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_gift, parent, false);
        return new GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftViewHolder holder, int position) {
        database = FirebaseFirestore.getInstance();

        Gift gift = giftArrayList.get(position);
        holder.nameGiftText.setText(gift.getGiftName());
        holder.priceGiftText.setText(String.valueOf(gift.getGiftPrice()));

        Glide
                .with(context)
                .load(gift.getGiftImage())
                .into(holder.imageGift);

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                list = (ArrayList<String>) document.getData().get("gifts");
                                Log.d("TAG", "DocumentSnapshot data: " + list);
                                Log.d("TAG", "Gift data: " + gift.getGiftName());
                                if (list.contains(gift.getGiftName())) {

                                    holder.selectText.setText(R.string.locked);
                                    holder.selectText.setEnabled(false);
                                }else {

                                    holder.selectText.setText(R.string.select);
                                    holder.selectText.setEnabled(true);
                                }
                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });


        holder.selectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("TAG", String.valueOf(coins));
//                Log.d("TAG", String.valueOf(gift.getGiftPrice()));
                new AlertDialog.Builder(context)
                        .setTitle("Đổi quà")
                        .setMessage("Bạn có muốn đổi quà này không?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                database.collection("users")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                User user = documentSnapshot.toObject(User.class);
                                                coins = user.getCoins();

                                                Log.d("TAG", String.valueOf(coins));
                                                if(coins >= gift.getGiftPrice()){
                                                    Map<String, Object> map = new HashMap<>();
                                                    map.put("coins", coins - gift.getGiftPrice());
                                                    map.put("gifts", FieldValue.arrayUnion(gift.getGiftName()));

                                                    database.collection("users")
                                                            .document(FirebaseAuth.getInstance().getUid())
                                                            .update(map)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.d("TAG", "DocumentSnapshot successfully updated!");

                                                                    // Thong bao thanh cong
                                                                    Toast.makeText(context, "Successfully", Toast.LENGTH_LONG).show();
                                                                    holder.selectText.setText(R.string.locked);
                                                                    holder.selectText.setEnabled(false);
                                                                    updateCurrentCoins.updateCoins(coins - gift.getGiftPrice());

                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w("TAG", "Error updating document", e);
                                                                }
                                                            });

                                                }else {
                                                    Toast.makeText(context, "Fail", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return giftArrayList.size();
    }

    class GiftViewHolder extends RecyclerView.ViewHolder{
        TextView nameGiftText, priceGiftText, selectText;
        ImageView imageGift;
        public GiftViewHolder(@NonNull View itemView) {
            super(itemView);

            nameGiftText = itemView.findViewById(R.id.name_gift_text);
            priceGiftText = itemView.findViewById(R.id.price_gift_text);
            selectText = itemView.findViewById(R.id.select_text);
            imageGift = itemView.findViewById(R.id.image_gift);

        }
    }

}
