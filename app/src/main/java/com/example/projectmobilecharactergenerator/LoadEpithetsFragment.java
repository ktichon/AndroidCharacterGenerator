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

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectmobilecharactergenerator.database.Info.Epithet;
import com.example.projectmobilecharactergenerator.database.Info.InfoDatabase;
import com.example.projectmobilecharactergenerator.display.EpithetCardAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoadEpithetsFragment extends Fragment {
    View mainView;
    TextView tvJsonCount;
    Button btnFetch;
    RequestQueue queue;
    int totalNames = 0;
    ArrayList<Epithet> epithets;
    LinearLayout linearLayout;
    GridLayoutManager gridLayoutManager;
    RecyclerView epithetView;
    RecyclerView.Adapter epithetAdapter;

    public LoadEpithetsFragment() {
        super(R.layout.fragment_load_epithets);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;

        btnFetch = view.findViewById(R.id.btnLoadEpithets);
        tvJsonCount = view.findViewById(R.id.tvJsonCount);
        linearLayout = view.findViewById(R.id.llEpithets);
        queue = Volley.newRequestQueue(getContext());
        btnFetch.setOnClickListener(onFetchClick);
        gridLayoutManager = new GridLayoutManager(mainView.getContext(), 2, GridLayoutManager.VERTICAL, false);
        epithetView = view.findViewById(R.id.rvAllEpithets);
        epithetView.setLayoutManager(gridLayoutManager);

        epithetView.setHasFixedSize(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        new GetAllStoredEpithets().execute(getContext());
    }




    private void displayEpithets()
    {
        tvJsonCount.setText("Loaded " + epithets.size() + " Epithets");
        btnFetch.setVisibility(View.VISIBLE);
        if (epithets.size() > 0)
        {
            btnFetch.setText("Refresh");
            Collections.sort(epithets);
            epithetAdapter = new EpithetCardAdapter(mainView.getContext(),  epithets);
            epithetView.setAdapter(epithetAdapter);

        }
        else {
            TextView textView = new TextView(getContext());
            textView.setText("No epithets are sorted on the device at the moment. Please click the 'Load' button to fetch epithets from https://api.scryfall.com/. Once fetched, the epithets are stored into the device for offline use");
            linearLayout.addView(textView);
        }

    }


    //region Fetch Data From Api

    View.OnClickListener onFetchClick = view -> {
        epithets.clear();
        epithetView.setAdapter(null);
        loadJson("https://api.scryfall.com/cards/search?q=t%3Acreature+-t%3Agod+t%3Alegend+%27%2C%27");
    };


    private void loadJson(String url)
    {
        try {
            StringRequest request = new StringRequest(url, loadMtgData, getJsonError);
            queue.add(request);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private Response.Listener<String> loadMtgData = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            JSONObject jsonObject = null;
            btnFetch.setVisibility(View.GONE);
            try {
                jsonObject = new JSONObject(response);
                JSONArray data = jsonObject.getJSONArray("data");
                if (jsonObject.getBoolean("has_more"))
                {
                    loadJson(jsonObject.getString("next_page"));
                }
                totalNames += data.length();
                tvJsonCount.setText("Fetching Epithets: " + totalNames + " out of " + jsonObject.getInt("total_cards"));
                for (int i = 0; i < data.length(); i ++)
                {

                    JSONObject card = data.getJSONObject(i);
                    String name = card.getString("name");
                    String epithetName= name.substring(name.indexOf(',') + 1);
                    epithetName = epithetName.replace("\"", "");
                    epithetName = epithetName.trim();
                    if (epithetName.contains("//"))
                        epithetName = epithetName.substring(0, epithetName.indexOf("//") -1);
                    if (epithetName.length() > 4 && epithetName.substring(0, 3).contains("the"))
                        epithetName = epithetName.replaceFirst("the", "");
                    epithetName = epithetName.trim();
                    Epithet epithet = new Epithet("scryfall.com", epithetName);
                    epithets.add(epithet);
                    epithetAdapter = new EpithetCardAdapter(mainView.getContext(),  epithets);
                    epithetView.setAdapter(epithetAdapter);
                }


                if (!jsonObject.getBoolean("has_more"))
                {
                    totalNames = 0;
                    queue.getCache().clear();
                    new SaveEpithets().execute(epithets);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private Response.ErrorListener getJsonError = error -> {
        Toast.makeText(getContext(),"Error fetching Data from https://api.scryfall.com/", Toast.LENGTH_SHORT);
    };
    //endregion Fetch Data From Api

    public class GetAllStoredEpithets extends AsyncTask<Context, Void, List<Epithet>> {
        Context context;
        @Override
        protected List<Epithet> doInBackground(Context... params) {
            context = params[0];
            InfoDatabase db = Room.databaseBuilder(context, InfoDatabase.class, context.getString(R.string.IDB)).build();
            List<Epithet> allEpithets = db.epithetDao().getAllEpithet();
            return allEpithets;
        }

        @Override
        protected void onPostExecute(List<Epithet> storedEpithets) {
            super.onPostExecute(storedEpithets);
            if (storedEpithets.size() < 1)
                Toast.makeText(context, "Please load epithets into the app by pressing 'Load Epithets' on the app bar", Toast.LENGTH_LONG).show();
            epithets = new ArrayList<>();

            epithets.addAll(storedEpithets);
            displayEpithets();
        }
    }

    private class SaveEpithets extends AsyncTask<List<Epithet>, Void, Boolean > {
        @Override
        protected Boolean doInBackground(List<Epithet>... params) {

            Boolean error = false;
            try {
                InfoDatabase db = Room.databaseBuilder(getContext(), InfoDatabase.class, getString(R.string.IDB)).build();
                db.epithetDao().deleteAll();
                List<Epithet> epithets = params[0];
                db.epithetDao().insertEpithetList(epithets);
            } catch (Exception e) {
                error = true;
            }
            return error;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Toast.makeText(getContext(), "Saving Epithets", Toast.LENGTH_SHORT).show();
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean error) {
            super.onPostExecute(error);
            if (error)
                Toast.makeText(getContext(), "Error Saving Epithets", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "Saving Epithets Success!", Toast.LENGTH_SHORT).show();
            displayEpithets();
        }
    }
}