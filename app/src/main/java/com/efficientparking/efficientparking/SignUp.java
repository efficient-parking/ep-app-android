package com.efficientparking.efficientparking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.security.MessageDigest.getInstance;

public class SignUp extends AppCompatActivity {

    TextInputLayout reg_name, reg_username, reg_email, reg_phonenumber, reg_password, reg_targa;
    Button reg_register, reg_login, GoToNewActivity;;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        reg_name = findViewById(R.id.regName);
        reg_username = findViewById(R.id.regUsername);
        reg_email = findViewById(R.id.regEmail);
        reg_phonenumber = findViewById(R.id.regPhonenumber);
        reg_password = findViewById(R.id.regPassword);
        reg_register = findViewById(R.id.regRegister);
        reg_login = findViewById(R.id.regLogin);
        reg_targa = findViewById(R.id.regTarga);
        GoToNewActivity = (Button)findViewById(R.id.regLogin);
        reg_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateFields()){
                    return;
                }
                /*if(validateUsername()){
                    return;
                }*/
                else{
                    reg_username.setError(null);
                    reg_username.setErrorEnabled(false);
                }

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");
                //Get all the values
                String reg_Name = reg_name.getEditText().getText().toString();
                String reg_Username = reg_username.getEditText().getText().toString();
                String reg_Email = reg_email.getEditText().getText().toString();
                String reg_Phonenumber = reg_phonenumber.getEditText().getText().toString();
                String reg_Password = reg_password.getEditText().getText().toString();
                String reg_Targa = reg_targa.getEditText().getText().toString();
                reg_Password = hash(reg_Password);
                reg_Password = bin2hex(getHash(reg_Password));

                UserHelperClass helperClass = new UserHelperClass(reg_Name, reg_Username, reg_Email, reg_Phonenumber, reg_Password, reg_Targa);
                /*while(true) {
                    if(validateTarga()){
                    reference.child(reg_Targa).setValue(helperClass);
                    Intent intent = new Intent(SignUp.this, TargaActivity.class);
                    startActivity(intent);
                    }
                    else{
                        continue;
                    }
                }*/
                reference.child(reg_Targa).setValue(helperClass);
                Intent intent = new Intent(SignUp.this, TargaActivity.class);
                startActivity(intent);
    }
});

        GoToNewActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Intent code for open new activity through intent.

                Intent intent = new Intent(SignUp.this, LoginActivity.class);
                startActivity(intent);

            }
        });
    }
    private boolean validateFields() {
        String _username = reg_username.getEditText().getText().toString().trim();
        String _password = reg_password.getEditText().getText().toString().trim();
        String _name = reg_name.getEditText().getText().toString().trim();
        String _phoneNo = reg_phonenumber.getEditText().getText().toString().trim();
        String _email = reg_email.getEditText().getText().toString().trim();
        String _targa = reg_targa.getEditText().getText().toString().trim();
        if(_name.isEmpty()){
            reg_name.setError("Il nome non può essere vuoto");
            reg_name.requestFocus();
            return false;
        }
        else {
            reg_name.setError(null);
            reg_name.setErrorEnabled(false);
        }
        if(_username.isEmpty()){
            reg_username.setError("L'username non può essere vuoto");
            reg_username.requestFocus();
            return false;
        }
        else {
            reg_username.setError(null);
            reg_username.setErrorEnabled(false);
        }
        if(_email.isEmpty()){
            reg_email.setError("L'email non può essere vuota");
            reg_email.requestFocus();
            return false;
        }
        else {
            reg_email.setError(null);
            reg_email.setErrorEnabled(false);
        }
        if(_phoneNo.isEmpty()){
            reg_phonenumber.setError("Il numero di telefono non può essere vuoto");
            reg_phonenumber.requestFocus();
            return false;
        }
        else {
            reg_phonenumber.setError(null);
            reg_phonenumber.setErrorEnabled(false);
        }
        if(_targa.isEmpty()){
            reg_targa.setError("La targa non può essere vuota");
            reg_targa.requestFocus();
            return false;
        }
        else {
            reg_targa.setError(null);
            reg_targa.setErrorEnabled(false);
        }
        if(_password.isEmpty()){
            reg_password.setError("La password non può essere vuota");
            reg_password.requestFocus();
            return false;
        }
        if(_password.length()<8){
            reg_password.setError("La password dev'essere lunga almeno 8 caratteri");
            reg_password.requestFocus();
            return false;
        }
        else {
            reg_password.setError(null);
            reg_password.setErrorEnabled(false);
        }
        if(!(_email.contains("@") && _email.contains("."))){
            reg_email.setError("L'email non è valida");
            reg_email.requestFocus();
            return false;
        }
        else {
            reg_email.setError(null);
            reg_email.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateTarga(){
        final String _targa = reg_targa.getEditText().getText().toString().trim();
        Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("targa").equalTo(_targa);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    reg_targa.setError("Targa già registrata");
                    Toast.makeText(SignUp.this, "Targa già registrata", Toast.LENGTH_SHORT).show();
                }
                else {
                    reg_targa.setError(null);
                    reg_targa.setErrorEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText( SignUp.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        if(reg_targa.getError()==("Targa già registrata")){
            return false;
        }
        else{
            return true;
        }
    }

    public static String hash(String s)
    {
        MessageDigest digest;
        try
        {
            digest = getInstance("MD5");
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

