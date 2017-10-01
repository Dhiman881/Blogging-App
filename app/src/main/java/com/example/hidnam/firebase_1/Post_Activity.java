package com.example.hidnam.firebase_1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;

public class Post_Activity extends AppCompatActivity {
    private EditText mPostTitle;
    private EditText mPostDescription;
    private Button mPostSubmit;
    private Toolbar mToolbar;
    private ImageButton mImageButton;
    private Uri imageUri = null;
    private StorageReference mStorageRef;
    private static final int GALLERY_REQUEST =1;
    private FirebaseUser mFireBaseUser=null;
    private ProgressBar mProgressbar;
    private DatabaseReference mDataBaseReference=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_);

        mToolbar =(Toolbar)findViewById(R.id.post_tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mFireBaseUser =FirebaseAuth.getInstance().getCurrentUser();
        mDataBaseReference = FirebaseDatabase.getInstance().getReference();


        mImageButton = (ImageButton)findViewById(R.id.imageButton2);
        mPostTitle = (EditText)findViewById(R.id.post_title) ;
        mPostDescription =(EditText)findViewById(R.id.post_desc);
        mPostSubmit =(Button)findViewById(R.id.btn_submit);


        mProgressbar =(ProgressBar)findViewById(R.id.post_Progress_bar);
        mProgressbar.setVisibility(View.INVISIBLE);

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"),GALLERY_REQUEST);
            }
        });
        mPostSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFireBaseUser !=null) {

                    startPosting();
                }
            }
        });
    }


   
    private   void startPosting() {

       final String title = mPostTitle.getText().toString().trim();
       final  String description = mPostDescription.getText().toString().trim();

        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && imageUri !=null){

            mProgressbar.setVisibility(View.VISIBLE);
            StorageReference filePath = mStorageRef.child(mFireBaseUser.getEmail()).child("Blog Images").child(imageUri.getLastPathSegment());


            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests")Uri  downloadUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference newRef = mDataBaseReference.child("Blog").child(mFireBaseUser.getUid()).push();
                    HashMap<String,String>  map = new HashMap<String,String>();
                    map.put("title",title);
                    map.put("description",description);
                    map.put("image",downloadUrl.toString());

                    newRef.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Post_Activity.this,"Post Successful...",Toast.LENGTH_LONG).show();
                            mProgressbar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(Post_Activity.this,MainActivity.class));
                            finish();
                        }
                    });




                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                    Toast.makeText(Post_Activity.this,"Error In Connection...",Toast.LENGTH_LONG).show();
                    mProgressbar.setVisibility(View.INVISIBLE);
                }
            });

        }
        else{

            Toast.makeText(this,"Fill this Shit!!!",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            Log.v("Trying to Insert","Trying to insert the image into imageButton......");
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
           // int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            imageUri = data.getData();
            mImageButton.setImageURI(imageUri);
           // File imageFile = new File(imageUri.getPath());
           // Picasso.with(this).load(imageFile).resize(200,200).into(mImageButton);
            Log.v("Image Inserted","image inserted into imageButton......");
        }
    }
}
