package com.example.myhabitat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import habitat.*;
import org.json.JSONObject;
import outils.GestionnaireEditHabitat;

import java.io.*;

public class ModeConceptionActivity extends AppCompatActivity {
    private Habitat habitat;
    private GestionnaireEditHabitat gestionnaire;
    private ActivityResultLauncher<Intent> launcher;
    private ImageButton photoEnCours;  //Detecte à quel mur associer la photo en cours


    /**
     * onCreate de ModeConceptionActivity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_conception);

        gestionnaire = new GestionnaireEditHabitat();

        photoEnCours = null;

        //On récupère Habitat
        Intent intent = getIntent();
        if (intent != null){
            Habitat habitat = intent.getParcelableExtra("Habitat");
            if (habitat != null){
                this.habitat = habitat;
                this.habitat.setCorrectly();
                affichePieces();
            }
        }

        launcher = registerForActivityResult(
                // Contrat qui détermine le type de l'interaction
                new ActivityResultContracts.StartActivityForResult(),
                // Callback appelé lorsque le résultat sera disponible
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //On récupère les données de habitat
                        Intent intent = result.getData();
                        if(intent != null) {
                            Bundle extras = intent.getExtras();
                            Bitmap photoBitmap = (Bitmap) extras.get("data");

                            //On vérifie que la photo a bien été prise
                            Log.i("testPhotoBitmap", "hauteur de l'image : " + photoBitmap.getHeight());

                            Mur murAssocie = gestionnaire.getMur(photoEnCours);
                            if(murAssocie != null){
                                //On enregistre la photo
                                FileOutputStream fos = null;
                                try {
                                    fos = openFileOutput(murAssocie.getId()+".data", MODE_PRIVATE);
                                    photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                    fos.flush();
                                    Log.i("enregistrementPhoto", "La photo " + murAssocie.getId()+".data a bien été enregistrée");
                                } catch (FileNotFoundException e) {
                                    throw new RuntimeException(e);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            affichePieces();
                        }

                    }
                }
        );
    }

    /**
     * Permet d'afficher l'ensemble des pieces de l'habitat
     */
    private void affichePieces() {
        gestionnaire.reset();
        LinearLayout ll = findViewById(R.id.linearLayout);
        ll.removeAllViews();
        for(Piece piece : habitat.getPieces()){
            EditText editText = new EditText(this);
            editText.setText(piece.getNom());
            editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
            editText.setSingleLine();
            gestionnaire.addEditText(editText, piece);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    gestionnaire.getPiece(editText).setNom(String.valueOf(editText.getText()));
                    majHabitat();
                }
            });

            TextView textView1 = new TextView(this);
            textView1.setText(piece.getMurs().toString());
            ll.addView(editText);
            for(Mur mur : piece.getMurs()){
                LinearLayout llMur = new LinearLayout(this);
                //llMur.setGravity(Gravity.CENTER_HORIZONTAL);
                ImageButton imageButton = new ImageButton(this);
                imageButton.setMaxHeight(50);
                imageButton.setMaxWidth(50);

                //On récupère la photo
                FileInputStream fis = null;
                try {
                    fis = openFileInput(mur.getId()+".data");
                } catch (FileNotFoundException e) {
                    //throw new RuntimeException(e);
                }
                if (fis != null) {
                    Bitmap bm = BitmapFactory.decodeStream(fis);

                    imageButton.setImageBitmap(bm);
                }


                gestionnaire.addEditMur(imageButton, mur);
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        photoEnCours = imageButton;
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null){
                            launcher.launch(intent);
                        }
                    }
                });

                TextView textViewOrientation = new TextView(getBaseContext());
                textViewOrientation.setText(mur.getOrientation().toString());
                ll.addView(textViewOrientation);

                llMur.addView(imageButton);
                ll.addView(llMur);

            }
            Button supprimer = new Button(getBaseContext());
            supprimer.setText("Supprimer la pièce");
            supprimer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    habitat.removePiece(piece);
                    affichePieces();
                    majHabitat();
                }
            });
            ll.addView(supprimer);
        }
    }

    /**
     * Permet d'ajouter une piece a l'habitat et de l'afficher
     * @param view
     */
    public void addPiece(View view) {
        Piece piece1 = new Piece("p");
        habitat.addPiece(piece1);
        affichePieces();
        majHabitat();
    }

    /**
     * Mise à jour de l'habitat
     */
    public void majHabitat(){
        Intent intent = new Intent().putExtra("Habitat", habitat);
        setResult(RESULT_OK, intent);
        enregistrement();
    }

    /**
     * Enregistrement de l'habitat sous format JSON
     */
    public void enregistrement(){
        JSONObject enregistrement = new JSONObject();
        enregistrement = habitat.toJSON();

        if(enregistrement != null){
            FileOutputStream fos = null;
            try {
                fos = openFileOutput("enregistrement.json", MODE_PRIVATE);
                PrintStream ps = new PrintStream(fos);
                ps.print(enregistrement);
                ps.close();
                fos.flush();
                Log.i("testEnregistrement", "enregistrement.json a bien été enregistré");
                Log.i("testEnregistrement", "json = " + enregistrement.toString());
                //ouvrirJSON();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Log.i("testJSON", enregistrement.toString());

        }else{
            Log.i("testJSON", "pbm");
        }
    }

    /**
     * Permet un retour à l'activite precedente
     * @param view
     */
    public void confirmer(View view) {
        finish();
    }

    /**
     * Ajoute une Ouverture à Habitat
     * @param view
     */
    public void addOuverture(View view) {
        Intent intent = new Intent(this, CreationOuvertureActivity.class);
        intent.putExtra("Habitat", habitat);
        startActivity(intent);
    }
}