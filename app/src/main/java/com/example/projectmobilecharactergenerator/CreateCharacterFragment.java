package com.example.projectmobilecharactergenerator;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import androidx.sqlite.db.SimpleSQLiteQuery;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectmobilecharactergenerator.database.Charcter.AdditionalTrait;
import com.example.projectmobilecharactergenerator.database.Charcter.CharacterDataBase;
import com.example.projectmobilecharactergenerator.database.Charcter.CharacterInfo;
import com.example.projectmobilecharactergenerator.database.Info.Epithet;
import com.example.projectmobilecharactergenerator.database.Info.InfoDatabase;
import com.example.projectmobilecharactergenerator.database.Info.Trait;
import com.example.projectmobilecharactergenerator.display.ImageHelper;
import com.example.projectmobilecharactergenerator.storageClasses.AllowedVillainTraits;
import com.example.projectmobilecharactergenerator.storageClasses.CharacterParam;
import com.example.projectmobilecharactergenerator.storageClasses.SaveTraitParam;
import com.example.projectmobilecharactergenerator.storageClasses.TraitResult;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class CreateCharacterFragment extends Fragment {
    //region Variables
    final int RESULT_GET_PIC = 72491;
    SharedPreferences preferences;
    int nameSyllableLength = 2;
    int nameReadability = 1;
    boolean includeSurname;
    boolean addThe;
    Boolean nameUseCustomGenerator;
    EditText etName;
    EditText etEpithet;
    EditText etDescription;
    Bitmap bitmapArt;
    TextView tvName;
    TextView tvEpithet;
    TextView tvNameTraits;

    EditText etScheme;
    EditText etMethod;
    EditText etWeakness;

    TextView tvScheme;
    TextView tvMethod;
    TextView tvWeakness;
    TextView tvVillainTraits;

    AllowedVillainTraits allowedVillainTraits;
    CharacterInfo characterInfo;

    TextView etPersonality;
    TextView etIdeal;
    TextView etBond;
    TextView etFlaw;

    TextView tvPersonality;
    TextView tvIdeal;
    TextView tvBond;
    TextView tvFlaw;
    TextView tvDnDTraits;

    boolean enableDnDTraits;
    String[] dndTraits;
    Set<String> availableBackgrounds;
    Set<String> availableDnDTraits;
    List<Trait> allPersonalities;
    List<Trait> allIdeals;
    List<Trait> allBonds;
    List<Trait> allFlaws;

    ImageButton ibRollCharacter;
    String name;
    Boolean isUpdate;





    View mainView;
    RequestQueue queue;
    ArrayList<Epithet> epithets;

    Random random;
    //endregion Variables

    //region Update Methods
    public static CreateCharacterFragment newInstance(boolean update, Long id, String name, Context context)
    {
        CreateCharacterFragment createFragment = new CreateCharacterFragment();
        Bundle args = new Bundle();
        args.putBoolean(context.getString(R.string.update), update);
        args.putLong(context.getString(R.string.c_id), id);
        args.putString(context.getString(R.string.c_name), name);
        createFragment.setArguments(args);
        return  createFragment;
    }
    //endregion Update Methods

    //region Startup Methods
    public CreateCharacterFragment() {
        super(R.layout.create_character_fragment);
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;
        String name;
        dndTraits = getContext().getResources().getStringArray(R.array.dnd_traits);
        etName = view.findViewById(R.id.etName);
        etDescription = view.findViewById(R.id.etDescription);
        etEpithet = view.findViewById(R.id.etEpithet);

        etScheme = view.findViewById(R.id.etScheme);
        etMethod = view.findViewById(R.id.etMethod);
        etWeakness = view.findViewById(R.id.etWeakness);

        etPersonality = view.findViewById(R.id.etPersonality1);
        etIdeal = view.findViewById(R.id.etIdeal);
        etBond  = view.findViewById(R.id.etBond);
        etFlaw = view.findViewById(R.id.etFlaw);



        Button btnUploadImage = view.findViewById(R.id.btnUploadArt);
        btnUploadImage.setOnClickListener(onUpload);
        Button btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(onSaveClick);

        ibRollCharacter = view.findViewById(R.id.ibRollCharacter);
        ibRollCharacter.setOnClickListener(clickRollCharacter);

        tvName  = view.findViewById(R.id.tvName);
        tvName.setOnClickListener(clickRollName);
        tvEpithet = view.findViewById(R.id.tvEpithet);
        tvEpithet.setOnClickListener(clickRollEpithet);
        tvNameTraits = view.findViewById(R.id.tvNameTraits);
        tvNameTraits.setOnClickListener(clickRollNamesTraits);

        tvScheme = view.findViewById(R.id.tvScheme);
        tvMethod = view.findViewById(R.id.tvMethod);
        tvWeakness = view.findViewById(R.id.tvWeakness);
        tvVillainTraits = view.findViewById(R.id.tvVillainTraits);
        tvScheme.setOnClickListener(clickRollScheme);
        tvMethod.setOnClickListener(clickRollMethod);
        tvWeakness.setOnClickListener(clickRollWeakness);
        tvVillainTraits.setOnClickListener(clickRollVillainTraits);

        tvPersonality = view.findViewById(R.id.tvPersonality);
        tvIdeal = view.findViewById(R.id.tvIdeal);
        tvBond  = view.findViewById(R.id.tvBond);
        tvFlaw = view.findViewById(R.id.tvFlaw);
        tvDnDTraits = view.findViewById(R.id.tvDnDTraits);

        tvPersonality.setOnClickListener(clickRollPersonality);
        tvIdeal.setOnClickListener(clickRollIdeal);
        tvBond.setOnClickListener(clickRollBond);
        tvFlaw.setOnClickListener(clickRollFlaw);
        tvDnDTraits.setOnClickListener(clickRollDnDTraits);

        random = new Random();
        new GetAllStoredEpithets().execute();


        isUpdate = getArguments().getBoolean(getContext().getString(R.string.update), false);
        if (isUpdate)
        {
            TextView titleView = view.findViewById(R.id.tvNewCharacterTitle);
            titleView.setText("Update Character");
            Long id = getArguments().getLong(getContext().getString(R.string.c_id));
            name = getArguments().getString(getContext().getString(R.string.c_name));
            CharacterParam params = new CharacterParam(id, name, getContext());
            new GetUpdateCharacter().execute(params);
            new GetUpdateTraits().execute(params);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        allPersonalities = new ArrayList<>();
        allIdeals = new ArrayList<>();
        allBonds = new ArrayList<>();
        allFlaws = new ArrayList<>();

        preferences =  android.preference.PreferenceManager.getDefaultSharedPreferences(getContext());
        nameUseCustomGenerator = preferences.getBoolean("name_generator_used", true);
        nameSyllableLength = preferences.getInt("max_syllable_length", 1);
        nameReadability = Integer.valueOf(preferences.getString("name_complexity", "1"));
        includeSurname = preferences.getBoolean("name_surname", false);
        addThe = preferences.getBoolean("epithet_the", false);
        allowedVillainTraits = new AllowedVillainTraits(preferences.getBoolean("enable_villain", false),
                preferences.getBoolean("villain_scheme", false),
                preferences.getBoolean("villain_method", false),
                preferences.getBoolean("villain_weakness", false));
        mainView.findViewById(R.id.llVillains).setVisibility(View.GONE);
        mainView.findViewById(R.id.llVillainScheme).setVisibility(View.GONE);
        mainView.findViewById(R.id.llVillainMethod).setVisibility(View.GONE);
        mainView.findViewById(R.id.llVillainWeakness).setVisibility(View.GONE);


        if (allowedVillainTraits.enabled)
        {
            mainView.findViewById(R.id.llVillains).setVisibility(View.VISIBLE);
            if (allowedVillainTraits.scheme)
                mainView.findViewById(R.id.llVillainScheme).setVisibility(View.VISIBLE);
            if (allowedVillainTraits.method)
                mainView.findViewById(R.id.llVillainMethod).setVisibility(View.VISIBLE);
            if (allowedVillainTraits.weakness)
                mainView.findViewById(R.id.llVillainWeakness).setVisibility(View.VISIBLE);
        }

        enableDnDTraits = preferences.getBoolean("enable_dnd", false);
        mainView.findViewById(R.id.llDnDTraits).setVisibility(View.GONE);
        mainView.findViewById(R.id.llDnDPersonality).setVisibility(View.GONE);
        mainView.findViewById(R.id.llDnDIdeal).setVisibility(View.GONE);
        mainView.findViewById(R.id.llDnDBond).setVisibility(View.GONE);
        mainView.findViewById(R.id.llDnDFlaw).setVisibility(View.GONE);
        availableDnDTraits = null;


        if (enableDnDTraits)
        {
            mainView.findViewById(R.id.llDnDTraits).setVisibility(View.VISIBLE);
            availableDnDTraits = preferences.getStringSet("dnd_traits", null);
            if (availableDnDTraits.contains(dndTraits[0]))
                mainView.findViewById(R.id.llDnDPersonality).setVisibility(View.VISIBLE);
            if (availableDnDTraits.contains(dndTraits[1]))
                mainView.findViewById(R.id.llDnDIdeal).setVisibility(View.VISIBLE);
            if (availableDnDTraits.contains(dndTraits[2]))
                mainView.findViewById(R.id.llDnDBond).setVisibility(View.VISIBLE);
            if (availableDnDTraits.contains(dndTraits[3]))
                mainView.findViewById(R.id.llDnDFlaw).setVisibility(View.VISIBLE);

            availableBackgrounds = preferences.getStringSet("dnd_backgrounds", null);
            if (availableBackgrounds != null && availableBackgrounds.size() > 0)
            {
                Iterator<String> setIterator = availableBackgrounds.iterator();
                String backgroundQuery = "' AND (background.background = '" + setIterator.next() + "'";
                while (setIterator.hasNext())
                {
                    backgroundQuery += " OR background.background ='" + setIterator.next() + "' ";
                }
                backgroundQuery += ");";
                String startQuery = "SELECT * FROM trait JOIN Background ON background_id = Background.id JOIN TraitType ON trait_type_id = TraitType.id WHERE TraitType.type = '";

                String[] personalityQuery = {startQuery, dndTraits[0], backgroundQuery};
                String[] idealQuery = {startQuery, dndTraits[1], backgroundQuery};
                String[] bondQuery = {startQuery, dndTraits[2], backgroundQuery};
                String[] flawQuery = {startQuery, dndTraits[3], backgroundQuery};
                new GetTraits().execute(personalityQuery);
                new GetTraits().execute(idealQuery);
                new GetTraits().execute(bondQuery);
                new GetTraits().execute(flawQuery);
            }
        }
    }
    //endregion Startup Methods

    //region OnClick Roll Traits Listeners

    //Roll all features
    private View.OnClickListener clickRollCharacter = view -> {
        tvNameTraits.performClick();
        tvVillainTraits.performClick();
        tvDnDTraits.performClick();

    };

    private View.OnClickListener clickRollNamesTraits = view -> {
        tvName.performClick();
        tvEpithet.performClick();
    };

    private View.OnClickListener clickRollName = view -> {
        if(nameUseCustomGenerator)
            etName.setText(NameGeneratorAtHome.getName(nameSyllableLength,nameReadability));
        else
        {
            queue = Volley.newRequestQueue(getContext());
            queue.getCache().clear();
            //String url = "https://uzby.com/api.php?min=" + nameMinLength + "&max=" + nameMaxLength;
            String url = "https://namey.muffinlabs.com/name.json?";
            if (includeSurname)
                url += "with_surname=true";
            Toast.makeText(getContext(), "Getting Name from namey.muffinlabs.com ...", Toast.LENGTH_SHORT).show();
            try {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                etName.setText(response.replace("\"", "").replace("[", "").replace("]", ""));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error generating name. Make sure you have a WIFI connection", Toast.LENGTH_SHORT ).show();
                    }
                });
                queue.add(stringRequest);
                queue.getCache().remove(url);

            }
            catch (Exception e)
            {
                Toast.makeText(getContext(), "Error generating name. Make sure you have a WIFI connection", Toast.LENGTH_SHORT ).show();
            }

        }


    };

    private View.OnClickListener clickRollEpithet = view -> {
        if(epithets.size() > 1)
        {
            Epithet rolledEpithet = epithets.get(random.nextInt(epithets.size()));
            String epithetName = rolledEpithet.epithet;
            if (addThe)
                epithetName = "the " + epithetName;
            etEpithet.setText(epithetName);
        }

    };

    private View.OnClickListener clickRollScheme = view -> {
        if (allowedVillainTraits.scheme)
            etScheme.setText(VillainGenerator.getScheme());
    };
    private View.OnClickListener clickRollMethod = view -> {
        if (allowedVillainTraits.method)
            etMethod.setText(VillainGenerator.getMethod());
    };
    private View.OnClickListener clickRollWeakness = view -> {
        if (allowedVillainTraits.weakness)
            etWeakness.setText(VillainGenerator.getWeakness());
    };
    private View.OnClickListener clickRollVillainTraits = view -> {
        if (allowedVillainTraits.enabled)
        {
            tvScheme.performClick();
            tvMethod.performClick();
            tvWeakness.performClick();
        }
    };

    private View.OnClickListener clickRollPersonality = view -> {
        if (availableBackgrounds.size() > 0 && availableDnDTraits.contains(dndTraits[0]) )
        {

            Trait personalityTrait1 = allPersonalities.get(random.nextInt(allPersonalities.size()));
            /*Trait personalityTrait2 = allPersonalities.get(random.nextInt(allPersonalities.size()));
            while (personalityTrait1 == personalityTrait2)
                personalityTrait2 = allPersonalities.get(random.nextInt(allPersonalities.size()));
            etPersonality.setText(personalityTrait1.trait + "\n\n" + personalityTrait2.trait);*/
            etPersonality.setText(personalityTrait1.trait);
        }
    };

    private View.OnClickListener clickRollIdeal = view -> {
        if (availableBackgrounds.size() > 0 && availableDnDTraits.contains(dndTraits[1]) )
        {
            Trait ideal = allIdeals.get(random.nextInt(allIdeals.size()));
            etIdeal.setText(ideal.trait);
        }
    };

    private View.OnClickListener clickRollBond = view -> {
        if (availableBackgrounds.size() > 0 && availableDnDTraits.contains(dndTraits[2]) )
        {
            Trait bond = allBonds.get(random.nextInt(allBonds.size()));
            etBond.setText(bond.trait);
        }
    };

    private View.OnClickListener clickRollFlaw = view -> {
        if (availableBackgrounds.size() > 0 && availableDnDTraits.contains(dndTraits[3]) )
        {
            Trait flaw = allFlaws.get(random.nextInt(allFlaws.size()));
            etFlaw.setText(flaw.trait);
        }
    };

    private View.OnClickListener clickRollDnDTraits = view -> {
        if (enableDnDTraits && availableDnDTraits.size() > 0)
        {
            tvPersonality.performClick();
            tvIdeal.performClick();
            tvBond.performClick();
            tvFlaw.performClick();
        }
    };

    //endregion OnClick Roll Traits Listeners

    //region Upload Photo Methods

    private View.OnClickListener onUpload = view -> {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, RESULT_GET_PIC);
        } catch (Exception e)
        {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_GET_PIC && resultCode == Activity.RESULT_OK && data != null)
        {
            try {
                Uri selectedImageUri = data.getData();
                InputStream imageStream = getContext().getContentResolver().openInputStream(selectedImageUri);
                bitmapArt = BitmapFactory.decodeStream(imageStream);
                ImageView imageView = mainView.findViewById(R.id.ivImage);
                imageView.setImageBitmap(bitmapArt);
                imageView.setVisibility(View.VISIBLE);
            } catch (Exception e)
            {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    //endregion Upload Photo Methods

    //region Async Get Info
    public class GetAllStoredEpithets extends AsyncTask<Void, Void, List<Epithet>> {
        @Override
        protected List<Epithet> doInBackground(Void ... params) {
            InfoDatabase db = Room.databaseBuilder(getContext(), InfoDatabase.class, getContext().getString(R.string.IDB)).build();
            List<Epithet> allEpithets = db.epithetDao().getAllEpithet();
            return allEpithets;
        }

        @Override
        protected void onPostExecute(List<Epithet> storedEpithets) {
            super.onPostExecute(storedEpithets);
            if (storedEpithets.size() < 1)
                Toast.makeText(getContext(), "Please load epithets into the app by pressing 'Load Epithets' on the app bar", Toast.LENGTH_LONG).show();
            epithets = new ArrayList<>();
            epithets.addAll(storedEpithets);
        }
    }

    private class GetTraits extends AsyncTask<String[], Void, TraitResult> {

        @Override
        protected TraitResult doInBackground(String[]... params) {
            InfoDatabase db = Room.databaseBuilder(getContext(), InfoDatabase.class, getContext().getString(R.string.IDB)).build();
            String stringQuery = params[0][0] + params[0][1] + params[0][2];
            SimpleSQLiteQuery query = new SimpleSQLiteQuery(stringQuery);
            return new TraitResult(db.traitDao().getTraitsByQuery(query), params[0][1]);
        }

        @Override
        protected void onPostExecute(TraitResult traitResult) {
            super.onPostExecute(traitResult);
            if (traitResult.traitType == dndTraits[0])
                allPersonalities.addAll(traitResult.traits);
            if (traitResult.traitType == dndTraits[1])
                allIdeals.addAll(traitResult.traits);
            if (traitResult.traitType == dndTraits[2])
                allBonds.addAll(traitResult.traits);
            if (traitResult.traitType == dndTraits[3])
                allFlaws.addAll(traitResult.traits);
        }
    }

    private class GetUpdateCharacter extends AsyncTask<CharacterParam, Void, CharacterInfo> {
        CharacterParam characterParameters;
        CharacterInfo foundCharacter;
        @Override
        protected CharacterInfo doInBackground(CharacterParam... params) {
            characterParameters = params[0];
            Context context = getContext();
            CharacterDataBase db = Room.databaseBuilder(context, CharacterDataBase.class, context.getString(R.string.CDB)).build();
            try {
                foundCharacter = db.characterDao().getCharacter(characterParameters.id);
            }
            catch (Exception e){
                foundCharacter = null;
            }
            return foundCharacter;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Toast.makeText(characterParameters.context, "Loading " + characterParameters.name + " ...", Toast.LENGTH_SHORT).show();
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(CharacterInfo foundCharacter) {
            super.onPostExecute(foundCharacter);
            characterInfo = foundCharacter;
            if (characterInfo != null) {
                etName.setText(characterInfo.name);
                if (characterInfo.epithet != null)
                {
                    etEpithet.setText(characterInfo.epithet);
                }
                bitmapArt = ImageHelper.loadArt(characterInfo.art_path);
                if (bitmapArt != null)
                {
                    ImageView imageView = mainView.findViewById(R.id.ivImage);
                    imageView.setImageBitmap(bitmapArt);
                    imageView.setVisibility(View.VISIBLE);
                }
                etDescription.setText(characterInfo.description);
            } else
                Toast.makeText(getContext(), "Error loading " + name , Toast.LENGTH_SHORT).show();
        }
    }

    private class GetUpdateTraits extends AsyncTask<CharacterParam, Void, List<AdditionalTrait>> {
        CharacterParam characterParameters;
        List<AdditionalTrait> traitList;
        @Override
        protected List<AdditionalTrait> doInBackground(CharacterParam... params) {
            characterParameters = params[0];
            Context context = getContext();
            CharacterDataBase db = Room.databaseBuilder(context, CharacterDataBase.class, context.getString(R.string.CDB)).build();
            try {
                traitList = db.additionalTraitDao().getAllAdditionalTraitsFromCharacter(characterParameters.id);
            }
            catch (Exception e){
                traitList = new ArrayList<>();
            }
            return traitList;
        }

        @Override
        protected void onPostExecute(List<AdditionalTrait>  traitList) {
            super.onPostExecute(traitList);
            for (AdditionalTrait trait: traitList) {
                switch (trait.traitType.toLowerCase(Locale.ROOT))
                {
                    case "scheme":
                        etScheme.setText(trait.characterTrait);
                        break;
                    case "method":
                        etMethod.setText(trait.characterTrait);
                        break;
                    case "weakness":
                        etWeakness.setText(trait.characterTrait);
                        break;
                    case "personality":
                        etPersonality.setText(trait.characterTrait);
                        break;
                    case "ideal":
                        etIdeal.setText(trait.characterTrait);
                        break;
                    case "bond":
                        etBond.setText(trait.characterTrait);
                        break;
                    case "flaw":
                        etFlaw.setText(trait.characterTrait);
                        break;
                }

            }

        }
    }
    //endregion Async Get Info

    //region Save Character
    private View.OnClickListener onSaveClick = view -> {
        if (characterInfo == null)
            characterInfo = new CharacterInfo(null, null, null, false , false, null);

        characterInfo.name = etName.getText().toString().trim();
        characterInfo.description = etDescription.getText().toString().trim();
        characterInfo.epithet = etEpithet.getText().toString().trim();
        characterInfo.hasVillain = this.allowedVillainTraits.enabled;
        characterInfo.hasDnDCharacter = enableDnDTraits;



        if (etName.getText().toString().trim() == null || etName.getText().toString().trim().isEmpty())
            Toast.makeText(getContext(), "Name Cannot be Blank", Toast.LENGTH_SHORT).show();
        else
        {
            if (bitmapArt !=null)
            {
                if (characterInfo.art_path != null)
                {
                    File oldArt = new File(characterInfo.art_path);
                    oldArt.delete();
                }
                try {
                    characterInfo.art_path = ImageHelper.saveArt(bitmapArt, characterInfo.name, getContext());
                }
                catch (Exception e)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Error Saving Image.");
                    builder.setCancelable(true);
                    builder.setTitle("Error");
                }
            }
            if (isUpdate )
                new UpdateCharacter().execute(characterInfo);
            else
                new AddCharacterToDatabase().execute(characterInfo);

        }
    };

    private class AddCharacterToDatabase extends AsyncTask<CharacterInfo, Void, Long>{
        String name;
        @Override
        protected Long doInBackground(CharacterInfo... params) {
            CharacterDataBase db = Room.databaseBuilder(getContext(), CharacterDataBase.class, getString(R.string.CDB)).build();
            
            name = params[0].name;
            long id = -1;
            try {
               id = db.characterDao().insertCharacter(params[0]);
            }
            catch (Exception e){
                id = -1;
            }
            return id;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Toast.makeText(getContext(), "Saving " + name + " ...", Toast.LENGTH_SHORT).show();
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Long character_id) {
            super.onPostExecute(character_id);
            if (character_id < 0)
                Toast.makeText(getContext(), "Error Saving " + name, Toast.LENGTH_SHORT).show();
            else
                saveAdditionalTraits(character_id);
            /*else if (!saveVillain(character_id) && !saveDnD(character_id))
            {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();

            }*/

        }

    }

    private void saveAdditionalTraits(long character_id)
    {
        List<AdditionalTrait> traitsToSave = new ArrayList<>();
        LinearLayout llMainTrait = mainView.findViewById(R.id.llTraitContainer);
        try
        {
            for (int i = 1; i < llMainTrait.getChildCount() - 3; i ++)
            {
                LinearLayout llCategory = (LinearLayout)llMainTrait.getChildAt(i);
                if (llCategory.getVisibility() == View.VISIBLE)
                {
                    TextView tvCategory = (TextView) llCategory.getChildAt(0);
                    String categoryName = tvCategory.getText().toString().substring(0, tvCategory.getText().toString().indexOf(" Trait") -1);
                    for (int k = 1; k < llCategory.getChildCount(); k ++)
                    {
                        LinearLayout llTrait = (LinearLayout) llCategory.getChildAt(k);
                        if (llTrait.getVisibility() == View.VISIBLE)
                        {
                            String traitType = ((TextView) llTrait.getChildAt(0)).getText().toString();
                            String characterTrait = ((EditText) llTrait.getChildAt(1)).getText().toString().trim();
                            if (characterTrait != null && !characterTrait.isEmpty() ) {
                                AdditionalTrait additionalTrait = new AdditionalTrait(character_id, characterTrait, categoryName, traitType, traitsToSave.size());
                                traitsToSave.add(additionalTrait);
                            }
                        }
                    }
                }
            }
            SaveTraitParam param = new SaveTraitParam(traitsToSave, character_id);
            new AddAdditionalTraitsToDatabase().execute(param);
        }
        catch (Exception e) {
            Toast.makeText(getContext(), "Error Saving Traits", Toast.LENGTH_SHORT).show();
        }

    }

    private class AddAdditionalTraitsToDatabase extends AsyncTask<SaveTraitParam, Void, Boolean>{
        @Override
        protected Boolean doInBackground(SaveTraitParam... params) {
            CharacterDataBase db = Room.databaseBuilder(getContext(), CharacterDataBase.class, getString(R.string.CDB)).build();
            SaveTraitParam saveTraitParam = params[0];
            boolean errors = false;
            try {
                db.additionalTraitDao().deleteAllAdditionalTraitsFromCharacter(saveTraitParam.character_id);
                db.additionalTraitDao().insertAdditionalTraitList(saveTraitParam.additionalTraitList);
            }
            catch (Exception e){
                errors = true;
            }
            return errors;
        }

        @Override
        protected void onPostExecute(Boolean errors) {
            super.onPostExecute(errors);
            if (errors)
                Toast.makeText(getContext(), "Error Saving Additional Traits", Toast.LENGTH_SHORT).show();
            else
            {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        }
    }

    //endregion Save Character

    //region Update Character
    private class UpdateCharacter extends AsyncTask<CharacterInfo, Void, Long>{
        String name;
        @Override
        protected Long doInBackground(CharacterInfo... params) {
            CharacterDataBase db = Room.databaseBuilder(getContext(), CharacterDataBase.class, getString(R.string.CDB)).build();
            name = params[0].name;
            long id = params[0].id;
            try {
                db.characterDao().updateCharacter(params[0]);
            }
            catch (Exception e){
                id = -1;
            }
            return id;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Toast.makeText(getContext(), "Updating " + name + " ...", Toast.LENGTH_SHORT).show();
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Long id) {
            super.onPostExecute(id);
            if (id < 0 )
                Toast.makeText(getContext(), "Error updating " + name, Toast.LENGTH_SHORT).show();
            else
                saveAdditionalTraits(id);
        }

    }


    //endregion Update Character

    //region Outdated Code

    /*public void setCharacter_info(Long id, String name)
    {
        CharacterParam params = new CharacterParam(id, name, getContext());
        try {
            characterInfo = new GetCharacter().execute(params).get();
            if (characterInfo.hasVillain)
            {
                villain = new GetVillain().execute(params).get();
            }
            if (characterInfo.hasDnDCharacter)
            {
                dnDCharacter = new GetDnD().execute(params).get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (characterInfo != null)
        {
            etName.setText(characterInfo.name);
            if (characterInfo.epithet != null)
            {
                etEpithet.setText(characterInfo.epithet);
            }
            bitmapArt = ImageHelper.loadArt(characterInfo.art_path);
            if (bitmapArt != null)
            {
                ImageView imageView = mainView.findViewById(R.id.ivImage);
                imageView.setImageBitmap(bitmapArt);
                imageView.setVisibility(View.VISIBLE);
            }
            etDescription.setText(characterInfo.description);
            if (villain != null)
            {
                if (villain.scheme != null)
                    etScheme.setText(villain.scheme);
                if (villain.method != null)
                    etMethod.setText(villain.method);
                if (villain.weakness != null)
                    etWeakness.setText(villain.weakness);
            }
            if (dnDCharacter != null)
            {
                etPersonality.setText(dnDCharacter.personality);
                etIdeal.setText(dnDCharacter.ideal);
                etBond.setText(dnDCharacter.bond);
                etFlaw.setText(dnDCharacter.flaw);
            }
        }
    }*/

    /*private boolean saveVillain(long character_id)
    {
        boolean errors = false;
        if (allowedVillainTraits.enabled)
        {
            if (villain == null)
                villain = new Villain(0L, null, null, null );
            villain.character_id = character_id;
            villain.scheme = etScheme.getText().toString().trim();
            villain.method = etMethod.getText().toString().trim();
            villain.weakness = etWeakness.getText().toString().trim();
            if (!allowedVillainTraits.scheme)
                villain.scheme = null;
            if (!allowedVillainTraits.method)
                villain.method = null;
            if (!allowedVillainTraits.weakness)
                villain.weakness = null;
            try {
                errors = new AddVillainToDatabase().execute(villain).get();
            } catch (Exception e) {
                errors = true;
            }
            if (errors)
                Toast.makeText(getContext(), "Error Saving Villain Traits", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                new RemoveVillain().execute(character_id).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return errors;

    }
    private boolean saveDnD(long character_id)
    {
        boolean errors = false;
        if (enableDnDTraits)
        {
            if (dnDCharacter == null)
                dnDCharacter = new DnDCharacter(0l, null, null, null, null, null);
            dnDCharacter.character_id = character_id;
            dnDCharacter.personality = ((availableDnDTraits.contains(dndTraits[0]) ? etPersonality.getText().toString().trim(): null));
            dnDCharacter.ideal = ((availableDnDTraits.contains(dndTraits[1]) ? etIdeal.getText().toString().trim(): null));
            dnDCharacter.bond = ((availableDnDTraits.contains(dndTraits[2]) ? etBond.getText().toString().trim(): null));
            dnDCharacter.flaw = ((availableDnDTraits.contains(dndTraits[3]) ? etFlaw.getText().toString().trim(): null));
            try {
                errors = new AddDnDToDatabase().execute(dnDCharacter).get();
            } catch (Exception e) {
                errors = true;
            }
            if (errors)
                Toast.makeText(getContext(), "Error Saving Character Traits", Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                new RemoveDnDCharater().execute(character_id).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return errors;
    }
    private class RemoveDnDCharater extends AsyncTask<Long, Void, Void>{
        @Override
        protected Void doInBackground(Long... params) {
            CharacterDataBase db = Room.databaseBuilder(getContext(), CharacterDataBase.class, getString(R.string.CDB)).build();
            boolean errors = false;
            try {
                db.dndCharacterDao().removeDnDCharacterFromCharacterId(params[0]);
            }
            catch (Exception e){
                errors = true;
            }
            return null;
        }
    }
    private class RemoveVillain extends AsyncTask<Long, Void, Void>{
        @Override
        protected Void doInBackground(Long... params) {
            CharacterDataBase db = Room.databaseBuilder(getContext(), CharacterDataBase.class, getString(R.string.CDB)).build();
            boolean errors = false;
            try {
                db.villainDao().removeAllVillainFromCharacter(params[0]);
            }
            catch (Exception e){
                errors = true;
            }
            return null;
        }
    }

    private class AddVillainToDatabase extends AsyncTask<Villain, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Villain... params) {
            CharacterDataBase db = Room.databaseBuilder(getContext(), CharacterDataBase.class, getString(R.string.CDB)).build();

            boolean errors = false;
            try {
                db.villainDao().insertVillain(params[0]);
            }
            catch (Exception e){
                errors = true;
            }
            return errors;
        }

    }
    private class AddDnDToDatabase extends AsyncTask<DnDCharacter, Void, Boolean>{
        @Override
        protected Boolean doInBackground(DnDCharacter... params) {
            CharacterDataBase db = Room.databaseBuilder(getContext(), CharacterDataBase.class, getString(R.string.CDB)).build();

            boolean errors = false;
            try {
                db.dndCharacterDao().insertDnDCharacter(params[0]);
            }
            catch (Exception e){
                errors = true;
            }
            return errors;
        }
    }*/
    //endregion Outdated Code
}