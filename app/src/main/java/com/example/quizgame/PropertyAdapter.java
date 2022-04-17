package com.example.quizgame;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    ArrayList<Gift> giftArrayList;
    Context context;

    public PropertyAdapter(ArrayList<Gift> giftArrayList, Context context) {
        this.giftArrayList = giftArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_property, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Gift gift = giftArrayList.get(position);
        holder.nameGiftText.setText(gift.getGiftName());
        holder.priceGiftText.setText(String.valueOf(gift.getGiftPrice()));

        Glide
                .with(context)
                .load(gift.getGiftImage())
                .into(holder.imageGift);

        holder.useText.setOnClickListener(new View.OnClickListener() {
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

    class PropertyViewHolder extends RecyclerView.ViewHolder{
        TextView nameGiftText, useText, priceGiftText;
        ImageView imageGift;


        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameGiftText = itemView.findViewById(R.id.name_gift_text);
            useText = itemView.findViewById(R.id.use_text);
            priceGiftText = itemView.findViewById(R.id.price_gift_text);
            imageGift = itemView.findViewById(R.id.image_gift);

        }
    }
}
