package com.ipi.garageplus.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ipi.garageplus.R;

import java.util.ArrayList;
import java.util.List;

public class ServiceScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_GROUP = 0;
    private static final int TYPE_CHILD = 1;

    private final List<Object> items = new ArrayList<>();

    public void setGroups(List<ServiceGroup> groups) {
        items.clear();
        for (ServiceGroup group : groups) {
            items.add(group);
            items.addAll(group.getItems());
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof ServiceGroup ? TYPE_GROUP : TYPE_CHILD;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_GROUP) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_service_group, parent, false);
            return new GroupViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_service_child, parent, false);
            return new ChildViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);

        if (holder instanceof GroupViewHolder) {
            ((GroupViewHolder) holder).bind((ServiceGroup) item);
        } else if (holder instanceof ChildViewHolder) {
            ((ChildViewHolder) holder).bind((SubcategoryTotal) item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvCategoryTotal;

        GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvCategoryTotal = itemView.findViewById(R.id.tvCategoryTotal);
        }

        void bind(ServiceGroup group) {
            tvCategory.setText(group.getCategory());
            tvCategoryTotal.setText(String.format("%.2f KM", group.getTotal()));
        }
    }

    static class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubcategory, tvPrice;

        ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubcategory = itemView.findViewById(R.id.tvSubcategory);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }

        void bind(SubcategoryTotal item) {
            tvSubcategory.setText(item.getName());
            tvPrice.setText(String.format("%.2f KM", item.getTotal()));
        }
    }
}