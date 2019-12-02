package com.example.fazalproject1;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SnapshotMetadata;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class Traveler extends Fragment {


    Spinner spinnerTo,spinnerFrom;
    ListView listView;
    String cityNameTo,cityNameFrom;
    Button searchButton;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();

    StorageReference storageRef2 = storage.getReference();

    FirebaseFirestore db ;
    Adapter adapter;
    String text;
    public static ArrayList phoneNumber = new ArrayList();
    public static ArrayList details = new ArrayList();
    public static ArrayList images = new ArrayList();
    public static ArrayList quantity = new ArrayList();
    public static ArrayList ToFrom = new ArrayList();
    public static ArrayList price = new ArrayList();

    FirebaseStorage storage2 = FirebaseStorage.getInstance();


    //StorageReference storageRef2 = storage2.getReference();

    StorageReference imageRef = storageRef.child("images");







    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    public static RecyclerView.Adapter recyler_adapter;

    static int index=0;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View RootView = inflater.inflate(R.layout.fragment_traveler, container, false);

        db = FirebaseFirestore.getInstance();

        searchButton = RootView.findViewById(R.id.SearchButton);
        searchButton.setText("Search");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RecyclerAdapter.titles.clear();
                RecyclerAdapter.details.clear();
                phoneNumber.clear();
                details.clear();

                Toast.makeText(getContext(),"buttonClicked",Toast.LENGTH_LONG).show();

                db.collection("SenderTable")
                        .whereEqualTo("searchCity", cityNameTo+","+cityNameFrom)
                        //.whereEqualTo("SpinFrom",cityNameFrom)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Toast.makeText(getContext(), document.getId() + " => " + document.getData().toString(), Toast.LENGTH_SHORT).show();
                                        phoneNumber.add(document.getString("contactNo"));
                                        details.add("Price: "+ document.getString("Price") + ", Quantity: " + document.getString("quantity") + ", To: "
                                                + document.getString("spinTo") + ", From: " + document.getString("spinFrom") );
                                        //images.add(document.getString("uploadPic"));





                                        RecyclerAdapter.titles.add(phoneNumber.get(index).toString());
                                        RecyclerAdapter.details.add(details.get(index).toString());


                                        recyler_adapter.notifyDataSetChanged();
                                        index++;

                                        Log.d("TAG1 Hyd" + cityNameTo, document.getId() + " => " + document.getData());

                                    }
                                    index = 0;
                                } else {
                                    Toast.makeText(getContext(), "Error getting documents.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        spinnerTo = (Spinner) RootView.findViewById(R.id.spinner3);
        spinnerFrom = (Spinner) RootView.findViewById(R.id.spinner4);

        recyclerView = (RecyclerView) RootView.findViewById(R.id.recycler_view);
        layoutManager =new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyler_adapter = new RecyclerAdapter();
        recyclerView.setAdapter(recyler_adapter);

       // listView = (ListView) RootView.findViewById(R.id.listview1);
        Toast.makeText(getContext(),"fazal",Toast.LENGTH_SHORT).show();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.cities_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerTo.setAdapter(adapter);
        spinnerFrom.setAdapter(adapter);



        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityNameTo = (String) adapterView.getItemAtPosition(i);

                Toast.makeText(getContext(), cityNameTo, Toast.LENGTH_SHORT).show();





            }



            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getContext(),"NothingSelected",Toast.LENGTH_SHORT).show();
            }
        });



        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityNameFrom = (String) adapterView.getItemAtPosition(i);

                Toast.makeText(getContext(),cityNameFrom,Toast.LENGTH_SHORT).show();

            }



            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getContext(),"NothingSelected",Toast.LENGTH_SHORT).show();
            }
        });


        db.collection("SenderTable")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                Toast.makeText(getContext(),document.getId() + " => " + document.getData().toString(),Toast.LENGTH_SHORT).show();
                                phoneNumber.add(document.getString("contactNo"));
                                details.add("Quantity: " + document.getString("quantity") + "To: " + document.getString("spinTo")+ "From: " + document.getString("spinFrom")
                                        + "Price: " + document.getString("Price") );
                                images.add(document.getString("uploadPic"));



                                quantity.add("Quantity: " + document.getString("quantity"));
                                ToFrom.add("To: " + document.getString("spinTo") + ", From: " + document.getString("spinFrom") );
                                price.add("Price: "+ document.getString("Price"));



//                                RecyclerAdapter.titles[index] = phoneNumber.get(index).toString();
                                RecyclerAdapter.titles.add(phoneNumber.get(index).toString());
//                                RecyclerAdapter.details[index] = details.get(index).toString();
                                RecyclerAdapter.details.add(details.get(index).toString());


                                recyler_adapter.notifyDataSetChanged();
                                index++;





                            }
                            index=0;
                        } else {

                            Toast.makeText(getContext(),"Error getting documents.",Toast.LENGTH_SHORT).show();

                        }
                    }
                });




        
        /*docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(getContext(),document.getData().toString(),Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(getContext(),"Fail",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("TAG1", "get failed with ", task.getException());
                }
            }
        });*/

        /*
        // Create a reference to the cities collection
        CollectionReference citiesRef = db.collection("cities");

// Create a query against the collection.
        Query query = citiesRef.whereEqualTo("state", "CA");

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                SnapshotMetadata metadata = queryDocumentSnapshots.getMetadata();
                metadata.toString();
            }
        });

*/




        return RootView;
    }

    public void dialogBoxFunc(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Please select any city");
// Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button



            }
        }).create();


        AlertDialog dialog = builder.create();
        dialog.show();
    }





}
