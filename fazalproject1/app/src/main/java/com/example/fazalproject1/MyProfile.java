package com.example.fazalproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyProfile extends AppCompatActivity {

    FirebaseFirestore db ;
    EditText name,number,email;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        button = findViewById(R.id.Save);

        name = findViewById(R.id.EnterName);
        number = findViewById(R.id.MobileNumber);
        email = findViewById(R.id.Email);
        number.setText(LoginActivity.userPhoneNumber);

        db.collection("UserIds")
                .whereEqualTo("MobileNumber", LoginActivity.userPhoneNumber)
                //.whereEqualTo("SpinFrom",cityNameFrom)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                            }
                        } else {
                        }
                    }
                });


        db = FirebaseFirestore.getInstance();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> city = new HashMap<>();
                if(name.getText().toString() != null && number.getText().toString() != null && email.getText().toString() != null) {
                    city.put("Name", name.getText().toString());
                    city.put("MobileNumber", LoginActivity.userPhoneNumber);
                    city.put("email", email.getText().toString());
                }

                db.collection("UserIds").document()
                        .set(city)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }
        });






    }
}
