package com.tennis.cli.tenn.cus.tennisapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tennis.cli.tenn.cus.tennisapp.Application.TennisApp;
import com.tennis.cli.tenn.cus.tennisapp.Application.TennisCommon;
import com.tennis.cli.tenn.cus.tennisapp.Fragments.Sheets.BottomSheetCredit;
import com.tennis.cli.tenn.cus.tennisapp.Fragments.Sheets.BottomSheetPaypal;
import com.tennis.cli.tenn.cus.tennisapp.Models.CreditModel;
import com.tennis.cli.tenn.cus.tennisapp.Models.PaypalModel;
import com.tennis.cli.tenn.cus.tennisapp.R;

import java.util.HashMap;
import java.util.UUID;

public class SignUpActivity extends TennisCommon implements BottomSheetPaypal.OnBottomSheetClick,BottomSheetCredit.OnBottomSheetCreditClick {

    private String paypalOrCredit = "";
    private TextInputLayout fNameLyt,sNameLyt,emailLyt,cEmailLyt,passLyt,cPassLyt;
    private TextInputEditText firNameEdit,secNameEdit,emailEdit,cEmailEdit,passEdit,cPassEdit;
    private RadioGroup genderGrp;
    private String sGender = "";
    private AppCompatButton finishBtn;
    private AppCompatRadioButton payPalRadBtn,creditCardRadBtn;
    private BottomSheetPaypal bottomSheetFragment;
    private BottomSheetCredit bottomSheetFragmentSheetCredit;

    private AppCompatImageView gifView;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore firebaseFirestore;
    private LottieAnimationView animationViewLottie;

    private String user_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // tO SHOW ACTIVITY ON FULL SCREEN //
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        gifView = findViewById(R.id.gif);

        Glide.with(SignUpActivity.this).load("https://i.pinimg.com/originals/72/d0/87/72d08779db9cbf8b94312ba301b737eb.gif").into(gifView);
//        gifView.loadGIFResource(LoginActivity.this,R.drawable.start);

        animationViewLottie = findViewById(R.id.animation_view);


        fNameLyt = findViewById(R.id.fn_layout);
        firNameEdit = findViewById(R.id.fname);

        sNameLyt = findViewById(R.id.sn_layout);
        secNameEdit = findViewById(R.id.sname);

        emailLyt = findViewById(R.id.email_layout);
        emailEdit = findViewById(R.id.email);

        cEmailLyt = findViewById(R.id.cemail_layout);
        cEmailEdit = findViewById(R.id.cemail);

        passLyt = findViewById(R.id.pass_layout);
        passEdit = findViewById(R.id.password);

        cPassLyt = findViewById(R.id.cpass_layout);
        cPassEdit = findViewById(R.id.cpassword);


        firNameEdit.addTextChangedListener(fWatcher);
        secNameEdit.addTextChangedListener(sWatcher);
        emailEdit.addTextChangedListener(emailWatcher);
        cEmailEdit.addTextChangedListener(cEmailWatcher);
        passEdit.addTextChangedListener(passWatcher);
        cPassEdit.addTextChangedListener(cPassWatcher);

        genderGrp = findViewById(R.id.sex);

        finishBtn = findViewById(R.id.finish_btn);

        payPalRadBtn = findViewById(R.id.rad_paypal);
        creditCardRadBtn = findViewById(R.id.rad_c_card);


        creditCardRadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                paypalOrCredit  = "credit";

