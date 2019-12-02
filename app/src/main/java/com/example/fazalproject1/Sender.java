package com.example.fazalproject1;


import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Sender extends Fragment {

    //changes

    public Uri uri2;

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    //changes ends




    EditText ItemName,quantity,spLocTo,spLocFrom,productName, price;
    Button submitRequest,gMap;
    ImageView uploadPic;
    Spinner spinnerTo,spinnerFrom;
    String imgPath;

    FirebaseFirestore db ;
    String cityNameTo,cityNameFrom;
   // changes StorageReference mStorageRef;
    Button imagedownloadButton;

    private StorageTask uploadTask;
    public Uri downloadUrl;

    public static Uri tempURI;


    public static Uri imguri;

    private static final int RESULT_LOAD_IMAGE = 1;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();

    StorageReference storageRef2 = storage.getReference();

    StorageReference imagesRef = storageRef.child("Images");





    StorageReference spaceRef = storageRef.child("images/space.jpg");

    // Points to the root reference

    // Create a storage reference from our app


    // Create a reference to "mountains.jpg"
    StorageReference mountainsRef = storageRef.child("senderimg.jpg");

    // Create a reference to 'images/mountains.jpg'
    StorageReference mountainImagesRef = storageRef.child("images/senderimg.jpg");



    Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
    StorageReference riversRef = storageRef.child("images/rivers.jpg");



    private void Fireuploader(){



        StorageReference Ref = mStorageRef.child(imgPath);

        Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
//            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        Toast.makeText(getContext(), "Image Uploaded successfully", Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });


    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sender, container, false);

        mStorageRef = FirebaseStorage.getInstance().getReference("Images");

        db = FirebaseFirestore.getInstance();


        //changes


        mButtonChooseImage = rootView.findViewById(R.id.Uploadpic);
        mButtonUpload = rootView.findViewById(R.id.UploadPicBtn);
        mEditTextFileName = rootView.findViewById(R.id.SenderItemName);
        mImageView = rootView.findViewById(R.id.Uploadpic);
        mProgressBar = rootView.findViewById(R.id.progress_bar);


        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });






        // While the file names are the same, the references point to different files
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // falseStorageActivity.java


        // Points to "images"
        imagesRef = storageRef.child("images");

        // Points to "images/space.jpg"
// Note that you can use variables to create child values
        String fileName = "space.jpg";
        spaceRef = imagesRef.child(fileName);

        // File path is "images/space.jpg"
        String path = spaceRef.getPath();

        // File name is "space.jpg"
        String name = spaceRef.getName();

// Points to "images"
        imagesRef = spaceRef.getParent();








        RecyclerAdapter.titles.clear();





        ItemName = rootView.findViewById(R.id.ItemName);
        // spLocTo = rootView.findViewById(R.id.ToLoc);
        // spLocFrom = rootView.findViewById(R.id.FromLoc);
        //productName = rootView.findViewById(R.id.editText12);
        price = rootView.findViewById(R.id.Price);
        quantity = rootView.findViewById(R.id.Quantity);

        submitRequest = rootView.findViewById(R.id.registerButton);
        spinnerTo = rootView.findViewById(R.id.spinner);
        spinnerFrom = rootView.findViewById(R.id.spinner2);

        //quantity.setText(LoginActivity.userPhoneNumber);

        ArrayAdapter<CharSequence> adapterTo = ArrayAdapter.createFromResource(getContext(),
                R.array.cities_array, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapterFrom = ArrayAdapter.createFromResource(getContext(),
                R.array.strings_array, android.R.layout.simple_spinner_item);

        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerTo.setAdapter(adapterTo);
        spinnerFrom.setAdapter(adapterFrom);



        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityNameTo = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(getContext(),cityNameTo,Toast.LENGTH_SHORT).show();
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



        submitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( TextUtils.isEmpty(ItemName.getText())){
                    ItemName.setError( "ItemName is required!" );
                }else if(TextUtils.isEmpty(quantity.getText())) {
                    quantity.setError( "Quantity is required!" );
                }else if(TextUtils.isEmpty(cityNameTo)) {
                }else if(TextUtils.isEmpty(cityNameFrom)) {
                }else if(TextUtils.isEmpty(price.getText())) {
                    price.setError( "Price is required!" );
                }else {
                    Map<String, Object> sender = new HashMap<>();
                    //anonymous user login
                    sender.put("SenderName", ItemName.getText().toString());//
                    //anonymous user login
                    sender.put("contactNo", LoginActivity.userPhoneNumber);
                    sender.put("spinTo", cityNameTo);
                    sender.put("spinFrom", cityNameFrom);
                    sender.put("spLocTo", "SpToLocEmpty");
                    sender.put("spLocFrom", "SpFromLocEmpty");
                    sender.put("productName", "Product Name Empty");
                    sender.put("quantity", quantity.getText().toString());//
                    sender.put("uploadPic", imgPath);
                    sender.put("submitRequest", submitRequest.getText().toString());
                    sender.put("searchCity", cityNameTo + "," + cityNameFrom);
                    if(uri2 != null) {
                        sender.put("imageUrl", uri2.toString());
                    }else {
                        sender.put("imageUrl", "");
                    }
                    sender.put("Price", price.getText().toString());//



                    db.collection("SenderTable").document()
                            .set(sender)
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

                    Toast.makeText(getContext(), "Request Submitted!!", Toast.LENGTH_SHORT).show();


                }





            }
        });




        return rootView;
    }


    //changes

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

            Picasso.with(getContext()).load(mImageUri).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri){

        ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
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

                            Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_LONG).show();
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    uri2 = uri;

                                    Upload upload = new Upload(ItemName.getText().toString(),
                                            uri2.toString());
                                   // Toast.makeText(getContext(),uri2.toString(),Toast.LENGTH_LONG).show();
                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(uploadId).setValue(upload);

                                }
                            });

                            taskSnapshot.getStorage().getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(),"Failure to upload!",Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }








    public Sender() {
        // Required empty public constructor

    }





    }







