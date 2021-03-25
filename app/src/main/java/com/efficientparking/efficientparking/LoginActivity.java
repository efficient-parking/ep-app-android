package com.efficientparking.efficientparking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText targaEditText, passwordEditText;
    TextInputLayout targa, password;
    Button GoToNewActivity;
    CheckBox rememberMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        targa=findViewById(R.id.logTarga);
        password=findViewById(R.id.logPassword);
        rememberMe=findViewById(R.id.remember_me);
        targaEditText = findViewById(R.id.targaEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        GoToNewActivity = (Button)findViewById(R.id.logRegister);
        SessionManager sessionManager = new SessionManager(LoginActivity.this,SessionManager.SESSION_REMEMBERMESESSION);
        if(sessionManager.checkRememberMe()){
            HashMap<String, String> rememberMeDetails = sessionManager.getRememberMeDetailFromSession();
            targaEditText.setText(rememberMeDetails.get(sessionManager.KEY_SESSIONTARGA));
            passwordEditText.setText(rememberMeDetails.get(sessionManager.KEY_SESSIONPASSWORD));
        }
        GoToNewActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Intent code for open new activity through intent.

                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);

            }
        });
    }
    public void letTheUserLoggedIn(View view){
        if(!validateFields()){
            return;
        }

        //get data

        final String _targa = targa.getEditText().getText().toString().trim();
        String _password = password.getEditText().getText().toString().trim();

        Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("targa").equalTo(_targa);

        final String final_password = bin2hex(getHash(md5(_password)));

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    targa.setError(null);
                    targa.setErrorEnabled(false);

                    String systemPassword = dataSnapshot.child(_targa).child("password").getValue(String.class);
                    if (systemPassword.equals(final_password)) {
                        password.setError(null);
                        password.setErrorEnabled(false);

                        String _Targa = dataSnapshot.child(_targa).child("targa").getValue(String.class);
                        String _password = password.getEditText().getText().toString().trim();
                        String _name = dataSnapshot.child(_targa).child("name").getValue(String.class);

                        //Create a Session

                        SessionManager sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSION_USERSESSION);
                        sessionManager.createLoginSession(_name,_Targa,_password);
                        if(rememberMe.isChecked()){
                            SessionManager sessionManager1 = new SessionManager(LoginActivity.this,SessionManager.SESSION_REMEMBERMESESSION);
                            sessionManager1.createRememberMeSession(_targa,_password);
                        }

                        Intent intent = new Intent(LoginActivity.this, TargaActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Targa does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( LoginActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateFields() {
        String _username = targa.getEditText().getText().toString().trim();
        String _password = password.getEditText().getText().toString().trim();
        if(_username.isEmpty()){
            targa.setError("La targa non può essere vuota");
            targa.requestFocus();
            return false;
        }
        else if(_password.isEmpty()){
            password.setError("La password non può essere vuota");
            password.requestFocus();
            return false;
        }
        else{
        return true;
    }
    }

    public static String md5(String s)
    {
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(Charset.forName("US-ASCII")),0,s.length());
            byte[] magnitude = digest.digest();
            BigInteger bi = new BigInteger(1, magnitude);
            String hash = String.format("%0" + (magnitude.length << 1) + "x", bi);
            return hash;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }
    public byte[] getHash(String password) {
        MessageDigest digest=null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        digest.reset();
        return digest.digest(password.getBytes());
    }

    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
    }

}



