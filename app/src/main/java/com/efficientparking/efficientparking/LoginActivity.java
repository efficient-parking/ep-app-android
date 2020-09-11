package com.efficientparking.efficientparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout targa, password;
    Button GoToNewActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        targa=findViewById(R.id.logTarga);
        password=findViewById(R.id.logPassword);

        GoToNewActivity = (Button)findViewById(R.id.logRegister);

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
        final String _password = password.getEditText().getText().toString().trim();

        Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("targa").equalTo(_targa);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    targa.setError(null);
                    targa.setErrorEnabled(false);

                    String systemPassword = dataSnapshot.child(_targa).child("password").getValue(String.class);
                    if (systemPassword.equals(_password)) {
                        password.setError(null);
                        password.setErrorEnabled(false);
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
        return true;
    }
}

