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
import com.example.projectmobilecharactergenerator.database.Charcter.CharacterInfo;

import java.util.List;

public class CharacterCardAdapter extends RecyclerView.Adapter<CharacterCardAdapter.ViewHolder>{

    Context context;
    List<CharacterInfo> allCharacters;
    FragmentManager fragmentManager;

    public CharacterCardAdapter(Context context, FragmentManager fragmentManager, List<CharacterInfo> allCharacters) {
        this.context = context;
        this.allCharacters = allCharacters;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public CharacterCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.character_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterCardAdapter.ViewHolder holder, int position) {
        CharacterInfo currentCharacter = allCharacters.get(position);
        Bitmap bitmap = ImageHelper.loadArt(currentCharacter.art_path);
        holder.imageView.setImageBitmap(bitmap);
        if (bitmap == null)
            holder.imageView.setVisibility(View.GONE);
        holder.tvName.setText(currentCharacter.name);
        holder.tvEpithet.setText(currentCharacter.epithet);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_left, R.anim.enter_from_left,R.anim.exit_from_right);
                ShowCharacterFragment characterFragment = ShowCharacterFragment.newInstance(currentCharacter.id, currentCharacter.name, view.getContext());
                ft.replace(R.id.fcvMain, characterFragment);
                ft.setReorderingAllowed(true).addToBackStack("load_char").commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return allCharacters.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView tvName;
        TextView tvEpithet;

        public ViewHolder(@NonNull View view) {
            super(view);
            cardView = view.findViewById(R.id.cvAllCharacters);
            imageView = view.findViewById(R.id.ivDisplayImage);
            tvName = view.findViewById(R.id.tvDisplayName);
            tvEpithet = view.findViewById(R.id.tvDisplayEpithet);
        }
    }
}