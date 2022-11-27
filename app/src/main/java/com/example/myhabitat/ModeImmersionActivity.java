package com.example.myhabitat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.*;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import habitat.*;
import outils.GrapheHabitat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class ModeImmersionActivity extends AppCompatActivity implements SensorEventListener {
    private Habitat habitat;
    private Mur murEnCours;
    private Piece pieceEnCours;
    private TextView textViewPiece;
    private SensorManager sensorManager;
    private ImageView imageViewBoussole;
    private float debut = 0;
    private ImageView imageViewMur;
    private SurfaceView surfaceView;
    private Paint myPaint;
    private Canvas canvas;
    private ArrayList<Rect> rectangles;
    private HashMap<Rect, Piece> pieceArriveeRect;
    private Piece goToPiece;
    private Rect goToRect;
    private GrapheHabitat grapheHabitat;
    private ImageView imageViewFleche;

    /**
     * onCreate de ModeImmersionActivity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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

        grapheHabitat = new GrapheHabitat(habitat);
        imageViewFleche = findViewById(R.id.imageViewFleche);
        imageViewFleche.setVisibility(View.INVISIBLE);

        rectangles = new ArrayList<Rect>();
        pieceArriveeRect = new HashMap<Rect, Piece>();

        pieceEnCours = habitat.getPieces().get(0);
        affichePiece(pieceEnCours);
        imageViewBoussole = findViewById(R.id.imageViewBoussole);
        imageViewMur = findViewById(R.id.imageViewMur);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceView.setZOrderOnTop(true);
        surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);

        canvas = surfaceView.getHolder().lockCanvas();

        myPaint = new Paint();
        myPaint.setStrokeWidth(5);
        myPaint.setColor(Color.BLUE);
        myPaint.setStyle(Paint.Style.STROKE);


        imageViewMur.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean res = false;
                Rect r = null;
                int touchX = (int) event.getX();
                int touchY = (int) event.getY();
                for(Rect rect : rectangles){
                    if(rect.contains(touchX,touchY)){
                        res = true;
                        r = rect;
                    }
                }
                if(res && r!=null){
                    //Log.i("testTouchRect", "je touche rect="+r.toString());
                    //Log.i("testHM", pieceArriveeRect+"");
                    pieceEnCours = pieceArriveeRect.get(r);
                    afficheMur();
                }
                return true;
            }
        });
    }

    /**
     * Cree le menu de l'activite
     * @param menu The options menu in which you place your items.
     *
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_immersion, menu);

        int i =0;
        for(Piece piece : habitat.getPieces()){
            menu.add(piece.getNom());
            menu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    affichePiece(piece);
                    return false;
                }
            });
            i++;
        }

        return true;
    }

    /**
     * selectionne la piece en cours
     * @param piece
     */
    public void affichePiece(Piece piece){
        pieceEnCours = piece;
        imageViewMur = findViewById(R.id.imageViewMur);
        murEnCours = piece.getMurs().get(0);

        afficheMur();
    }

    /**
     * Affiche le mur en cours
     */
    public void afficheMur(){
        textViewPiece = findViewById(R.id.textViewPiece);
        textViewPiece.setTextSize(25);
        textViewPiece.setText(pieceEnCours.getNom());
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
            //Log.i("testDrawable", "pas de photo");
            imageViewMur.setImageDrawable(getDrawable(R.drawable.imagemur));
        }
        //Log.i("testDate", murEnCours.getDate()+"test");
        TextView textViewDate = findViewById(R.id.textViewDate);
        textViewDate.setText(murEnCours.getDate());

        //On met à jour la piece suivante si mode "aller a une piece"
        if(goToPiece != null){
            goTo();
        }

        afficheOuvertures();
    }


    /**
     * Affiche l'ensemble des ouvertures du mur courant
     */
    public void afficheOuvertures(){
        ArrayList<Ouverture> ouvertures = habitat.getOuvertureDeMur(murEnCours);
        //Log.i("testOuvertures", ouvertures+"");
        rectangles.clear();
        pieceArriveeRect.clear();

        if(ouvertures.isEmpty()){
            //Il n'y a pas d'ouvertures à afficher
            try {
                canvas = surfaceView.getHolder().lockCanvas();
                synchronized (surfaceView.getHolder()) {
                    canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    surfaceView.getHolder().unlockCanvasAndPost(canvas);
                }
            }
        }else{
            for(Ouverture ouverture : ouvertures) {
                //Si mur de depart
                if (murEnCours.getId() == ouverture.getMurDepart().getId()) {
                    rectangles.add(ouverture.getRectDepart());
                    //Log.i("testGetPiece", ouverture.getMurArrivee()+"");
                    pieceArriveeRect.put(ouverture.getRectDepart(), ouverture.getMurArrivee().getPiece());
                } else {
                    //Si mur d'arrivee
                    rectangles.add(ouverture.getRectArrivee());
                    pieceArriveeRect.put(ouverture.getRectArrivee(), ouverture.getMurDepart().getPiece());
                }
            }
            try {
                canvas = surfaceView.getHolder().lockCanvas();
                synchronized (surfaceView.getHolder()) {
                    canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                    for(Rect rect : rectangles){
                        if(rect.equals(goToRect)){
                            Paint newPaint = new Paint();
                            newPaint.setStrokeWidth(5);
                            newPaint.setColor(Color.RED);
                            newPaint.setStyle(Paint.Style.STROKE);
                            canvas.drawRect(rect, newPaint);
                        }else {
                            canvas.drawRect(rect, myPaint);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    surfaceView.getHolder().unlockCanvasAndPost(canvas);
                }
            }

        }
    }

    /**
     * Gere la sensibilite du Sensor = affichage de la mur + màj mur en cours
     * @param event the {@link android.hardware.SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        // On récupère l'angle
        float angle = -(Math.round(event.values[0]));

        //On créé l'animation de rotation
        RotateAnimation rotateAnimation = new RotateAnimation(debut, angle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(200);

        //Et on la lance
        if(imageViewBoussole == null) {
            imageViewBoussole = findViewById(R.id.imageViewBoussole);
        }

        imageViewBoussole.startAnimation(rotateAnimation);


        Mur newMur = null;
        if(angle<(-45) && angle>=(-135)){
            newMur = pieceEnCours.getMurOrientation(Orientation.EST);
        } else if (angle<(-135) && angle>=(-225)) {
            newMur = pieceEnCours.getMurOrientation(Orientation.SUD);
        } else if (angle<(-225) && angle>=(-315)) {
            newMur = pieceEnCours.getMurOrientation(Orientation.OUEST);
        }else{
            newMur = pieceEnCours.getMurOrientation(Orientation.NORD);
        }
        if(newMur != murEnCours){
            murEnCours = newMur;
            afficheMur();
        }


        //Maj de l'angle de depart
        debut = angle;

        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Fonction du sensor
     * @param sensor
     * @param accuracy The new accuracy of this sensor, one of
     *         {@code SensorManager.SENSOR_STATUS_*}
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Fonction appelee lors du changement d'activite
     * Permet de desactiver le Sensor
     */
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    /**
     * Fonction appelee lors de l'arrive dans l'activite
     * Permet d'activer le Sensor
     */
    @Override
    protected void onResume() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
    }

    public void goTo(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ModeImmersionActivity.this);
        alertDialog.setTitle("Choisi une piece où aller");
        Spinner spinner = new Spinner(ModeImmersionActivity.this);
        String[] arrayPieces = new String[habitat.getPieces().size()];
        for(int i=0; i<habitat.getPieces().size(); i++){
            arrayPieces[i] = habitat.getPieces().get(i).getNom();
        }
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayPieces);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                goToPiece = habitat.getPieces().get(position);
                //Log.i("testGoTo", "je choisis la piece " + goTo.getNom());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        alertDialog.setView(spinner);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goTo();
            }
        });
        alertDialog.show();
    }

    private void goTo() {
        if(!pieceEnCours.equals(goToPiece)) {
            ArrayList<Piece> chemin = grapheHabitat.getPlusCourtChemin(pieceEnCours, goToPiece);

            //On recupere la prochaine piece et le mur à partir duquel on y accede
            Piece pieceEnsuite = chemin.get(1);

            //On recuperera la direction pour la fleche
            Orientation orientation = null;
            for (Ouverture ouverture : habitat.getOuvertures()) {
                Piece pieceDepart = ouverture.getMurDepart().getPiece();
                Piece pieceArrivee = ouverture.getMurArrivee().getPiece();
                Log.i("testGraphe", "pieceDepart = " + pieceDepart.getNom() + " pieceArrivee = " + pieceArrivee.getNom() + " pieceEnCours = " + pieceEnCours.getNom() + " pieceEnsuite = " + pieceEnsuite.getNom());
                if (pieceDepart.equals(pieceEnCours) && pieceArrivee.equals(pieceEnsuite)) {
                    goToRect = ouverture.getRectDepart();
                    orientation = ouverture.getMurDepart().getOrientation();
                } else if (pieceArrivee.equals(pieceEnCours) && pieceDepart.equals(pieceEnsuite)) {
                    goToRect = ouverture.getRectArrivee();
                    orientation = ouverture.getMurArrivee().getOrientation();
                }
            }
            afficheOuvertures();

            //Log.i("testFleche", orientation.toString());
            imageViewFleche.setVisibility(View.VISIBLE);
            imageViewFleche.animate().cancel();
            switch (orientation){
                case EST:
                    switch(murEnCours.getOrientation()){
                        case OUEST:
                            imageViewFleche.animate().rotation(180);
                            break;
                        case SUD:
                            imageViewFleche.animate().rotation(-90);
                            break;
                        case NORD:
                            imageViewFleche.animate().rotation(90);
                            break;
                        default:
                            imageViewFleche.animate().rotation(0);
                            break;
                    }
                    break;
                case OUEST:
                    switch(murEnCours.getOrientation()){
                        case EST:
                            imageViewFleche.animate().rotation(180);
                            break;
                        case SUD:
                            imageViewFleche.animate().rotation(90);
                            break;
                        case NORD:
                            imageViewFleche.animate().rotation(-90);
                            break;
                        default:
                            imageViewFleche.animate().rotation(0);
                            break;
                    }
                    break;
                case SUD:
                    switch(murEnCours.getOrientation()){
                        case EST:
                            imageViewFleche.animate().rotation(90);
                            break;
                        case OUEST:
                            imageViewFleche.animate().rotation(-90);
                            break;
                        case NORD:
                            imageViewFleche.animate().rotation(180);
                            break;
                        default:
                            imageViewFleche.animate().rotation(0);
                            break;
                    }
                    break;
                case NORD:
                    switch(murEnCours.getOrientation()){
                        case EST:
                            imageViewFleche.animate().rotation(-90);
                            break;
                        case OUEST:
                            imageViewFleche.animate().rotation(90);
                            break;
                        case SUD:
                            imageViewFleche.animate().rotation(180);
                            break;
                        default:
                            imageViewFleche.animate().rotation(0);
                            break;
                    }
                    break;
            }

        }else{
            goToRect = null;
            goToPiece = null;
            imageViewFleche.setVisibility(View.INVISIBLE);
            Toast.makeText(getBaseContext(), "Vous êtes arrivé !", Toast.LENGTH_SHORT).show();
        }
    }
}