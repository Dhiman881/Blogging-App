package com.example.hidnam.firebase_1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

import static android.R.attr.bitmap;

public class Account_Settings extends AppCompatActivity {

    private Button mChangeName;
    private FirebaseUser user=null;
    private DatabaseReference mDatabase =null;
    private TextView mTextView;
    private String user_name;
    private Button mChangeImage;
    private static final int Gallery_pick_intent =1;
    private StorageReference mStorageReference;
    private ImageView mImageView;
    private StorageReference mThumbRefernce;
    private  byte[] thumb_byte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__settings);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        mStorageReference = FirebaseStorage.getInstance().getReference().child(user.getEmail()).child("Profile_Images");
        mThumbRefernce =   mStorageReference.child("Thumb_Image");
        loadData();
        mChangeName = (Button) findViewById(R.id.btn_change_name);
        mTextView =(TextView)findViewById(R.id.settings_name_textView);

        mChangeImage = (Button)findViewById(R.id.btn_change_Image);
        mImageView = (ImageView)findViewById(R.id.settings_image);


        mChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();
            }
        });
        mChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"),Gallery_pick_intent);

                /*CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(Account_Settings.this);
                        */

            }
        });

    }

    private void loadData() {
        mDatabase.keepSynced(true);
        mDatabase.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.getValue(String.class);
                if(!name.equals("default")){
                    mTextView.setText(name);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.child("thumbnail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String image = dataSnapshot.getValue(String.class);

                if(!image.equals("default"))
                    //Picasso.with(Account_Settings.this).load(image).placeholder(R.drawable.if_anonymous_334588).into(mImageView);
                    Picasso.with(Account_Settings.this).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.if_anonymous_334588).into(mImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(Account_Settings.this).load(image).placeholder(R.drawable.if_anonymous_334588).into(mImageView);
                        }
                    });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Gallery_pick_intent && resultCode ==RESULT_OK){
            Uri imageUri  = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                File thumb_image_file = new File(resultUri.getPath());
                //mImageView.setImageURI(resultUri);

                try {
                    Bitmap thumb_image = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(75)
                            .compressToBitmap(thumb_image_file)
                            ;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    thumb_byte = baos.toByteArray();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                StorageReference file_path = mStorageReference.child(resultUri.getLastPathSegment());
                final StorageReference thumb_path = mThumbRefernce.child(resultUri.getLastPathSegment());


                file_path.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Log.v("Updated","Image Successfully Updated");

                            @SuppressWarnings("VisibleForTests") final String download_uri = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thumb_path.putBytes(thumb_byte);

                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                    @SuppressWarnings("VisibleForTests")final String download_Uri_thumb = taskSnapshot.getDownloadUrl().toString();

                                    mDatabase.child("image").setValue(download_uri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            mDatabase.child("thumbnail").setValue(download_Uri_thumb).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Snackbar.make(findViewById(R.id.btn_change_Image),"Image Updated Successfully",Snackbar.LENGTH_INDEFINITE).show();

                                                }
                                            });

                                        }
                                    });



                                }
                            });






                        }
                        else{
                            Snackbar.make(findViewById(R.id.btn_change_Image),"Error In Uploading",Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void showAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Change name");
        alertDialog.setMessage("Enter new Name");

        final EditText input = new EditText(Account_Settings.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.mipmap.ic_perm_identity_white_24dp);

        alertDialog.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(!TextUtils.isEmpty(input.getText()))
                            changeName(input.getText().toString().trim());
                        else{
                            Toast.makeText(Account_Settings.this,"Please Enter a valid Name", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private void changeName(final String trim) {

        if(user != null && mDatabase !=null){
            mDatabase.child("name").setValue(trim).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(findViewById(R.id.btn_change_name),"Name Sucessfully Updated",Snackbar.LENGTH_SHORT).show();
                   // mTextView.setText(trim);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(findViewById(R.id.btn_change_name),"Unable to Update Name,Check Your Connection",Snackbar.LENGTH_SHORT).show();
                }
            });
        }

    }


}
