package com.example.projectmobilecharactergenerator.display;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectmobilecharactergenerator.R;
import com.example.projectmobilecharactergenerator.ShowCharacterFragment;
import com.example.projectmobilecharactergenerator.database.Info.Epithet;
import com.example.projectmobilecharactergenerator.database.Info.Epithet;

import java.util.List;

public class EpithetCardAdapter extends RecyclerView.Adapter<EpithetCardAdapter.ViewHolder>{
    Context context;
    List<Epithet> allEpithets;
    List<View> subCategorie;


    public EpithetCardAdapter(Context context, List<Epithet> allEpithets) {
        this.context = context;
        this.allEpithets = allEpithets;
    }

    @NonNull
    @Override
    public EpithetCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.epithets_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpithetCardAdapter.ViewHolder holder, int position) {
        Epithet currentEpithet = allEpithets.get(position);
        holder.tvEpithet.setText(currentEpithet.epithet);
    }

    @Override
    public int getItemCount() {
        return allEpithets.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvEpithet;

        public ViewHolder(@NonNull View view) {
            super(view);
            cardView = view.findViewById(R.id.cvEpithets);
            tvEpithet = view.findViewById(R.id.tvEpithet);
        }
    }
}