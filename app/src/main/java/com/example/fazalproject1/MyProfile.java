package com.example.fazalproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyProfile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
//    public String mImageUrl = "";



    EditText mobileNumber,email,residency,yourName;
    Button done;
    ImageView profilePic;
    FirebaseFirestore db ;
    public Uri mUri;
    public Uri mImageUri;
    public String mImageDownloadedUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);



        //Database
        db = FirebaseFirestore.getInstance();

        final DocumentReference documentReference = db.collection("UsersTable").document(LoginActivity.userPhoneNumber);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot != null && documentSnapshot.exists()) {
                        mImageDownloadedUri = documentSnapshot.get("ProfilePic").toString();
                        if(mImageDownloadedUri != "") {
                            Picasso.with(getApplicationContext()).load(mImageDownloadedUri).into(profilePic);
                            mobileNumber.setText(documentSnapshot.get("PhoneNumber").toString());
                            email.setText(documentSnapshot.get("Email").toString());
                            residency.setText(documentSnapshot.get("Residency").toString());
                            yourName.setText(documentSnapshot.get("Name").toString());
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



        //EditText
        mobileNumber = findViewById(R.id.PhoneNumber);
        email = findViewById(R.id.Email);
        residency = findViewById(R.id.residency);
        yourName = findViewById(R.id.YourName);


        mStorageRef = FirebaseStorage.getInstance().getReference("profilePics");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("profilePics");

        //Button
        //done = findViewById(R.id.done);

        //ImageView
        profilePic = findViewById(R.id.UploadProfilePic);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });

        /*done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(mobileNumber.getText())){ mobileNumber.setError("Mobile Number is required!" );}
                else if(TextUtils.isEmpty(email.getText())) {email.setError("Email is required!");}
                else if(TextUtils.isEmpty(residency.getText())) {residency.setError("Residency is required!");}
                else if(TextUtils.isEmpty(yourName.getText())){yourName.setError("YouName is required!");}
                else {

                    if (mUploadTask != null && mUploadTask.isInProgress()) {
                        Toast.makeText(getApplicationContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
                    } else {
                        UpdateOnDB();

                    }



                }


            }
        });*/




    }

    public void UpdateOnDB(){

        Map<String, Object> user = new HashMap<>();
        user.put("PhoneNumber", mobileNumber.getText().toString());//
        user.put("Email", email.getText().toString());
        user.put("Residency", residency.getText().toString());
        user.put("Name", yourName.getText().toString());
        if(mUri != null) {
            user.put("ProfilePic", mUri.toString());
        }else {
            user.put("ProfilePic","");
        }


        // Changes when user will login his number will be registered as unique id in UserTable

        db.collection("UsersTable").document(LoginActivity.userPhoneNumber)
                .update(user)
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

        Toast.makeText(getApplicationContext(), "Profile Updated!!", Toast.LENGTH_SHORT).show();
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

            Picasso.with(getApplicationContext()).load(mImageUri).into(profilePic);

            uploadFile();
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
                                  //  mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_LONG).show();
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    mUri = uri;

                                    Upload upload = new Upload(mobileNumber.getText().toString(),
                                            mUri.toString());
                                    Toast.makeText(getApplicationContext(),mUri.toString(),Toast.LENGTH_LONG).show();
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
                           // mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }


}
