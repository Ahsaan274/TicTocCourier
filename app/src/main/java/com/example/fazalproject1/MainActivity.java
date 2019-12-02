package com.example.fazalproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText editText, editText2;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
    }



    public void button1(View view){

        String text = editText.getText().toString();
        String text2 = editText2.getText().toString();
        Map<String, Object> city = new HashMap<>();
        city.put("name", editText.getText().toString());
        city.put("password",editText2.getText().toString());

        db.collection("UserTable").document()
                .set(city)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG1", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG2", "Error writing document", e);
                    }
                });
    }
}
