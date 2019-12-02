package com.example.fazalproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {

    public static ArrayList phoneNumberArr = new ArrayList();
    public static ArrayList details = new ArrayList();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter recyler_adapter;
    Button button ;
    static int index = 0;
    FirebaseFirestore db ;
    String phoneNumber;
    final int SEND_SMS_PERMISSION_CODE = 1;
    String quantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        final TextView phoneNum = findViewById(R.id.UpPhoneNumber);
        final TextView quantityTv = findViewById(R.id.UpQuantity);
        TextView toFromTv = findViewById(R.id.UpToFrom);
        TextView priceTv = findViewById(R.id.UpPice);
        button = findViewById(R.id.submitRequest);

        Intent intent = getIntent();

        final int position = intent.getIntExtra("position",-1);


        if(checkPermission(Manifest.permission.SEND_SMS)){

        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_CODE);

        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermission(Manifest.permission.SEND_SMS)){
                    SmsManager smsManager = SmsManager.getDefault();
                    // phoneNubme rwill be changed
                    smsManager.sendTextMessage(phoneNumber,null,"Hello sir I want to pick your Request of : " + quantity + " " + Traveler.ToFrom.get(position).toString()
                            + " "+ Traveler.price.get(position).toString() + " Kindly contact me to proceed further.",null, null);
                    Toast.makeText(getApplicationContext(), "Your msg has been sent to"+ phoneNumber, Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getApplicationContext(), "Message not Sent", Toast.LENGTH_SHORT).show();
                }


            }
        });










        phoneNumber = RecyclerAdapter.titles.get(position);
        phoneNum.setText("Contact: " +phoneNumber);
        ArrayList<String> details = new ArrayList<>();


        String s1=RecyclerAdapter.details.get(position);;
        String[] words=s1.split(",");//splits the string based on whitespace
//using java foreach loop to print elements of string array
        /*for(String w:words){
            details.add(w);

        }*/
        //Toast.makeText(getApplicationContext(),words.length, Toast.LENGTH_LONG).show();

        quantity = Traveler.quantity.get(position).toString();




        //String price = details.get(3);

        quantityTv.setText(quantity);

        toFromTv.setText(Traveler.ToFrom.get(position).toString());
        priceTv.setText(Traveler.price.get(position).toString());




    }


    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this,permission);
        return (check == PackageManager.PERMISSION_GRANTED);

    }

    public void onSend(View v){


    }
}
