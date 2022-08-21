package com.tennis.cli.tenn.cus.tennisapp.Fragments.Sheets;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tennis.cli.tenn.cus.tennisapp.Activities.SignUpActivity;
import com.tennis.cli.tenn.cus.tennisapp.Application.TennisApp;
import com.tennis.cli.tenn.cus.tennisapp.Application.TennisCommon;
import com.tennis.cli.tenn.cus.tennisapp.Models.PaypalModel;
import com.tennis.cli.tenn.cus.tennisapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class BottomSheetPaypal extends BottomSheetDialogFragment implements View.OnClickListener {

    private PaypalModel paypalModel;
    private View itemView;
    private TennisCommon tennisCommon;
    private TextInputLayout emailLyt,passLyt;
    private TextInputEditText emailEdit,passEdit;
    private AppCompatButton loginBtn;
    private AppCompatImageView back;


    public OnBottomSheetClick onBottomSheetClick;

    public interface OnBottomSheetClick{

        void onLoginClicked(AppCompatButton view,String email,String password);
        void onBackPaypalClicked();

    }

    public void setOnBottomSheetClick(OnBottomSheetClick onBottomSheetClick) {
        this.onBottomSheetClick = onBottomSheetClick;
    }

    public BottomSheetPaypal() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        itemView =  inflater.inflate(R.layout.fragment_bottom_sheet_dialog_paypal, container, false);

        tennisCommon = new TennisCommon();

        paypalModel  = TennisApp.getPaypalModel();

        back = itemView.findViewById(R.id.back);

        emailLyt = itemView.findViewById(R.id.email_lyt);
        passLyt = itemView.findViewById(R.id.pass_lyt);

        emailEdit = itemView.findViewById(R.id.email);
        passEdit = itemView.findViewById(R.id.password);

        emailEdit.addTextChangedListener(emailWatcher);
        passEdit.addTextChangedListener(passWatcher);

        loginBtn = itemView.findViewById(R.id.login_btn);

        if (paypalModel != null){

            emailEdit.setText(paypalModel.getEmail());
            passEdit.setText(paypalModel.getPassword());


        }

        back.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

        return itemView;

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.back:

                if (onBottomSheetClick != null){

                    onBottomSheetClick.onBackPaypalClicked();
                }


                break;

            case R.id.login_btn:

                String email = emailEdit.getText().toString().trim();
                String pass = passEdit.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    emailLyt.setError("Email is missing");
                    return;
                }

                if (tennisCommon != null && !tennisCommon.isValidEmail(email)){
                    emailLyt.setError("Email not valid");
                    return;

                }

                emailLyt.setError(null);

                if (TextUtils.isEmpty(pass)){
                    passLyt.setError("Password is missing");
                    return;
                }

                if (pass.length() <= 8){
                    passLyt.setError("should be more then 8 characters");
                    return;
                }

                passLyt.setError(null);

                if (onBottomSheetClick != null){

                    onBottomSheetClick.onLoginClicked(loginBtn,email,pass);
                }

                break;


            default:

                break;
        }
    }


    TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String s = charSequence.toString();

            if (tennisCommon != null && !tennisCommon.isValidEmail(s)){

                emailLyt.setError("Email not valid");

            }else{

                emailLyt.setError(null);

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

            if (s.length() <= 8){
                passLyt.setError("should be more then 8 characters");
                return;
            }


            passLyt.setError(null);

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


}
