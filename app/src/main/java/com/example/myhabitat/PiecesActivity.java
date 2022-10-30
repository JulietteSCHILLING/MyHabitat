package com.example.myhabitat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

public class PiecesActivity extends AppCompatActivity {
    private Habitat habitat;
    private TextView textView;
    private GestionnaireEditHabitat gestionnaire;
    private ActivityResultLauncher<Intent> launcher;
    private ImageButton photoEnCours;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pieces);

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
                            //Toast.makeText(PiecesActivity.this, "hauteur de l'image : " + photoBitmap.getHeight(), Toast.LENGTH_SHORT);
                            Log.i("testPhotoBitmap", "hauteur de l'image : " + photoBitmap.getHeight());

                            gestionnaire.getMur(photoEnCours).setPhoto(photoBitmap);
                            affichePieces();

                            /*
                            //On enregistre la photo
                            FileOutputStream fos = null;
                            try {
                                fos = openFileOutput("image.data", MODE_PRIVATE);
                                photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                fos.flush();
                                Log.i("MainActivity", "La photo a bien été enregistrée");
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                             */

                        }

                    }
                }
        );
    }

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
            //ll.addView(textView1);
            for(Mur mur : piece.getMurs()){
                LinearLayout llMur = new LinearLayout(this);
                llMur.setOrientation(LinearLayout.HORIZONTAL);
                ImageButton imageButton = new ImageButton(this);
                imageButton.setMaxHeight(50);
                imageButton.setMaxWidth(50);
                imageButton.setImageBitmap(mur.getPhoto());
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
                llMur.addView(imageButton);
                ll.addView(llMur);
            }
        }
    }

    public void addPiece(View view) {
        Piece piece1 = new Piece("p", habitat);
        Mur murN = new Mur(piece1, Orientation.NORD, habitat);
        Mur murE = new Mur(piece1, Orientation.EST, habitat);
        Mur murS = new Mur(piece1, Orientation.SUD, habitat);
        Mur murO = new Mur(piece1, Orientation.OUEST, habitat);
        piece1.setMurs(murS, murO, murN, murE);
        habitat.getPieces().add(piece1);
        affichePieces();
        majHabitat();
    }

    public void majHabitat(){
        Intent intent = new Intent().putExtra("Habitat", habitat);
        setResult(RESULT_OK, intent);
    }
}