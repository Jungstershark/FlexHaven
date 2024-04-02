package com.example.flexhaven.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    }
    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
    static class ListingViewHolder extends RecyclerView.ViewHolder {
        final TextView itemName;
        final TextView itemDescription;

        ListingViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.textItemName);
            itemDescription = itemView.findViewById(R.id.textItemDescription);
            // itemImage = itemView.findViewById(R.id.imageViewItem); //TODO add images
        }
    }
}
