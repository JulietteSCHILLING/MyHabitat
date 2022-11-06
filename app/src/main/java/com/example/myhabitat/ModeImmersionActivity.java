package com.example.myhabitat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import habitat.Habitat;
import habitat.Mur;
import habitat.Orientation;
import habitat.Piece;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ModeImmersionActivity extends AppCompatActivity {
    Habitat habitat;
    Mur murEnCours;
    Piece pieceEnCours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //On récupère Habitat
        Intent intent = getIntent();
        if (intent != null){
            Habitat habitat = intent.getParcelableExtra("Habitat");
            if (habitat != null){
                this.habitat = habitat;
                this.habitat.setCorrectly();
            }
        }

        setContentView(R.layout.activity_mode_immersion);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_immersion, menu);

        int i =0;
        for(Piece piece : habitat.getPieces()){
            menu.add(piece.getNom());
            menu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Log.i("testImmersion", "J'ouvre " + item.getTitle());
                    Toast.makeText(getBaseContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                    affichePiece(piece);
                    return false;
                }
            });
            i++;
        }

        return true;
    }

    public void affichePiece(Piece piece){
        pieceEnCours = piece;
        ImageView imageViewMur = findViewById(R.id.imageViewMur);
        murEnCours = piece.getMurs().get(0);

        //On récupère la photo
        FileInputStream fis = null;
        try {
            fis = openFileInput(murEnCours.getId()+".data");
        } catch (FileNotFoundException e) {
            //throw new RuntimeException(e);
        }
        if (fis != null) {
            Bitmap bm = BitmapFactory.decodeStream(fis);
            imageViewMur.setImageBitmap(bm);
        }else{
            Log.i("testDrawable", "pas de photo");
            imageViewMur.setImageDrawable(getDrawable(R.drawable.imagemur));
        }
    }

    public void afficheMur(){
        ImageView imageViewMur = findViewById(R.id.imageViewMur);

        //On récupère la photo
        FileInputStream fis = null;
        try {
            fis = openFileInput(murEnCours.getId()+".data");
        } catch (FileNotFoundException e) {
            //throw new RuntimeException(e);
        }
        if (fis != null) {
            Bitmap bm = BitmapFactory.decodeStream(fis);
            imageViewMur.setImageBitmap(bm);
        }else{
            Log.i("testDrawable", "pas de photo");
            imageViewMur.setImageDrawable(getDrawable(R.drawable.imagemur));
        }
    }

    public void afficheSud(View view){
        murEnCours = pieceEnCours.getMurOrientation(Orientation.SUD);
        afficheMur();
    }

    public void afficheNord(View view){
        murEnCours = pieceEnCours.getMurOrientation(Orientation.NORD);
        afficheMur();
    }

    public void afficheEst(View view){
        murEnCours = pieceEnCours.getMurOrientation(Orientation.EST);
        afficheMur();
    }

    public void afficheOuest(View view){
        murEnCours = pieceEnCours.getMurOrientation(Orientation.OUEST);
        afficheMur();
    }
}