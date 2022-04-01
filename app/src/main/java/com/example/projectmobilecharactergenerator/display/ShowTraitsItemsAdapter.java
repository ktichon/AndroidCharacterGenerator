package com.example.projectmobilecharactergenerator.display;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmobilecharactergenerator.R;
import com.example.projectmobilecharactergenerator.database.Charcter.AdditionalTrait;
import com.example.projectmobilecharactergenerator.database.Info.Epithet;

import java.util.List;

public class ShowTraitsItemsAdapter extends RecyclerView.Adapter<ShowTraitsItemsAdapter.ViewHolder>{
    Context context;
    List<AdditionalTrait> allShowTraits;
    String subCategory;


    public ShowTraitsItemsAdapter(Context context, List<AdditionalTrait> allShowTraits) {
        this.context = context;
        this.allShowTraits = allShowTraits;
        subCategory = null;
    }

    @NonNull
    @Override
    public ShowTraitsItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_traits_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowTraitsItemsAdapter.ViewHolder holder, int position) {
        AdditionalTrait currentTrait = allShowTraits.get(position);
        holder.tvType.setText(currentTrait.traitType);
        holder.tvTrait.setText(currentTrait.characterTrait);

    }

    @Override
    public int getItemCount() {
        return allShowTraits.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvType ;
        TextView tvTrait;
        LinearLayout layout;

        public ViewHolder(@NonNull View view) {
            super(view);
            layout = view.findViewById(R.id.llShowItem);
            tvType = view.findViewById(R.id.tvTraitType);
            tvTrait = view.findViewById(R.id.tvTrait);
        }
    }
}