                bottomSheetFragmentSheetCredit = new BottomSheetCredit();
                bottomSheetFragmentSheetCredit.setCancelable(false);
                bottomSheetFragmentSheetCredit.show(getSupportFragmentManager(), bottomSheetFragmentSheetCredit.getTag());
                bottomSheetFragmentSheetCredit.setOnBottomSheetCreditClick(SignUpActivity.this);


            }
        });

        payPalRadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                paypalOrCredit  = "paypal";

                bottomSheetFragment = new BottomSheetPaypal();
                bottomSheetFragment.setCancelable(false);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                bottomSheetFragment.setOnBottomSheetClick(SignUpActivity.this);

            }
        });


        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fName = firNameEdit.getText().toString().trim();
                String sName = secNameEdit.getText().toString().trim();

                String email = emailEdit.getText().toString().trim();
                String cEmail = cEmailEdit.getText().toString().trim();

                String pass = passEdit.getText().toString().trim();
                String cPass = cPassEdit.getText().toString().trim();

                int genderId = genderGrp.getCheckedRadioButtonId();

                if (genderId == R.id.male){

                    sGender = "M";

                }else{

                    sGender = "F";

                }

                if (TextUtils.isEmpty(fName)){
                    fNameLyt.setError("First Name is missing");
                    return;
                }

                fNameLyt.setError(null);

                if (TextUtils.isEmpty(sName)){
                    sNameLyt.setError("Second Name is missing");
                    return;
                }

                sNameLyt.setError(null);

                if (TextUtils.isEmpty(sGender)){
                    inFormUser(SignUpActivity.this,"Choose your gender");
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    emailLyt.setError("Email is missing");
                    return;
                }

                if (!isValidEmail(email)){
                    emailLyt.setError("Email not valid");
                    return;
                }

                emailLyt.setError(null);

                if (TextUtils.isEmpty(cEmail)){
                    cEmailLyt.setError("confirm email box  is empty");
                    return;
                }

                if (!isValidEmail(cEmail)){
                    cEmailLyt.setError("Confirm Email not valid");
                    return;
                }

                cEmailLyt.setError(null);

                if (!email.contentEquals(cEmail)){
                    inFormUser(SignUpActivity.this,"Email mismatches");
                    return;
                }


                if (TextUtils.isEmpty(pass)){
                    passLyt.setError("Password is missing");
                    return;
                }


                passLyt.setError(null);

                if (TextUtils.isEmpty(cPass)){
                    cPassLyt.setError("confirm password box  is empty");
                    return;
                }

                cPassLyt.setError(null);

                if (!pass.contentEquals(cPass)){
                    inFormUser(SignUpActivity.this,"Password mismatches");
                    return;
                }


                if (TextUtils.isEmpty(paypalOrCredit)){
                    inFormUser(SignUpActivity.this,"Choose Payment Method");
                    return;
                }

                if (paypalOrCredit.contentEquals("paypal")){

                    if (TennisApp.getPaypalModel()== null){
                        inFormUser(SignUpActivity.this,"Please enter your paypal details to continue to payment process");

                    }else{

                        // Start Uploading the data to firestore  //
                        if (isNetWorkAvailable(SignUpActivity.this)){

                            finishBtn.setEnabled(false);
                            exitReveal(finishBtn);

                            animationViewLottie.setVisibility(View.VISIBLE);
                            animationViewLottie.setAnimation(R.raw.loading);
                            animationViewLottie.playAnimation();

                            if (mAuth != null){

                                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if(task.isSuccessful()){

                                            mUser = mAuth.getCurrentUser();
                                            user_id = mUser.getUid();

                                            System.out.println("USER_ID: " + user_id);

                                            if (firebaseFirestore != null){

                                                HashMap<String,Object> userMap = new HashMap<>();
                                                userMap.put("firstname",fName);
                                                userMap.put("secondname",sName);
                                                userMap.put("gender",sGender);
                                                userMap.put("email",email);
                                                userMap.put("password",pass);

                                                firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()){

                                                            HashMap<String,Object> paypalMap = new HashMap<>();
                                                            paypalMap.put("paypalObj",TennisApp.getPaypalModel());

                                                            firebaseFirestore.collection("Users").document(user_id)
                                                                    .collection("PaymentMethods").document(user_id)
                                                                    .collection("paypalmethod").document("paypal").set(paypalMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            if (task.isSuccessful()){

                                                                                if (animationViewLottie != null) {

                                                                                animationViewLottie.setVisibility(View.GONE);
                                                                            }
                                                                                finishBtn.setEnabled(true);
                                                                                enterReveal(finishBtn);

                                                                                inFormUser(SignUpActivity.this,"Successfully registered");
                                                                                gotoLoginActivity();

                                                                            }else{

                                                                                if (animationViewLottie != null) {

                                                                                    animationViewLottie.setVisibility(View.GONE);
                                                                                }

                                                                                finishBtn.setEnabled(true);
                                                                                enterReveal(finishBtn);

                                                                                String error  =  task.getException().getMessage();
                                                                                showSnackBar(finishBtn,error);

                                                                            }
                                                                        }
                                                                    });
                                                        }else{

                                                            if (animationViewLottie != null) {

                                                                animationViewLottie.setVisibility(View.GONE);
                                                            }

                                                            finishBtn.setEnabled(true);
                                                            enterReveal(finishBtn);

                                                            String error  =  task.getException().getMessage();
                                                            showSnackBar(finishBtn,error);


                                                        }
                                                    }
                                                });
                                            }


                                        }else{

                                            if (animationViewLottie != null) {

                                                animationViewLottie.setVisibility(View.GONE);
                                            }

                                            finishBtn.setEnabled(true);
                                            enterReveal(finishBtn);

                                            String error  =  task.getException().getMessage();
                                            showSnackBar(finishBtn,error);

                                        }
                                    }
                                });
                            }

                        }

                    }
                }


                if (paypalOrCredit.contentEquals("credit")){

                    if (TennisApp.getCreditModel()== null){

                        inFormUser(SignUpActivity.this,"Please enter your credit card to continue to payment process");

                    }else{

                        // Start Uploading the data to firestore  //
                        if (isNetWorkAvailable(SignUpActivity.this)){

                            finishBtn.setEnabled(false);
                            exitReveal(finishBtn);

                            animationViewLottie.setVisibility(View.VISIBLE);
                            animationViewLottie.setAnimation(R.raw.loading);
                            animationViewLottie.playAnimation();

                            if (mAuth != null){

                                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if(task.isSuccessful()){

                                            mUser = mAuth.getCurrentUser();
                                            user_id = mUser.getUid();

                                            System.out.println("USER_ID: " + user_id);

                                            if (firebaseFirestore != null){

                                                HashMap<String,Object> userMap = new HashMap<>();
                                                userMap.put("firstname",fName);
                                                userMap.put("secondname",sName);
                                                userMap.put("gender",sGender);
                                                userMap.put("email",email);
                                                userMap.put("password",pass);

                                                firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()){

                                                            HashMap<String,Object> paypalMap = new HashMap<>();
                                                            paypalMap.put("creditObj",TennisApp.getCreditModel());

                                                            String unq_id = "credit" + UUID.randomUUID().toString() + System.currentTimeMillis();

                                                            firebaseFirestore.collection("Users").document(user_id)
                                                                    .collection("PaymentMethods").document(user_id)
                                                                    .collection("creditmethod").document(unq_id).set(paypalMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            if (task.isSuccessful()){

                                                                                if (animationViewLottie != null) {

                                                                                    animationViewLottie.setVisibility(View.GONE);
                                                                                }
                                                                                finishBtn.setEnabled(true);
                                                                                enterReveal(finishBtn);

                                                                                inFormUser(SignUpActivity.this,"Successfully registered");
                                                                                gotoLoginActivity();

                                                                            }else{

                                                                                if (animationViewLottie != null) {

                                                                                    animationViewLottie.setVisibility(View.GONE);
                                                                                }

                                                                                finishBtn.setEnabled(true);
                                                                                enterReveal(finishBtn);

                                                                                String error  =  task.getException().getMessage();
                                                                                showSnackBar(finishBtn,error);

                                                                            }
                                                                        }
                                                                    });
                                                        }else{

                                                            if (animationViewLottie != null) {

                                                                animationViewLottie.setVisibility(View.GONE);
                                                            }

                                                            finishBtn.setEnabled(true);
                                                            enterReveal(finishBtn);

                                                            String error  =  task.getException().getMessage();
                                                            showSnackBar(finishBtn,error);


                                                        }
                                                    }
                                                });
                                            }


                                        }else{

                                            if (animationViewLottie != null) {

                                                animationViewLottie.setVisibility(View.GONE);
                                            }

                                            finishBtn.setEnabled(true);
                                            enterReveal(finishBtn);

                                            String error  =  task.getException().getMessage();
                                            showSnackBar(finishBtn,error);

                                        }
                                    }
                                });
                            }

                        }

                    }
                }

            }
        });
    }

    private void gotoLoginActivity() {

        Intent mainIntent = new Intent(SignUpActivity.this, LoginActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void onLoginClicked(AppCompatButton btn, String email, String password) {

        if (bottomSheetFragment != null){

            bottomSheetFragment.dismiss();
            bottomSheetFragment = null;
        }

        PaypalModel paypalModel = new PaypalModel(email,password);
        TennisApp.setPaypalModel(paypalModel);

        inFormUser(SignUpActivity.this,"Payment method accepted");
    }

    @Override
    public void onBackPaypalClicked() {

        if (bottomSheetFragment != null){

            bottomSheetFragment.dismiss();
            bottomSheetFragment = null;
        }

        if (!TextUtils.isEmpty(paypalOrCredit) && paypalOrCredit.contentEquals("paypal")){
            TennisApp.setPaypalModel(null);
        }

    }

    @Override
    public void onCreditAddClicked(AppCompatButton view,String type,String cNum,String mm,String yy, String cvv,String fullName,String address) {

        if (bottomSheetFragmentSheetCredit != null){

            bottomSheetFragmentSheetCredit.dismiss();
            bottomSheetFragmentSheetCredit = null;
        }

        CreditModel creditModel = new CreditModel(type,cNum,mm,yy,cvv,fullName,address);
        TennisApp.setCreditModel(creditModel);

        inFormUser(SignUpActivity.this,"Payment method accepted");


    }

    @Override
    public void onCreditBackIsClicked() {

        if (bottomSheetFragmentSheetCredit != null){

            bottomSheetFragmentSheetCredit.dismiss();
            bottomSheetFragmentSheetCredit = null;
        }

        if (!TextUtils.isEmpty(paypalOrCredit) && paypalOrCredit.contentEquals("credit")){
            TennisApp.setCreditModel(null);
        }
    }


    TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String s = charSequence.toString();

            if (!isValidEmail(s)){

                emailLyt.setError("Email not valid");

            }else{

                emailLyt.setError(null);

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    TextWatcher cEmailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String s = charSequence.toString();

            if (!isValidEmail(s)){

                cEmailLyt.setError("Email not valid");

            }else{

                cEmailLyt.setError(null);

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    TextWatcher passWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String s = charSequence.toString();

            if (s.contains(" ")){
                passLyt.setError("Remove spaces from pass");
                return;
            }

            if (s.length() < 6){
                passLyt.setError("atleast 6 characters");
                return;
            }

            passLyt.setError(null);

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    TextWatcher cPassWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String s = charSequence.toString();

            if (s.contains(" ")){
                cPassLyt.setError("Remove spaces from pass");
                return;
            }

            if (s.length() < 6){
                cPassLyt.setError("atleast 6 characters");
                return;
            }

            cPassLyt.setError(null);

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };




    TextWatcher fWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String s = charSequence.toString();

            if (TextUtils.isEmpty(s)){
                fNameLyt.setError("First Name is missing");
                return;
            }

            fNameLyt.setError(null);

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };



    TextWatcher sWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String s = charSequence.toString();

            if (TextUtils.isEmpty(s)){
                sNameLyt.setError("Second Name is missing");
                return;
            }

            sNameLyt.setError(null);


        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };





}