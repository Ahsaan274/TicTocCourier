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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

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
    String quantity, mImageDownloadedUri;
    ImageView profilePic,itemImage;
    TextView itemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        //Database
        db = FirebaseFirestore.getInstance();

        profilePic = findViewById(R.id.userPic);



        itemImage = findViewById(R.id.itemImage);
        itemText = findViewById(R.id.itemName);

        final TextView phoneNum = findViewById(R.id.UpPhoneNumber);
        final TextView quantityTv = findViewById(R.id.UpQuantity);
        TextView toFromTv = findViewById(R.id.UpToFrom);
        TextView priceTv = findViewById(R.id.UpPrice);
        final TextView userTextView = findViewById(R.id.Tv_name);

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
                    smsManager.sendTextMessage(phoneNumber,null,"Hello sir I want to pick your Request of ItemName: "+RecyclerAdapter.SenderItemNameArr.get(position) +", Quantity: " + quantity + " " + Traveler.details.get(position).toString()
                            + " Price: "+ Traveler.price.get(position).toString() + " Kindly contact me to proceed further.",null, null);
                    Toast.makeText(getApplicationContext(), "Your msg has been sent to"+ phoneNumber, Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getApplicationContext(), "Message not Sent", Toast.LENGTH_SHORT).show();
                }


            }
        });










        phoneNumber = RecyclerAdapter.titles.get(position);
        phoneNum.setText("Contact:  " + phoneNumber);
        ArrayList<String> details = new ArrayList<>();

        Picasso.with(getApplicationContext()).load(RecyclerAdapter.imageUrlArr.get(position)).into(itemImage);
        itemText.setText(RecyclerAdapter.SenderItemNameArr.get(position));



        String s1=RecyclerAdapter.details.get(position);;
        String[] words=s1.split(",");//splits the string based on whitespace
//using java foreach loop to print elements of string array
        /*for(String w:words){
            details.add(w);

        }*/
        //Toast.makeText(getApplicationContext(),words.length, Toast.LENGTH_LONG).show();

        quantity = Traveler.quantity.get(position).toString();




        //String price = details.get(3);

        quantityTv.setText("Quantity: "+quantity);

        toFromTv.setText(Traveler.details.get(position).toString());
        priceTv.setText("Price: "+Traveler.price.get(position).toString());
        userTextView.setText(Traveler.ItemName.get(position).toString());

        final DocumentReference documentReference = db.collection("UsersTable").document(phoneNumber);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot != null && documentSnapshot.exists()) {
                        mImageDownloadedUri = documentSnapshot.get("ProfilePic").toString();
                        userTextView.setText(documentSnapshot.get("Name").toString());
                        if(mImageDownloadedUri != "") {
                            Picasso.with(getApplicationContext()).load(mImageDownloadedUri).into(profilePic);
                            Toast.makeText(getApplicationContext(),"ImageDownloaded!",Toast.LENGTH_LONG).show();
                        }


                    }else{
                        Toast.makeText(getApplicationContext(),"No such document Exists!",Toast.LENGTH_LONG).show();
                    }

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        Log.d("Androidview", e.getMessage());
                    }
                });





    }


    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this,permission);
        return (check == PackageManager.PERMISSION_GRANTED);

    }

    public void onSend(View v){


    }
}
