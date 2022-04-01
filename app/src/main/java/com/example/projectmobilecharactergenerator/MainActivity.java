package com.example.projectmobilecharactergenerator;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.projectmobilecharactergenerator.database.Info.FillInfoDatabase;
import com.example.projectmobilecharactergenerator.database.Info.InfoDatabase;
import com.example.projectmobilecharactergenerator.database.Info.Trait;
import com.example.projectmobilecharactergenerator.settings.SettingsActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Menu menu;
    FirebaseUser user;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_left, R.anim.enter_from_left,R.anim.exit_from_right)
                    .setReorderingAllowed(true)
                    .add(R.id.fcvMain, ViewAllCharactersFragment.class, null)
                    .commit();
        }
        new FillTraitsIfEmpty().execute();

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FragmentManager manager = getSupportFragmentManager();

        switch (item.getItemId())
        {
            case R.id.itViewAll:
                manager.beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_left, R.anim.enter_from_left,R.anim.exit_from_right).replace(R.id.fcvMain, ViewAllCharactersFragment.class, null ).setReorderingAllowed(true).commit();
                break;
            case R.id.itDeleteAll:

                AlertDialog.Builder confirm = new AlertDialog.Builder(this);
                confirm.setMessage("Confirm removing all characters on this app?");
                confirm.setTitle("Delete All");
                confirm.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteDatabase(getString(R.string.CDB));
                        ContextWrapper cw = new ContextWrapper(getApplicationContext());
                        File directory = cw.getDir(getString(R.string.art_dir), Context.MODE_PRIVATE);
                        for (File child : directory.listFiles())
                            child.delete();
                        new FillInfoDatabase().execute(getApplicationContext());
                        manager.beginTransaction().replace(R.id.fcvMain, ViewAllCharactersFragment.class, null ).setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_left, R.anim.enter_from_left,R.anim.exit_from_right).setReorderingAllowed(true).commit();
                    }
                });
                confirm.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });
                confirm.show();

                break;
            case R.id.itNewCharacter:
                FragmentTransaction ft = manager.beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_left, R.anim.enter_from_left,R.anim.exit_from_right);
                CreateCharacterFragment createCharacterFragment = CreateCharacterFragment.newInstance(false, 0L, null, getApplicationContext());
                ft.replace(R.id.fcvMain, createCharacterFragment);
                ft.setReorderingAllowed(true).addToBackStack("createChar").commit();
                break;
            case R.id.itSettings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.itLoadEpithets:
                manager.beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_left, R.anim.enter_from_left,R.anim.exit_from_right).replace(R.id.fcvMain, LoadEpithetsFragment.class, null )
                        .setReorderingAllowed(true).addToBackStack("load_emp").commit();
                break;
            case R.id.itSearch:
                FragmentTransaction fragSearch = manager.beginTransaction().setCustomAnimations(R.anim.enter_from_right,R.anim.exit_from_left, R.anim.enter_from_left,R.anim.exit_from_right);
                ViewAllCharactersFragment viewAllCharactersFragment = ViewAllCharactersFragment.newInstance(true, getApplicationContext());
                fragSearch.replace(R.id.fcvMain, viewAllCharactersFragment);
                fragSearch.setReorderingAllowed(true).addToBackStack("search").commit();
                break;
            case R.id.itLogin:
                if(user == null)
                {
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build());
                    Intent signInIntent = AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build();
                    signInLauncher.launch(signInIntent);
                }
                else
                {
                    AuthUI.getInstance()
                            .signOut(this)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "Signed Out", Toast.LENGTH_LONG).show();
                                    user = null;
                                    toolbar.setBackgroundColor(Color.WHITE);
                                }
                            });
                }


        }
        return super.onOptionsItemSelected(item);
    }

    private class FillTraitsIfEmpty extends AsyncTask<Void, Void, Boolean > {
        @Override
        protected Boolean doInBackground(Void... params) {
            InfoDatabase db = Room.databaseBuilder(getApplicationContext(), InfoDatabase.class, getString(R.string.IDB)).fallbackToDestructiveMigration().build();
            List<Trait> allTraits = db.traitDao().getAllTrait();


            return allTraits.size() < 1;
        }

        @Override
        protected void onPostExecute(Boolean isEmpty) {
            super.onPostExecute(isEmpty);
            if (isEmpty)
                new FillInfoDatabase().execute(getApplicationContext());
        }
    }

    //region Login
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );
    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            user = FirebaseAuth.getInstance().getCurrentUser();
            Toast.makeText(getApplicationContext(), "Sign In Successful. Welcome " + user.getDisplayName(), Toast.LENGTH_LONG).show();
            int lightGreen = Color.parseColor("#54ff54");
            toolbar.setBackgroundColor(lightGreen);


            // ...
        } else {
            Toast.makeText(getApplicationContext(), "Sign In Failure. Please try again ", Toast.LENGTH_LONG).show();
            toolbar.setBackgroundColor(Color.WHITE);
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

    //endregion login
}