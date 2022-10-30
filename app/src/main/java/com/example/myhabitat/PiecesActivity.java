package com.example.myhabitat;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import habitat.*;

public class PiecesActivity extends AppCompatActivity {
    private Habitat habitat;
    private TextView textView;
    private GestionnaireEditPiece gestionnaire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pieces);

        gestionnaire = new GestionnaireEditPiece();

        //On récupère Habitat
        Intent intent = getIntent();
        if (intent != null){
            Habitat habitat = intent.getParcelableExtra("Habitat");
            if (habitat != null){
                this.habitat = habitat;
                this.habitat.setCorrectly();
                affichePieces();
                /*
                textView = findViewById(R.id.textViewPieces);
                textView.setText(habitat.toString());

                 */
            }
        }
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
            gestionnaire.add(editText, piece);

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
            ll.addView(textView1);
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