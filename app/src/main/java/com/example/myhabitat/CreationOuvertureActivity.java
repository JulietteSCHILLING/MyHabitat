package com.example.myhabitat;

import android.content.Intent;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import habitat.Habitat;
import habitat.Mur;
import habitat.Orientation;
import habitat.Piece;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CreationOuvertureActivity extends AppCompatActivity{

    private Habitat habitat;
    private ImageView imageViewDepart;
    private Piece pieceDepart;
    private Piece pieceArrivee;
    private Piece pieceEnCours;
    private Orientation orientationPieceDepart;
    private Orientation orientationPieceArrivee;
    private Paint myPaint;
    private Canvas canvasDepart;
    private Rect rectDepart;
    private Rect rectArrivee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_ouverture);

        //On récupère Habitat
        Intent intent = getIntent();
        if (intent != null){
            Habitat habitat = intent.getParcelableExtra("Habitat");
            if (habitat != null){
                this.habitat = habitat;
                this.habitat.setCorrectly();
            }
        }

        imageViewDepart = findViewById(R.id.imageViewDepart);

        myPaint = new Paint();
        myPaint.setStrokeWidth(5);
        myPaint.setColor(Color.BLUE);
        myPaint.setStyle(Paint.Style.STROKE);

        setSurfaceForSelect();

        Spinner spinnerD = findViewById(R.id.spinnerDepart);
        Spinner spinnerDOrientation = findViewById(R.id.spinnerDOrientation);
        Spinner spinnerA = findViewById(R.id.spinnerArrivee);
        Spinner spinnerAOrientation = findViewById(R.id.spinnerAOrientation);

        String[] arrayPieces = new String[habitat.getPieces().size()];
        for(int i=0; i<habitat.getPieces().size(); i++){
            arrayPieces[i] = habitat.getPieces().get(i).getNom();
        }

        String[] arrayOrientation = new String[]{"NORD", "SUD", "EST", "OUEST"};

        ArrayAdapter arrayAdapterD = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayPieces);
        spinnerD.setAdapter(arrayAdapterD);
        ArrayAdapter arrayAdapterDOrientation = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayOrientation);
        spinnerDOrientation.setAdapter(arrayAdapterDOrientation);
        ArrayAdapter arrayAdapterA = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayPieces);
        spinnerA.setAdapter(arrayAdapterA);
        ArrayAdapter arrayAdapterAOrientation = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayOrientation);
        spinnerAOrientation.setAdapter(arrayAdapterAOrientation);


        spinnerD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pieceDepart = habitat.getPieces().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDOrientation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        orientationPieceDepart = Orientation.NORD;
                        break;
                    case 1:
                        orientationPieceDepart = Orientation.SUD;
                        break;
                    case 2:
                        orientationPieceDepart = Orientation.EST;
                        break;
                    case 3:
                        orientationPieceDepart = Orientation.OUEST;
                        break;
                    default:
                        orientationPieceDepart = Orientation.SUD;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        spinnerA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pieceArrivee = habitat.getPieces().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerAOrientation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        orientationPieceArrivee = Orientation.NORD;
                        break;
                    case 1:
                        orientationPieceArrivee = Orientation.SUD;
                        break;
                    case 2:
                        orientationPieceArrivee = Orientation.EST;
                        break;
                    case 3:
                        orientationPieceArrivee = Orientation.OUEST;
                        break;
                    default:
                        orientationPieceArrivee = Orientation.SUD;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSurfaceForSelect() {
        SurfaceView surfaceViewDepart = findViewById(R.id.surfaceViewDepart);
        surfaceViewDepart.setZOrderOnTop(true);
        surfaceViewDepart.getHolder().setFormat(PixelFormat.TRANSPARENT);
        canvasDepart = surfaceViewDepart.getHolder().lockCanvas();

        imageViewDepart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (pieceEnCours != null) {
                    if (event.getPointerCount() == 2) {
                        float x1, x2, y1, y2;
                        x1 = event.getX(0);
                        y1 = event.getY(0);
                        x2 = event.getX(1);
                        y2 = event.getY(1);
                        Log.i("SelectActivity", "################################################# Coords : " + x1 + " | " + y1 + "  &  " + x2 + " | " + y2);

                        if (pieceEnCours.equals(pieceDepart)) {
                            rectDepart = new Rect((int) x1, (int) y1, (int) x2, (int) y2);
                            rectDepart.sort();

                            try {
                                canvasDepart = surfaceViewDepart.getHolder().lockCanvas();
                                synchronized (surfaceViewDepart.getHolder()) {
                                    canvasDepart.drawColor(0, PorterDuff.Mode.CLEAR);
                                    canvasDepart.drawRect(rectDepart, myPaint);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (canvasDepart != null) {
                                    surfaceViewDepart.getHolder().unlockCanvasAndPost(canvasDepart);
                                }
                            }
                        } else {
                            rectArrivee = new Rect((int) x1, (int) y1, (int) x2, (int) y2);
                            rectArrivee.sort();

                            try {
                                canvasDepart = surfaceViewDepart.getHolder().lockCanvas();
                                synchronized (surfaceViewDepart.getHolder()) {
                                    canvasDepart.drawColor(0, PorterDuff.Mode.CLEAR);
                                    canvasDepart.drawRect(rectArrivee, myPaint);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (canvasDepart != null) {
                                    surfaceViewDepart.getHolder().unlockCanvasAndPost(canvasDepart);
                                }
                            }
                        }


                        //Log.i("SelectActivity", "################################################# Coords Rect : " + rect.left + " | " + rect.top + "  &  " + rect.right + " | " + rect.bottom);

                    }
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (rectDepart != null) {
                            Log.i("Touchup", "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ je releve mon doigt");

                            //showImage();

                        }
                    }
                }
                return true;
            }
        });
    }


    public void affichePieceDepart(){
        if(pieceDepart != null && orientationPieceDepart != null){
            Mur mur = pieceDepart.getMurOrientation(orientationPieceDepart);
            FileInputStream fis = null;
            try {
                fis = openFileInput(mur.getId()+".data");
            } catch (FileNotFoundException e) {
                //throw new RuntimeException(e);
            }
            if (fis != null) {
                Bitmap bm = BitmapFactory.decodeStream(fis);

                imageViewDepart.setImageBitmap(bm);
            }else{
                imageViewDepart.setImageDrawable(getDrawable(R.drawable.imagemur));
            }
            imageViewDepart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


    public void affichePieceArrivee(){
        if(pieceArrivee != null && orientationPieceArrivee != null){
            Mur mur = pieceArrivee.getMurOrientation(orientationPieceArrivee);
            FileInputStream fis = null;
            try {
                fis = openFileInput(mur.getId()+".data");
            } catch (FileNotFoundException e) {
                //throw new RuntimeException(e);
            }
            if (fis != null) {
                Bitmap bm = BitmapFactory.decodeStream(fis);

                imageViewDepart.setImageBitmap(bm);
            }else{
                imageViewDepart.setImageDrawable(getDrawable(R.drawable.imagemur));
            }
        }
    }

    public void setSDepart(View view) {
        pieceEnCours = pieceDepart;
        affichePieceDepart();
    }

    public void setSArrivee(View view) {
        pieceEnCours = pieceArrivee;
        affichePieceArrivee();
    }
}