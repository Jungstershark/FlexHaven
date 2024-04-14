package com.example.flexhaven.helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flexhaven.ItemDescription;
import com.example.flexhaven.R;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingViewHolder> {
    private final Context context;
    private LayoutInflater mInflater;
    private ArrayList<Item> items;

    // Constructor
    public ListingAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.items = items;
    }

    public void updateItems(ArrayList<Item> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.row_item, parent, false);
        return new ListingViewHolder(itemView, items);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {
        Item item = items.get(position);
        String url = item.getImageUrl();
        ImageView itemProductImage = holder.itemProductImage;
        if (!TextUtils.isEmpty(url)) {
            // Load image using Glide if URL is not empty
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(itemProductImage);
        } else {
            // If URL is empty, set the ImageView to display a placeholder image directly
            itemProductImage.setImageResource(R.drawable.ic_launcher_foreground);
        }

        String name = item.name;
        holder.itemName.setText(name);
        holder.itemCondition.setText("Condition: " + item.condition);
        holder.itemPrice.setText("Price: " + item.price);
        holder.itemLocation.setText("Location: " + item.location);
        holder.itemOwner.setText("By: " + item.getOwner().userTier);

        // Set user tier drawable dynamically
        User currentUser = item.getOwner();
        if (currentUser != null) {
            currentUser.computeTier();
            Drawable tierIcon;
            switch (currentUser.userTier) {
                case "Bronze":
                    tierIcon = context.getResources().getDrawable(R.drawable.tier_bronze_icon);
                    break;
                case "Silver":
                    tierIcon = context.getResources().getDrawable(R.drawable.tier_silver_icon);
                    break;
                case "Gold":
                    tierIcon = context.getResources().getDrawable(R.drawable.tier_gold_icon);
                    break;
                case "Platinum":
                    tierIcon = context.getResources().getDrawable(R.drawable.tier_platinum_icon);
                    break;
                default:
                    tierIcon = context.getResources().getDrawable(R.drawable.tier_bronze_icon);
            }
            // Set the drawable to the end of the TextView
            holder.itemOwner.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, tierIcon, null);
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    class ListingViewHolder extends RecyclerView.ViewHolder {
        final TextView itemName;
        final TextView itemCondition;
        final TextView itemPrice;
        final TextView itemLocation;
        final ImageView itemProductImage;
        final TextView itemOwner;
        ArrayList<Item> items;

        ListingViewHolder(View itemView, ArrayList<Item> items) {
            super(itemView);
            this.items = items;
            itemName = itemView.findViewById(R.id.textItemName);
            itemCondition = itemView.findViewById(R.id.textItemCondition);
            itemPrice = itemView.findViewById(R.id.textItemPrice);
            itemLocation = itemView.findViewById(R.id.textItemLocation);
            itemProductImage = itemView.findViewById(R.id.itemProductImage);
            itemOwner = itemView.findViewById(R.id.textUser);

            // Set click listener for the item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get the position of the clicked item
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Retrieve the corresponding Item object
                        Item item = items.get(position);

                        // Example of starting a new activity and passing the Item object
                        Intent intent = new Intent(view.getContext(), ItemDescription.class);
                        intent.putExtra("ITEM_OBJECT", item);
                        view.getContext().startActivity(intent);
                    }
                }
            });
        }
    }
}
