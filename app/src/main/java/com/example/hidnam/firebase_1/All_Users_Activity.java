package com.example.hidnam.firebase_1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class All_Users_Activity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mUserList;
    private DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__users_);
        mToolbar =(Toolbar)findViewById(R.id.all_user_tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUserList = (RecyclerView) findViewById(R.id.user_list);
        mUserList.setHasFixedSize(true);
        mUserList.setLayoutManager(new LinearLayoutManager(this));
        //It will Display all user in blog Database,,,
        //Chnage it Here
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Users,UserViewHolder> firebaseAdapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(Users.class,R.layout.user_single_layout,UserViewHolder.class,mDatabaseReference) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, Users model, int position) {
                viewHolder.setName(model.getName());
                //viewHolder.setImage(model.getImage());
                viewHolder.setUserImage(model.getThumbnail(),getApplicationContext());
                final String user_id = getRef(position).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent profileIntent = new Intent(All_Users_Activity.this,ProfileActivity.class);
                        profileIntent.putExtra("user_id",user_id);
                        startActivity(profileIntent);
                    }
                });
            }
        };
        mUserList.setAdapter(firebaseAdapter);

    }
    public static class UserViewHolder extends RecyclerView.ViewHolder {
         View mView;
        public UserViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void setName(String name){
            Log.v("Name",name+"  this is the name");
            TextView userNameView  =  mView.findViewById(R.id.single_name_view);
            userNameView.setText(name);
        }
        public void setUserImage(String thumbnail,Context ctx){
            CircleImageView imageView = (CircleImageView)mView.findViewById(R.id.single_image_view);
            Picasso.with(ctx).load(thumbnail).placeholder(R.drawable.if_anonymous_334588).into(imageView);
        }

    }
}
