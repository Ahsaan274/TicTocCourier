package com.example.fazalproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import java.util.HashMap;
import java.util.Map;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditActivity extends AppCompatActivity {

    public Uri uri2;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageTask mUploadTask;


    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;



    EditText ItemName,quantity,price;
    Spinner spinner1,spinner2;
    Button submit, cancel, uploadPicBtnEdit;
    FirebaseFirestore db ;
    String cityNameTo,cityNameFrom;
    ImageView uploadImageEdit;
    ProgressBar mProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        db = FirebaseFirestore.getInstance();

        ItemName = findViewById(R.id.ItemNameEdit);
        quantity = findViewById(R.id.QuantityEdit);
        price = findViewById(R.id.PriceEdit);

        mProgressBar = findViewById(R.id.progress_barEdit);

        uploadImageEdit = findViewById(R.id.UploadpicEdit);
        uploadPicBtnEdit = findViewById(R.id.UploadPicBtn);

        submit = findViewById(R.id.registerButtonEdit);
        cancel = findViewById(R.id.cancelButtonEdit);

        spinner1 = (Spinner) findViewById(R.id.spinnerEdit);
        spinner2 = (Spinner) findViewById(R.id.spinner2Edit);


        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        ArrayAdapter<CharSequence> adapterTo = ArrayAdapter.createFromResource(this,
                R.array.cities_array, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapterFrom = ArrayAdapter.createFromResource(this,
                R.array.strings_array, android.R.layout.simple_spinner_item);

        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapterTo);
        spinner2.setAdapter(adapterFrom);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityNameTo = (String) adapterView.getItemAtPosition(i);

                Toast.makeText(getApplicationContext(),cityNameTo,Toast.LENGTH_SHORT).show();

            }



            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(),"NothingSelected",Toast.LENGTH_SHORT).show();
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityNameFrom = (String) adapterView.getItemAtPosition(i);

                Toast.makeText(getApplicationContext(),cityNameFrom,Toast.LENGTH_SHORT).show();

            }



            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(),"NothingSelected",Toast.LENGTH_SHORT).show();
            }
        });







        Intent intent = getIntent();

        final int position = intent.getIntExtra("position",-1);

        // may be there will be an error because I think I should user Traveler instead RecyclerAdapter
        ItemName.setText(RecyclerAdapter.SenderItemNameArr.get(position));
        quantity.setText(RecyclerAdapter.quantityArr.get(position));
        price.setText(RecyclerAdapter.priceArr.get(position));
        Picasso.with(getApplicationContext()).load(RecyclerAdapter.imageUrlArr.get(position)).into(uploadImageEdit);


        uploadImageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        uploadPicBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                      if (mUploadTask != null && mUploadTask.isInProgress()) {
                            Toast.makeText(getApplicationContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
                        } else {
                            uploadFile();
                        }
            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Map<String, Object> senderUpdate = new HashMap<>();
                //anonymous user login
                senderUpdate.put("SenderName", ItemName.getText().toString());//
                //anonymous user login
                senderUpdate.put("contactNo", "+923062337855");
                senderUpdate.put("spinTo", cityNameTo);
                senderUpdate.put("spinFrom", cityNameFrom);
                senderUpdate.put("spLocTo", "SpToLocEmpty");
                senderUpdate.put("spLocFrom", "SpFromLocEmpty");
                senderUpdate.put("productName", "Product Name Empty");
                senderUpdate.put("quantity", quantity.getText().toString());//
                senderUpdate.put("uploadPic", "");
                senderUpdate.put("submitRequest", "");
                senderUpdate.put("searchCity", cityNameTo + "," + cityNameFrom);
                if(uri2 != null) {
                    senderUpdate.put("imageUrl", uri2.toString());
                }else {
                    senderUpdate.put("imageUrl", RecyclerAdapter.imageUrlArr.get(position));
                }
                senderUpdate.put("Price", price.getText().toString());//



                db.collection("SenderTable").document(Traveler.SenderRequestIds.get(position).toString())
                        .update(senderUpdate)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                           Toast.makeText(getApplicationContext(),"Updated!",Toast.LENGTH_LONG).show();

                           /*RecyclerAdapter.SenderItemNameArr.set(position, ItemName.getText().toString());
                           RecyclerAdapter.quantityArr.set(position, quantity.getText().toString());
                           RecyclerAdapter.priceArr.set(position,price.getText().toString());
                           RecyclerAdapter.details.set(position,"To: " + cityNameTo +" , From: " + cityNameFrom);
                           RecyclerAdapter.imageUrlArr.set(position,uri2.toString());
*/

                           Traveler.recyler_adapter.notifyDataSetChanged();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failured!",Toast.LENGTH_LONG).show();
                    }
                });


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });








    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(getApplicationContext()).load(mImageUri).into(uploadImageEdit);
        }
    }

    private String getFileExtension(Uri uri){

        ContentResolver cr = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }



    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_LONG).show();
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    uri2 = uri;

                                    Upload upload = new Upload(ItemName.getText().toString(),
                                            uri2.toString());
                                    Toast.makeText(getApplicationContext(),uri2.toString(),Toast.LENGTH_LONG).show();
                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(uploadId).setValue(upload);

                                }
                            });

                            taskSnapshot.getStorage().getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"Failure to upload!",Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}
