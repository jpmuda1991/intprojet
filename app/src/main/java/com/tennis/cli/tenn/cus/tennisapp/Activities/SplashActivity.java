package com.tennis.cli.tenn.cus.tennisapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.tennis.cli.tenn.cus.tennisapp.Application.TennisApp;
import com.tennis.cli.tenn.cus.tennisapp.Models.StatisModels.Top;
import com.tennis.cli.tenn.cus.tennisapp.Models.StatisModels.TopTwnModels;
import com.tennis.cli.tenn.cus.tennisapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private SharedPreferences login_preferences;
    private boolean isDefaultValue;
    private ConstraintLayout constraintLayout;
    private FirebaseFirestore firebaseFirestore;
//    private String image,name;

    private LottieAnimationView animationViewLottie;
    private List<TopTwnModels> topTwnModelsList;

//    private String topMaleJson = "{\"rankings\": [{\"name\":\"Medvedev, Daniil\",\"dob\":\"1996-02-11\",\"abbr\":\"MED\"},\n" +
//            "{\"name\":\"Zverev, Alexander\",\"dob\":\"1997-04-20\",\"abbr\":\"ZVE\"},\n" +
//            "{\"name\":\"Nadal, Rafael\",\"dob\":\"1986-06-03\",\"abbr\":\"NAD\"},\n" +
//            "{\"name\":\"Alcaraz, Carlos\",\"dob\":\"2003-05-05\",\"abbr\":\"ALC\"},\n" +
//            "{\"name\":\"Tsitsipas, Stefanos\",\"dob\":\"1998-08-12\",\"abbr\":\"TSI\"},\n" +
//            "{\"name\":\"Djokovic, Novak\",\"dob\":\"1987-05-22\",\"abbr\":\"DJO\"},\n" +
//            "{\"name\":\"Ruud, Casper\",\"dob\":\"1998-12-22\",\"abbr\":\"RUU\"},\n" +
//            "{\"name\":\"Rublev, Andrey\",\"dob\":\"1997-10-20\",\"abbr\":\"RUB\"},\n" +
//            "{\"name\":\"Auger-Aliassime, Felix\",\"dob\":\"2000-08-08\",\"abbr\":\"AUG\"},\n" +
//            "{\"name\":\"Sinner, Jannik\",\"dob\":\"2001-08-16\",\"abbr\":\"SIN\"},\n" +
//            "{\"name\":\"Hurkacz, Hubert\",\"dob\":\"1997-02-11\",\"abbr\":\"HUR\"},\n" +
//            "{\"name\":\"Norrie, Cameron\",\"dob\":\"1995-08-23\",\"abbr\":\"NOR\"},\n" +
//            "{\"name\":\"Fritz, Taylor\",\"dob\":\"1997-10-28\",\"abbr\":\"FRI\"},\n" +
//            "{\"name\":\"Berrettini, Matteo\",\"dob\":\"1996-04-12\",\"abbr\":\"BER\"},\n" +
//            "{\"name\":\"Schwartzman, Diego\",\"dob\":\"1992-08-16\",\"abbr\":\"SWM\"},\n" +
//            "{\"name\":\"Cilic, Marin\",\"dob\":\"1988-09-28\",\"abbr\":\"CIL\"},\n" +
//            "{\"name\":\"Opelka, Reilly\",\"dob\":\"1997-08-28\",\"abbr\":\"OPE\"},\n" +
//            "{\"name\":\"Bautista Agut, Roberto\",\"dob\":\"1988-04-14\",\"abbr\":\"BAU\"},\n" +
//            "{\"name\":\"Dimitrov, Grigor\",\"dob\":\"1991-05-16\",\"abbr\":\"DIM\"},\n" +
//            "{\"name\":\"Monfils, Gael\",\"dob\":\"1986-09-01\",\"abbr\":\"MON\"},\n" +
//            "{\"name\":\"Medvedev, Daniil\",\"dob\":\"1996-02-11\",\"abbr\":\"MED\"},\n" +
//            "{\"name\":\"Zverev, Alexander\",\"dob\":\"1997-04-20\",\"abbr\":\"ZVE\"},\n" +
//            "{\"name\":\"Nadal, Rafael\",\"dob\":\"1986-06-03\",\"abbr\":\"NAD\"},\n" +
//            "{\"name\":\"Alcaraz, Carlos\",\"dob\":\"2003-05-05\",\"abbr\":\"ALC\"},\n" +
//            "{\"name\":\"Tsitsipas, Stefanos\",\"dob\":\"1998-08-12\",\"abbr\":\"TSI\"},\n" +
//            "{\"name\":\"Djokovic, Novak\",\"dob\":\"1987-05-22\",\"abbr\":\"DJO\"},\n" +
//            "{\"name\":\"Ruud, Casper\",\"dob\":\"1998-12-22\",\"abbr\":\"RUU\"},\n" +
//            "{\"name\":\"Rublev, Andrey\",\"dob\":\"1997-10-20\",\"abbr\":\"RUB\"},\n" +
//            "{\"name\":\"Auger-Aliassime, Felix\",\"dob\":\"2000-08-08\",\"abbr\":\"AUG\"},\n" +
//            "{\"name\":\"Sinner, Jannik\",\"dob\":\"2001-08-16\",\"abbr\":\"SIN\"},\n" +
//            "{\"name\":\"Hurkacz, Hubert\",\"dob\":\"1997-02-11\",\"abbr\":\"HUR\"},\n" +
//            "{\"name\":\"Norrie, Cameron\",\"dob\":\"1995-08-23\",\"abbr\":\"NOR\"},\n" +
//            "{\"name\":\"Fritz, Taylor\",\"dob\":\"1997-10-28\",\"abbr\":\"FRI\"},\n" +
//            "{\"name\":\"Berrettini, Matteo\",\"dob\":\"1996-04-12\",\"abbr\":\"BER\"},\n" +
//            "{\"name\":\"Schwartzman, Diego\",\"dob\":\"1992-08-16\",\"abbr\":\"SWM\"},\n" +
//            "{\"name\":\"Cilic, Marin\",\"dob\":\"1988-09-28\",\"abbr\":\"CIL\"},\n" +
//            "{\"name\":\"Opelka, Reilly\",\"dob\":\"1997-08-28\",\"abbr\":\"OPE\"},\n" +
//            "{\"name\":\"Bautista Agut, Roberto\",\"dob\":\"1988-04-14\",\"abbr\":\"BAU\"},\n" +
//            "{\"name\":\"Dimitrov, Grigor\",\"dob\":\"1991-05-16\",\"abbr\":\"DIM\"},\n" +
//            "{\"name\":\"Monfils, Gael\",\"dob\":\"1986-09-01\",\"abbr\":\"MON\"},\n" +
//            "{\"name\":\"Swiatek, Iga\",\"dob\":\"2001-05-31\",\"abbr\":\"SWI\"},\n" +
//            "{\"name\":\"Kontaveit, Anett\",\"dob\":\"1995-12-24\",\"abbr\":\"KON\"},\n" +
//            "{\"name\":\"Sakkari, Maria\",\"dob\":\"1995-07-25\",\"abbr\":\"SAK\"},\n" +
//            "{\"name\":\"Badosa, Paula\",\"dob\":\"1997-11-15\",\"abbr\":\"BAS\"},\n" +
//            "{\"name\":\"Jabeur, Ons\",\"dob\":\"1994-08-28\",\"abbr\":\"JAB\"},\n" +
//            "{\"name\":\"Sabalenka, Aryna\",\"dob\":\"1998-05-05\",\"abbr\":\"SAB\"},\n" +
//            "{\"name\":\"Pegula, Jessica\",\"dob\":\"1994-02-24\",\"abbr\":\"PEG\"},\n" +
//            "{\"name\":\"Muguruza, Garbine\",\"dob\":\"1993-10-08\",\"abbr\":\"MUG\"},\n" +
//            "{\"name\":\"Collins, Danielle\",\"dob\":\"1993-12-13\",\"abbr\":\"COL\"},\n" +
//            "{\"name\":\"Raducanu, Emma\",\"dob\":\"2002-11-13\",\"abbr\":\"RAD\"},\n" +
//            "{\"name\":\"Gauff, Cori\",\"dob\":\"2004-03-13\",\"abbr\":\"GAU\"},\n" +
//            "{\"name\":\"Kasatkina, Daria\",\"dob\":\"1997-05-07\",\"abbr\":\"KAS\"},\n" +
//            "{\"name\":\"Bencic, Belinda\",\"dob\":\"1997-03-10\",\"abbr\":\"BEN\"},\n" +
//            "{\"name\":\"Fernandez, Leylah Annie\",\"dob\":\"2002-09-06\",\"abbr\":\"FER\"},\n" +
//            "{\"name\":\"Pliskova, Karolina\",\"dob\":\"1992-03-21\",\"abbr\":\"PLI\"},\n" +
//            "{\"name\":\"Halep, Simona\",\"dob\":\"1991-09-27\",\"abbr\":\"HAL\"},\n" +
//            "{\"name\":\"Ostapenko, Jelena\",\"dob\":\"1997-06-08\",\"abbr\":\"OST\"},\n" +
//            "{\"name\":\"Krejcikova, Barbora\",\"dob\":\"1995-12-18\",\"abbr\":\"KRE\"},\n" +
//            "{\"name\":\"Kudermetova, Veronika\",\"dob\":\"1997-04-24\",\"abbr\":\"KUD\"},\n" +
//            "{\"name\":\"Azarenka, Victoria\",\"dob\":\"1989-07-31\",\"abbr\":\"AZA\"}]}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSharedPreferences();

        firebaseFirestore = FirebaseFirestore.getInstance();

        // tO SHOW ACTIVITY ON FULL SCREEN //
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

        animationViewLottie = findViewById(R.id.animation_view);

        topTwnModelsList = new ArrayList<>();

        animationViewLottie.setAnimation(R.raw.tennis_loading);
        animationViewLottie.playAnimation();

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {

                    sleep(4000);

                    if (isDefaultValue){

                        mAuth = FirebaseAuth.getInstance();

                        gotoMainActivity();


//                        try {
//
//                            JSONObject jsonObject = new JSONObject(topMaleJson);
//                            JSONArray arr = jsonObject.getJSONArray("rankings");
//
//                            System.out.println("ARRAY LENGTH IS: " + arr.length());
//                            for (int i = 0 ; i < arr.length(); i++){
//
//                                JSONObject jsonObject1 = (JSONObject) arr.get(i);
//                                String name = jsonObject1.getString("name");
//                                String dob = jsonObject1.getString("dob");
//                                String abbr = jsonObject1.getString("abbr");
//
//                                topTwnModelsList.add(new TopTwnModels(name,dob,abbr));
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                        TennisApp.setTopTwnModelsList(topTwnModelsList);



                    }else{

                        gotoLoginActivity();


//                        try {
//
//                            JSONObject jsonObject = new JSONObject(topMaleJson);
//                            JSONArray arr = jsonObject.getJSONArray("rankings");
//
//                            System.out.println("ARRAY LENGTH IS: " + arr.length());
//                            for (int i = 0 ; i < arr.length(); i++){
//
//                                JSONObject jsonObject1 = (JSONObject) arr.get(i);
//                                String name = jsonObject1.getString("name");
//                                String dob = jsonObject1.getString("dob");
//                                String abbr = jsonObject1.getString("abbr");
//
//                                topTwnModelsList.add(new TopTwnModels(name,dob,abbr));
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                        TennisApp.setTopTwnModelsList(topTwnModelsList);
//
////                        gotoMainActivity();

                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }


    private void gotoMainActivity() {

        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        finish();
    }

    private void gotoLoginActivity() {

        Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
        finish();
    }


    public void getSharedPreferences(){

        login_preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        isDefaultValue = login_preferences.getBoolean("FLAG",false);

    }


}