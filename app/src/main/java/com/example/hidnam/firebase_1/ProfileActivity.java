package com.example.hidnam.firebase_1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView mProfileimage;
    private TextView mProfileName;
    private RecyclerView mBlogList;
    private Button mFollowButton;
    private DatabaseReference mDatabaseReference;
    private boolean isFollowed;
    private DatabaseReference mFollowedDatabase;
    private ImageView mProfileFollowImage;
    private FirebaseUser mFirebaseUser;
    private RecyclerView mBlog_list;
    private DatabaseReference mBlogDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        mProfileimage = (CircleImageView)findViewById(R.id.profile_display_image);
        mProfileName =(TextView)findViewById(R.id.profile_display_name);
        mFollowButton =(Button)findViewById(R.id.profile_follow_button);
        mProfileFollowImage = (ImageView)findViewById(R.id.profile_follow_image);
        final String user_id =getIntent().getStringExtra("user_id");


        isFollowed=false;
        mFollowButton.setEnabled(false);
        mProfileFollowImage.setVisibility(View.INVISIBLE);

        mBlog_list = (RecyclerView)findViewById(R.id.profile_blog_list);
        mBlog_list.setHasFixedSize(true);
        mBlog_list.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFollowedDatabase = FirebaseDatabase.getInstance().getReference().child("Followed");
        mBlogDatabase = FirebaseDatabase.getInstance().getReference().child("Blog").child(user_id);


        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(user_id.equals(mFirebaseUser.getUid())){
            mFollowButton.setVisibility(View.INVISIBLE);

        }

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String display_name = dataSnapshot.child("name").getValue().toString();
                String display_image = dataSnapshot.child("thumbnail").getValue().toString();
                mProfileName.setText(display_name);
                if(!display_image.equals("default"))
                    Picasso.with(ProfileActivity.this).load(display_image).placeholder(R.drawable.if_anonymous_334588).into(mProfileimage);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // _____________Friendlist______________
        mFollowedDatabase.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){
                    mProfileFollowImage.setVisibility(View.VISIBLE);
                    mFollowButton.setText("Followed");
                    isFollowed=true;
                }
                else{
                    mProfileFollowImage.setVisibility(View.INVISIBLE);
                    mFollowButton.setText("Follow");
                    isFollowed=false;
                }
                mFollowButton.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFollowed){

                    mFollowedDatabase.child(mFirebaseUser.getUid()).child(user_id).setValue("Followed").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mProfileFollowImage.setVisibility(View.VISIBLE);
                            mFollowButton.setText("Followed");
                            isFollowed=true;
                            Snackbar.make(findViewById(R.id.profile_follow_button),"you are now Following "+mProfileName.getText().toString(),Snackbar.LENGTH_SHORT).show();
                        }
                    });


                }
                else{
                    mFollowedDatabase.child(mFirebaseUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mProfileFollowImage.setVisibility(View.INVISIBLE);
                            mFollowButton.setText("Follow");
                            isFollowed=false;
                            Snackbar.make(findViewById(R.id.profile_follow_button),"Unfollowed "+mProfileName.getText().toString(),Snackbar.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseblogAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(Blog.class,R.layout.blog_row,BlogViewHolder.class,mBlogDatabase) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setImage(model.getImage(),getApplicationContext());
            }
        };
        mBlog_list.setAdapter(firebaseblogAdapter);

    }
    public static class BlogViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public BlogViewHolder(View itemView) {

            super(itemView);
            mView=itemView;
        }
        public void setTitle(String title){
            TextView post_title=(TextView) mView.findViewById(R.id.post_title_id);
            post_title.setText(title);
        }
        public void setDescription(String description){
            TextView post_desc=(TextView) mView.findViewById(R.id.post_desc_id);
            post_desc.setText(description);
        }
        public void setImage(String image, Context ctx){
            ImageView post_image =(ImageView) mView.findViewById(R.id.post_image_id);
            Picasso.with(ctx).load(image).placeholder(R.drawable.loading).into(post_image);
        }
    }
}
