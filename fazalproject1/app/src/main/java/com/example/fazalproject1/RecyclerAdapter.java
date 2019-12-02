package com.example.fazalproject1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    //public static String[] titles = {"Chapter One" , "Chapter Two" ,"Chapter One" , "Chapter Two" ,"Chapter One" , "Chapter Two"};

    public static ArrayList<String> titles = new ArrayList<>();

    public static ArrayList<String> details = new ArrayList<>();

    public static ArrayList phoneNumber = new ArrayList();
    public static ArrayList detailsLocal = new ArrayList();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter recyler_adapter;

    static int index=0;




    // public static ArrayList<String> titles = new ArrayList<String>();


//    public static String[] details = {"Item one details","Item one details","Item one details","Item one details","Item one details","Item one details" };

   // private int[] images = { R.drawable.logo,R.drawable.logo,R.drawable.logo,R.drawable.logo,R.drawable.logo,R.drawable.logo};

    public static ArrayList<Uri> images = new ArrayList<Uri>(1000);



    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetail;
        FirebaseFirestore db ;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemDetail = (TextView)itemView.findViewById(R.id.item_detail);
            db = FirebaseFirestore.getInstance();
            //recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);
//            layoutManager =new LinearLayoutManager(itemView.getContext());
 //           recyclerView.setLayoutManager(layoutManager);

            //recyler_adapter = new RecyclerAdapter();
            //recyclerView.setAdapter(recyler_adapter);




            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    final int position = getAdapterPosition();

                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle("Do you want to submit this request?");
// Add the buttons
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            Toast.makeText(itemView.getContext(),"Your request has been submitted wait for the response of the SENDER", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(itemView.getContext(),UserProfile.class);
                        intent.putExtra("position",position);
                        itemView.getContext().startActivity(intent);
/*

                            db.collection("SenderTable")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Toast.makeText(itemView.getContext(), document.getId() + " => " + document.getData().toString(), Toast.LENGTH_SHORT).show();
                                                    phoneNumber.add(document.getString("contactNo"));
                                                    details.add("Quantity: " + document.getString("quantity") + "To: " + document.getString("spinTo") + "From: " + document.getString("spinFrom"));
                                                    //images.add(document.getString("uploadPic"));

                                                    if(index == position) {
                                                        Toast.makeText(itemView.getContext(), ""+index, Toast.LENGTH_SHORT).show();
                                                        RecyclerAdapter.titles.add(phoneNumber.get(index).toString());
                                                        RecyclerAdapter.details.add(detailsLocal.get(index).toString());
                                                    }else{

                                                    }

                                                    Traveler.recyler_adapter.notifyDataSetChanged();
                                                    index++;

                                            //        Log.d("TAG1 Hyd" + cityNameTo, document.getId() + " => " + document.getData());

                                                }
                                                index = 0;
                                            } else {
                                                Toast.makeText(itemView.getContext(), "Error getting documents.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
*/




                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            Toast.makeText(itemView.getContext(),"You can choose any other request!", Toast.LENGTH_LONG).show();
                        }
                    }).create();

                    AlertDialog dialog = builder.create();
                    dialog.show();



                    /*
                    Intent intent = new Intent(itemView.getContext(), UserProfile.class);

                    itemView.getContext().startActivity(intent);*/



                    Snackbar.make(v, "Click detected on item " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();



                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_card_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
       //viewHolder.itemTitle.setText(titles[i]);
        viewHolder.itemTitle.setText(titles.get(i));
        //viewHolder.itemDetail.setText(details[i]);
        viewHolder.itemDetail.setText(details.get(i));
        viewHolder.itemImage.setImageResource(R.drawable.image);
        //images.get(i).getPath();
       // viewHolder.itemImage.setImageURI(Sender.imguri);

    //        viewHolder.itemImage.setImageURI(null);

        //viewHolder.itemImage.setImageBitmap();
        /*
        try{
            viewHolder.itemImage.setImageURI(Uri.parse(images.get(i).getPath()));
        //    viewHolder.itemImage.set

        *//*    String drawableRes="http://kartpay.biz/api/v1/file/banner/IHMOcSHU7yoTVOkwYi1bOOz8shrXXrJhayCPFO17.jpeg"
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
          *//*  try {
                URL url = new URL(images.get(i));
                viewHolder.itemImage.setImageBitmap(BitmapFactory.decodeStream((InputStream)url.getContent()));
            } catch (IOException e) {
                //Log.e(TAG, e.getMessage());
            }

        }catch (Exception e){
            Toast.makeText(,e.toString(),Toast.LENGTH_LONG).show();
        }
*/
        //viewHolder.itemImage.setImageResource(images.get(i).getPath());



        /*String stringUri = imageUri.getPath(); //works
        String stringUri = imageUri.toString(); //does not work

        i.setImageURI(Uri.parse(stringUri));*/
    }

    @Override
    public int getItemCount() {
        //return titles.length;
        return titles.size();
    }
}