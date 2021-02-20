package com.efficientparking.efficientparking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class TargaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_targa);
        //public static final String PAYPAL_CLIENT_ID = "AfLrjn-VoDImykiifjHImn1mtrZp4vwDLFkL73h0zGv3ZRaCb4gabNOnQ0usEahMEK6q1OPlG1n1yCaj";

        TextView targaa = findViewById(R.id.targa);
        TextView benvenuto = findViewById(R.id.textBenvenuto);
        Button button_uscita = findViewById(R.id.buttonUscita);
        SessionManager sessionManager = new SessionManager(this, null);
        HashMap<String, String> usersDetails = sessionManager.getUsersDetailFromSession();

        String fullName = usersDetails.get(SessionManager.KEY_FULLNAME);
        final String targa = usersDetails.get(SessionManager.KEY_TARGA);

        targaa.setText(targa);
        benvenuto.setText("Benvenuto su Efficient Parking, " + fullName);
        button_uscita.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
                //String dataUscita = sdf.format(new Date());
                FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                DatabaseReference reference = rootNode.getReference("users");
                reference.child(targa).child("uscita").setValue("0");
            }
        });

        final String address = "Park+Fogazzaro";
        final ImageView v = (ImageView) findViewById(R.id.buttonMappa);
        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String url = "https://www.google.com/maps/search/?api=1&query=" + address;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }

}
