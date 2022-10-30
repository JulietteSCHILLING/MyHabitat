package com.example.myhabitat;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import habitat.Habitat;
import habitat.Mur;
import habitat.Orientation;
import habitat.Piece;

public class PiecesActivity extends AppCompatActivity {
    private Habitat habitat;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pieces);

        //On récupère Habitat
        Intent intent = getIntent();
        if (intent != null){
            Habitat habitat = intent.getParcelableExtra("Habitat");
            if (habitat != null){
                this.habitat = habitat;
                this.habitat.setCorrectly();
                textView = findViewById(R.id.textViewPieces);
                textView.setText(habitat.toString());
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
        textView.setText(habitat.toString());
        Intent intent = new Intent().putExtra("Habitat", habitat);
        setResult(RESULT_OK, intent);
    }
}