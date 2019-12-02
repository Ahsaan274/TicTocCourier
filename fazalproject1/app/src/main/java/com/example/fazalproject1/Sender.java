package com.example.fazalproject1;


import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

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


    //private static final int RESULT_OK = 1;
    EditText senderName,contactNo,spLocTo,spLocFrom,productName,quantity, price;
    Button submitRequest,gMap;
    ImageView uploadPic;
    Spinner spinnerTo,spinnerFrom;
    String imgPath;

    FirebaseFirestore db ;
    String cityNameTo,cityNameFrom;
    StorageReference mStorageRef;
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
        uploadPic = (ImageView) rootView.findViewById(R.id.uploadPicImage);

        imagedownloadButton = rootView.findViewById(R.id.downloadImagebtn);

        imagedownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


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




        //button2.setText(LoginActivity.emailAddress);




        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ImageToUpload
              /*  if(false){
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);

                }
*/
               /* if(true){
                    Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
                    StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
                    UploadTask uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Toast.makeText(getContext(),"Failure",Toast.LENGTH_SHORT).show();
                            exception.printStackTrace();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...

                            Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
                        }
                    });
                } */

            //    Filechooser();



            }
        });



        RecyclerAdapter.titles.clear();





        senderName = rootView.findViewById(R.id.editText5);
        contactNo = rootView.findViewById(R.id.editText11);
        spLocTo = rootView.findViewById(R.id.editText9);
        spLocFrom = rootView.findViewById(R.id.editText10);
        productName = rootView.findViewById(R.id.editText12);
        price = rootView.findViewById(R.id.editText14);
        quantity = rootView.findViewById(R.id.editText13);

        submitRequest = rootView.findViewById(R.id.button3);
        spinnerTo = rootView.findViewById(R.id.spinner);
        spinnerFrom = rootView.findViewById(R.id.spinner2);

        contactNo.setText(LoginActivity.userPhoneNumber);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.cities_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerTo.setAdapter(adapter);
        spinnerFrom.setAdapter(adapter);

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



               /* if(uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_LONG).show();

                }else {
                    Fileuploader();
                }
*/


                Map<String, Object> city = new HashMap<>();
                city.put("SenderName", senderName.getText().toString());
                city.put("contactNo", LoginActivity.userPhoneNumber);
                city.put("spinTo",cityNameTo);
                city.put("spinFrom", cityNameFrom );
                city.put("spLocTo", spLocTo.getText().toString());
                city.put("spLocFrom" , spLocFrom.getText().toString());
                city.put("productName", productName.getText().toString());
                city.put("quantity", quantity.getText().toString());
                city.put("uploadPic", imgPath);
                city.put("submitRequest", submitRequest.getText().toString());
                city.put("searchCity",cityNameTo+","+cityNameFrom);
                city.put("Price", price.getText().toString());

              /*  storageRef.child(imguri.toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png' in uri
                        System.out.println(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });


                tempURI = imguri;
*/







                db.collection("SenderTable").document()
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




        return rootView;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        /*
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            uploadPic.setImageURI(selectedImage);

        }*/

        if(requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData() != null){

            imguri=data.getData();
            uploadPic.setImageURI(imguri);



        }




    }

    private String getExtension(Uri uri){

        ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));


    }



    private void Filechooser(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);


    }




    public Sender() {
        // Required empty public constructor

    }



    private void writeNewUser(String userId, String name, String email) {
     //   User user = new User(name, email);

        // mDatabase.child("users").child(userId).setValue(user);
    }

    public void updateName(String userId,String name) {
        //    mDatabase.child("users").child(userId).child("username").setValue(name);
    }



    public void submitButton(View view){

/*        Request request = new Request(senderName.getText().toString(),"example@gmail.com",contactNo.getText().toString(),
                spLocTo.getText().toString(),spLocFrom.getText().toString(),
                productName.getText().toString(),quantity.getText().toString());*/


    }

    private void Fileuploader(){

        imgPath = System.currentTimeMillis() + "." + getExtension(imguri);

        StorageReference Ref = mStorageRef.child(imgPath);

        uploadTask = Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
//            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        Toast.makeText(getContext(), "Image Uploaded successfully", Toast.LENGTH_LONG).show();


                   //     downloadUrl = StorageReference.getDownloadUrl();




                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });




      /*  mStorageRef.child(imguri.toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png' in uri
                //System.out.println(uri.toString());
                Toast.makeText(getContext(),"IMAGE HAS BEEN DOWNLOADED" +uri.toString(), Toast.LENGTH_SHORT).show();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"FAIL TO DOWNLOAD" , Toast.LENGTH_SHORT).show();

            }
        });*/






    }




    }
