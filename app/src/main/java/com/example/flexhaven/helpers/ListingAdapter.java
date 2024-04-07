package com.example.flexhaven.helpers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flexhaven.ItemDescription;
import com.example.flexhaven.R;

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
        return new ListingViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {

        Item item = items.get(position);
        holder.itemName.setText(item.name);
        holder.itemDescription.setText(item.itemDescription);
        holder.itemImageUrl.setText(item.getImageUrl());
    }
    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
    static class ListingViewHolder extends RecyclerView.ViewHolder {
        final TextView itemName;
        final TextView itemDescription;
        final TextView itemImageUrl;

        ListingViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.textItemName);
            itemDescription = itemView.findViewById(R.id.textItemDescription);
            itemImageUrl = itemView.findViewById(R.id.ListingsItemURL);
            //TODO add images

            //make it clickable!
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Example of starting a new activity
                    Intent intent = new Intent(view.getContext(), ItemDescription.class);
                    intent.putExtra("ITEM_NAME", itemName.getText().toString());
                    intent.putExtra("ITEM_DESCRIPTION", itemDescription.getText().toString());
                    // Add any other data you need to pass to the detail activity

                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
