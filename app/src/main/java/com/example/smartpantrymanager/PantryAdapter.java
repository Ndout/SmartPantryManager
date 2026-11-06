package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<PantryItem> pantryItems = new ArrayList<>();
    private OnItemClickListener deleteClickListener;

    public interface OnItemClickListener {
        void onDeleteClick(PantryItem item);
    }

    public void setOnDeleteClickListener(OnItemClickListener listener) {
        this.deleteClickListener = listener;
    }

    public void setPantryItems(List<PantryItem> items) {
        this.pantryItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem currentItem = pantryItems.get(position);
        holder.tvName.setText(currentItem.getName());
        holder.tvQuantity.setText("Quantity: " + currentItem.getQuantity() + " " + currentItem.getUnit());

        String expiry = currentItem.getExpiryDate();
        if (expiry != null && !expiry.isEmpty()) {
            holder.tvExpiry.setText("Expires: " + expiry);
            holder.tvExpiry.setVisibility(View.VISIBLE);
        } else {
            holder.tvExpiry.setVisibility(View.GONE);
        }

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvExpiry;
        ImageButton btnDelete;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvIngredientName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvExpiry = itemView.findViewById(R.id.tvExpiryDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}