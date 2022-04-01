package com.example.projectmobilecharactergenerator;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmobilecharactergenerator.database.Charcter.CharacterDataBase;
import com.example.projectmobilecharactergenerator.database.Charcter.CharacterInfo;
import com.example.projectmobilecharactergenerator.display.CharacterCardAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewAllCharactersFragment extends Fragment {
    RecyclerView charactersView;
    CharacterCardAdapter adapter;
    View mainView;
    List<CharacterInfo> allCharacterList;
    List<CharacterInfo> searchCharacterList;
    EditText etSearch;
    boolean isSearch = false;

    public ViewAllCharactersFragment() {
        super(R.layout.fragment_view_all_characters);
    }

    public static ViewAllCharactersFragment newInstance(boolean search, Context context)
    {
        ViewAllCharactersFragment viewAllCharactersFragment = new ViewAllCharactersFragment();
        Bundle args = new Bundle();
        args.putBoolean(context.getString(R.string.search), search);
        viewAllCharactersFragment.setArguments(args);
        return  viewAllCharactersFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
        charactersView = view.findViewById(R.id.rvAllCharacters);
        etSearch = view.findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(searchListener);
        if (getArguments() != null)
        {
            isSearch = getArguments().getBoolean(getContext().getString(R.string.search), false);
            if (isSearch)
            {
                etSearch.setVisibility(View.VISIBLE);
                TextView tvSearchTitle = view.findViewById(R.id.tvViewAllTitle);
                tvSearchTitle.setText("Search Characters");
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        new GetAllCharacters().execute();
    }

    TextWatcher searchListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {
            if (isSearch)
            {
                searchCharacterList.clear();
                if (editable.toString() == null || editable.toString().isEmpty())
                    searchCharacterList.addAll(allCharacterList);
                else {
                    for (CharacterInfo character : allCharacterList) {
                        if (character.name.toLowerCase(Locale.ROOT).contains(editable.toString().toLowerCase()))
                            searchCharacterList.add(character);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    };
    private class GetAllCharacters extends AsyncTask<Void, Void, List<CharacterInfo> > {
        @Override
        protected List<CharacterInfo> doInBackground(Void... params) {
            CharacterDataBase db = Room.databaseBuilder(mainView.getContext(), CharacterDataBase.class, getString(R.string.CDB)).fallbackToDestructiveMigration().build();
            List<CharacterInfo> allCharacters = db.characterDao().getAllCharacters();
            return allCharacters;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Toast.makeText(mainView.getContext(), "Loading all Characters", Toast.LENGTH_SHORT).show();
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<CharacterInfo> allCharacters) {
            super.onPostExecute(allCharacters);
            TextView tvNoCharacters = mainView.findViewById(R.id.tvNoCharacters);
            allCharacterList = new ArrayList<>();
            searchCharacterList = new ArrayList<>();
            allCharacterList.addAll(allCharacters) ;
            searchCharacterList.addAll(allCharacterList) ;

            if (allCharacters.size() < 1)
            {
                tvNoCharacters.setVisibility(View.VISIBLE);
            } else {
                tvNoCharacters.setVisibility(View.GONE);
                adapter = new CharacterCardAdapter(mainView.getContext(), getActivity().getSupportFragmentManager(),  searchCharacterList);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(mainView.getContext(), 2, GridLayoutManager.VERTICAL, false);
                charactersView.setLayoutManager(gridLayoutManager);
                charactersView.setHasFixedSize(true);
                charactersView.setAdapter(adapter);
            }
        }
    }
}