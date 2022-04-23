package com.example.quizgame.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quizgame.model.Gift;
import com.example.quizgame.R;
import com.example.quizgame.action.UpdateCurrentCoins;
import com.example.quizgame.model.User;
import com.example.quizgame.databinding.FragmentWalletBinding;
import com.example.quizgame.databinding.RowGiftBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.GiftViewHolder> {

    Context context;
    ArrayList<Gift> giftArrayList;
    long coins;
    FirebaseFirestore database;
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
        holder.binding.nameGiftText.setText(gift.getGiftName());
        holder.binding.priceGiftText.setText(String.valueOf(gift.getGiftPrice()));

        Glide
                .with(context)
                .load(gift.getGiftImage())
                .into(holder.binding.imageGift);
        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("TAG", "Listen failed.", error);
                            return;
                        }

                        User user = value.toObject(User.class);
                        coins = user.getCoins();
                        ArrayList<Gift> gifts = user.getGifts();
                        ArrayList<String> giftName = new ArrayList<>();
                        for(Gift gift1 : gifts){
                            giftName.add(gift1.getGiftName());
                        }

                        if(giftName.contains(gift.getGiftName())){
                            holder.binding.purchaseText.setVisibility(View.GONE);
                            holder.binding.openText.setVisibility(View.VISIBLE);
                        }else {
                            holder.binding.purchaseText.setVisibility(View.VISIBLE);
                            holder.binding.openText.setVisibility(View.GONE);
                        }
                        holder.binding.purchaseText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new AlertDialog.Builder(context)
                                        .setTitle("Đổi quà")
                                        .setMessage("Bạn có muốn đổi quà này không?")
                                        .setNegativeButton("No", null)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if(coins >= gift.getGiftPrice()){
                                                    Map<String, Object> map = new HashMap<>();
                                                    map.put("coins", coins - gift.getGiftPrice());
                                                    map.put("gifts", FieldValue.arrayUnion(gift));

                                                    database.collection("users")
                                                            .document(FirebaseAuth.getInstance().getUid())
                                                            .update(map)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Toast.makeText(context, "Successfully", Toast.LENGTH_LONG).show();
                                                                    holder.binding.purchaseText.setVisibility(View.GONE);
                                                                    holder.binding.openText.setVisibility(View.VISIBLE);
                                                                    updateCurrentCoins.updateCoins(coins);
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(context, "Error updating document", Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                }else {
                                                    Toast.makeText(context,
                                                            "Xin lỗi, Bạn không đủ coin để đổi quà này!",
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }).show();

                            }
                        });
                    }
                });

        holder.binding.openText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(gift.getLink()));
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return giftArrayList.size();
    }

    class GiftViewHolder extends RecyclerView.ViewHolder{
        RowGiftBinding binding;
        public GiftViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowGiftBinding.bind(itemView);

        }
    }


}
