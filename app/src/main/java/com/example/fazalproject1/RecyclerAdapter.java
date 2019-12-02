package com.example.fazalproject1;

import android.app.AlertDialog;
import android.app.Dialog;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    //changes
    private Context mContext;
    private List<Upload> mUploads;

  /*  public RecyclerAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUploads = uploads;
    }*/


    //public static String[] titles = {"Chapter One" , "Chapter Two" ,"Chapter One" , "Chapter Two" ,"Chapter One" , "Chapter Two"};

    public static ArrayList<String> titles = new ArrayList<>();

    public static ArrayList<String> details = new ArrayList<>();

    public static ArrayList<String> priceArr = new ArrayList<>();
    public static ArrayList<String> quantityArr = new ArrayList<>();
    public static ArrayList<String> SenderItemNameArr = new ArrayList<>();
    public static ArrayList<String> imageUrlArr = new ArrayList<>();

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
        public TextView price;
        public TextView quantity;
        public TextView senderItemName;
        public ImageView deleteImg;
        public ImageView updateImg;
        public Context context;
        // changes
        public ImageView imageView;

        FirebaseFirestore db ;

        public ViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();
            db = FirebaseFirestore.getInstance();
            // changes
            imageView = itemView.findViewById(R.id.image_view_upload);

            //  itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
            itemDetail = (TextView)itemView.findViewById(R.id.item_detail);
            price = (TextView)itemView.findViewById(R.id.NewPrice);
            quantity = (TextView)itemView.findViewById(R.id.Newquantity);
            senderItemName = (TextView)itemView.findViewById(R.id.SenderItemName);
            deleteImg = (ImageView)itemView.findViewById(R.id.deleteImg);
            updateImg = (ImageView)itemView.findViewById(R.id.updateImg);
            db = FirebaseFirestore.getInstance();

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
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        //viewHolder.itemTitle.setText(titles[i]);
        viewHolder.itemTitle.setText(titles.get(i));
        viewHolder.price.setText(priceArr.get(i));
        viewHolder.quantity.setText(quantityArr.get(i));
        //viewHolder.itemDetail.setText(details[i]);
        viewHolder.itemDetail.setText(details.get(i));
        viewHolder.senderItemName.setText(SenderItemNameArr.get(i));
        viewHolder.deleteImg.setTag(Traveler.TagsArr.get(i));
        viewHolder.updateImg.setTag(Traveler.TagsArr.get(i));


        //changes

            String uploadCurrent = RecyclerAdapter.imageUrlArr.get(i);
            mContext = Traveler.travelerContext;
            // changes viewHolder.textViewName.setText(uploadCurrent.getName());
            Picasso.with(mContext)
                    .load(uploadCurrent)
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(viewHolder.imageView);


        //Changes anonymous

        viewHolder.updateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int position =  Integer.parseInt(viewHolder.updateImg.getTag().toString());

                if(titles.get(position).equalsIgnoreCase(LoginActivity.userPhoneNumber)) {

                    Toast.makeText(viewHolder.context, position + "", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(viewHolder.context, EditActivity.class);
                    intent.putExtra("position", position);
                    viewHolder.context.startActivity(intent);

                }else{
                    Toast.makeText(viewHolder.context,"You can't edit this record!",Toast.LENGTH_LONG).show();
                }




            }
        });

        viewHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position =  Integer.parseInt(viewHolder.deleteImg.getTag().toString());

                if(titles.get(position).equalsIgnoreCase(LoginActivity.userPhoneNumber)) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    DocumentReference noteRef = db.collection("SenderTable")
                            .document(Traveler.SenderRequestIds.get(position).toString());

                    noteRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(viewHolder.context,"Deleted successfully",Toast.LENGTH_LONG).show();
                                Traveler.recyler_adapter.notifyItemRemoved(position);
                                Traveler.recyler_adapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(viewHolder.context,"Fail to Delete",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(viewHolder.context,"You can't delete this record!",Toast.LENGTH_LONG).show();
                }






            }
        });


    }

    @Override
    public int getItemCount() {
        //return titles.length;
        return titles.size();
    }
}