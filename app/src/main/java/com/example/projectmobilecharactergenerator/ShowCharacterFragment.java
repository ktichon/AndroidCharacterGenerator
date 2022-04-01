package com.example.projectmobilecharactergenerator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.projectmobilecharactergenerator.database.Charcter.AdditionalTrait;
import com.example.projectmobilecharactergenerator.database.Charcter.CharacterDataBase;
import com.example.projectmobilecharactergenerator.database.Charcter.CharacterInfo;
import com.example.projectmobilecharactergenerator.display.ImageHelper;
import com.example.projectmobilecharactergenerator.display.ShowTraitsItemsAdapter;
import com.example.projectmobilecharactergenerator.storageClasses.CharacterParam;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowCharacterFragment extends Fragment {
    long id;
    String name;
    View mainView;
    CharacterInfo characterInfo;
    TextView tvDescription;
    TextView title;
    Button btnDelete;
    Button btnUpdate;
    Button btnUpload;
    ScrollView scrollView;
    LinearLayout llUploading;
    TextView tvUploadText;
    List<AdditionalTrait> additionalTraitList;
    LinearLayoutManager layoutManager;
    RecyclerView rvShowTraits;
    RecyclerView.Adapter adapter;

    public ShowCharacterFragment() {
        super(R.layout.fragment_show_character);
    }

    public static ShowCharacterFragment newInstance(Long id, String name, Context context)
    {
        ShowCharacterFragment showCharacterFragment = new ShowCharacterFragment();
        Bundle args = new Bundle();
        args.putLong(context.getString(R.string.c_id), id);
        args.putString(context.getString(R.string.c_name), name);
        showCharacterFragment.setArguments(args);
        return  showCharacterFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainView = view;

        id = getArguments().getLong(getContext().getString(R.string.c_id));
        name = getArguments().getString(getContext().getString(R.string.c_name));
        title  = mainView.findViewById(R.id.tvCharacterTitle);
        title.setText(name);
        tvDescription = mainView.findViewById(R.id.tvDescription);


        btnDelete = view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(onClickDelete);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(onUpdateClick);
        btnUpload = view.findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(onClickUpload);

        scrollView = view.findViewById(R.id.svShowTraits);
        llUploading = view.findViewById(R.id.llLoading);
        tvUploadText = view.findViewById(R.id.tvLoadingText);

        layoutManager = new LinearLayoutManager(mainView.getContext());
        rvShowTraits = view.findViewById(R.id.rvShowTraits);
        rvShowTraits.setLayoutManager(layoutManager);
        rvShowTraits.setNestedScrollingEnabled(true);
        rvShowTraits.setHasFixedSize(true);

    }

    @Override
    public void onStart() {
        super.onStart();
        CharacterParam params = new CharacterParam(id, name, getContext());
        new GetShowCharacter().execute(params);
        new GetShowTraits().execute(params);
    }

    View.OnClickListener onClickDelete = view -> {
        AlertDialog.Builder confirm = new AlertDialog.Builder(getContext());
        confirm.setMessage("Confirm removing " + characterInfo.name +  " from this app?");
        confirm.setTitle("Delete " + characterInfo.name);
        confirm.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new DeleteCharacter().execute(characterInfo);
            }
        });
        confirm.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        confirm.show();

    };

    View.OnClickListener onUpdateClick = view -> {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_left, R.anim.enter_from_left,R.anim.exit_from_right);
        CreateCharacterFragment createCharacterFragment = CreateCharacterFragment.newInstance(true, id, name, getContext());
        ft.replace(R.id.fcvMain, createCharacterFragment);
        ft.setReorderingAllowed(true).addToBackStack("updateChar").commit();
    };

    View.OnClickListener onClickUpload = view -> {
        scrollView.setVisibility(View.GONE);
        llUploading.setVisibility(View.VISIBLE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
            Toast.makeText(getContext(), "Please login before trying to upload Characters", Toast.LENGTH_LONG).show();
        else {
            tvUploadText.setText("Uploading " + characterInfo.name + " to the Cloud");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            Map<String, Object> uploadCharacter = new HashMap<>();
            uploadCharacter.put("0%Name", characterInfo.name);
            uploadCharacter.put("01%Epithet", characterInfo.epithet);
            uploadCharacter.put(String.format("%02d", uploadCharacter.size()) + "%Notes", characterInfo.description);
            uploadCharacter.put(String.format("%02d", uploadCharacter.size()) + "%User", user.getDisplayName());
            if(characterInfo.art_path != null)
            {
                tvUploadText.setText("Uploading Image to the Cloud");
                StorageReference storageRef = storage.getReference();
                StorageReference artRef = storageRef.child(characterInfo.name.replaceAll(" ", "_").replaceAll("/", "_") + "_" + String.valueOf(System.currentTimeMillis()) + "_" + user.getProviderId() + ".png");
                Uri artFile = Uri.fromFile(new File(characterInfo.art_path));
                UploadTask uploadTask = artRef.putFile(artFile);

                try {
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                task.getException();
                            }
                            // Continue with the task to get the download URL
                            return artRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                uploadCharacter.put(String.format("%02d", uploadCharacter.size())  + "%ArtLink", downloadUri.toString());
                                UploadTraitsToCloud(db, uploadCharacter);
                            } else {
                                Toast.makeText(getContext(), "Unable to upload image to cloud", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                catch (Exception e)
                {
                    Log.e("Error Uploading Image To Cloud", e.getLocalizedMessage());
                    Toast.makeText(getContext(), "Error uploading image to cloud", Toast.LENGTH_SHORT).show();
                }
            }
            else
                UploadTraitsToCloud(db, uploadCharacter);
        }
        scrollView.setVisibility(View.VISIBLE);
        llUploading.setVisibility(View.GONE);

    };

    private void UploadTraitsToCloud(FirebaseFirestore db,  Map<String, Object> uploadCharacter)
    {
        tvUploadText.setText("Uploading " + characterInfo.name + " to the Cloud");
        for (AdditionalTrait trait: additionalTraitList)
            uploadCharacter.put(String.format("%02d", uploadCharacter.size())  + "%" + trait.traitType, trait.characterTrait);
        db.collection("characters")
                .add(uploadCharacter)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        scrollView.setVisibility(View.VISIBLE);
                        llUploading.setVisibility(View.GONE);
                        Toast.makeText(getContext(), characterInfo.name + " Successfully Uploaded for All Users to See", Toast.LENGTH_LONG).show();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.popBackStack();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error Uploading " + characterInfo.name, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private class DeleteCharacter extends AsyncTask<CharacterInfo, Void, String> {
        String name;
        @Override
        protected String doInBackground(CharacterInfo... params) {
            CharacterDataBase db = Room.databaseBuilder(getContext(), CharacterDataBase.class, getString(R.string.CDB)).build();
            String errors = null;
            name = params[0].name;
            try {
                errors = params[0].art_path;
                db.characterDao().deleteCharacter(params[0]);
            }
            catch (Exception e){
                errors = "-1";
            }
            return errors;
        }

        @Override
        protected void onPostExecute(String errors) {
            super.onPostExecute(errors);
            if (errors == "-1")
                Toast.makeText(getContext(), "Error Deleting " + name, Toast.LENGTH_SHORT).show();
            else
            {
                if (errors != null)
                {
                    File oldArt = new File(characterInfo.art_path);
                    oldArt.delete();
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        }

    }
    //region Async Tasks
    private class GetShowCharacter extends AsyncTask<CharacterParam, Void, CharacterInfo> {
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
                title.setText(characterInfo.name);
                if (characterInfo.epithet != null) {
                    TextView tvEpithet = mainView.findViewById(R.id.tvCharacterEpithet);
                    tvEpithet.setVisibility(View.VISIBLE);
                    tvEpithet.setText(characterInfo.epithet);
                }
                Bitmap bitmap = ImageHelper.loadArt(characterInfo.art_path);
                if (bitmap != null) {
                    ImageView art = mainView.findViewById(R.id.ivImage);
                    art.setImageBitmap(bitmap);
                    art.setVisibility(View.VISIBLE);
                }
                tvDescription.setText(characterInfo.description);

            } else
                Toast.makeText(getContext(), "Error loading " + name, Toast.LENGTH_SHORT);
        }
    }
    private class GetShowTraits extends AsyncTask<CharacterParam, Void, List<AdditionalTrait>> {
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
            additionalTraitList = traitList;
            adapter = new ShowTraitsItemsAdapter(mainView.getContext(), additionalTraitList);
            rvShowTraits.setAdapter(null);
            rvShowTraits.setAdapter(adapter);
        }
    }
    //endregion

    //region OutDated

    /* public void setCharacter_info(Long id, String name)
    {

        characterInfo = null;
        villain = null;
        dndCharacter = null;
        CharacterParam params = new CharacterParam(id, name, getContext());

        try {
            characterInfo = new GetCharacter().execute(params).get();
            if (characterInfo.hasVillain)
                villain = new GetVillain().execute(params).get();
            if (characterInfo.hasDnDCharacter)
                dndCharacter = new GetDnD().execute(params).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (characterInfo != null)
        {
            title.setText(characterInfo.name);
            if (characterInfo.epithet != null)
            {
                TextView tvEpithet = mainView.findViewById(R.id.tvCharacterEpithet);
                tvEpithet.setVisibility(View.VISIBLE);
                tvEpithet.setText(characterInfo.epithet);
            }
            Bitmap bitmap = ImageHelper.loadArt(characterInfo.art_path);
            if (bitmap != null)
            {
                ImageView art = mainView.findViewById(R.id.ivImage);
                art.setImageBitmap(bitmap);
                art.setVisibility(View.VISIBLE);
            }
            tvDescription.setText(characterInfo.description);
            if (villain != null)
            {
                if (villain.scheme != null && villain.scheme != "")
                {
                    TextView tvScheme = mainView.findViewById(R.id.tvScheme);
                    tvScheme.setVisibility(View.VISIBLE);
                    tvScheme.setText("Wishes to " + villain.scheme.substring(0, 1).toLowerCase(Locale.ROOT) + villain.scheme.substring(1));
                }
                if (villain.method != null && villain.method != "")
                {
                    TextView tvMethod = mainView.findViewById(R.id.tvMethod);
                    tvMethod.setVisibility(View.VISIBLE);
                    tvMethod.setText("Accomplishes their plans by " + villain.method.substring(0, 1).toLowerCase(Locale.ROOT) + villain.method.substring(1));
                }
                if (villain.weakness != null && villain.weakness != "")
                {
                    TextView tvWeakness = mainView.findViewById(R.id.tvWeakness);
                    tvWeakness.setVisibility(View.VISIBLE);
                    tvWeakness.setText("They can be defeated by " + villain.weakness.substring(0, 1).toLowerCase(Locale.ROOT) + villain.weakness.substring(1));
                }
            }
            if (dndCharacter != null)
            {
                if (dndCharacter.personality != null && dndCharacter.personality != "")
                {
                    TextView tvPersonality= mainView.findViewById(R.id.tvPersonality);
                    tvPersonality.setVisibility(View.VISIBLE);
                    tvPersonality.setText("Personality: " + dndCharacter.personality);
                }
                if (dndCharacter.ideal != null && dndCharacter.ideal != "")
                {
                    TextView tvIdeal = mainView.findViewById(R.id.tvIdeal);
                    tvIdeal.setVisibility(View.VISIBLE);
                    tvIdeal.setText("Ideal: " + dndCharacter.ideal);
                }
                if (dndCharacter.bond != null && dndCharacter.bond != "")
                {
                    TextView tvBond= mainView.findViewById(R.id.tvBond);
                    tvBond.setVisibility(View.VISIBLE);
                    tvBond.setText("Bond: " + dndCharacter.bond);
                }
                if (dndCharacter.flaw != null && dndCharacter.flaw != "")
                {
                    TextView tvFlaw= mainView.findViewById(R.id.tvFlaw);
                    tvFlaw.setVisibility(View.VISIBLE);
                    tvFlaw.setText("Flaw: " + dndCharacter.flaw);
                }
            }
        }
    }*/
    //endregion OutDated

}