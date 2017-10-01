package com.example.hidnam.firebase_1;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FollowFragment extends Fragment {


    public FollowFragment() {
        // Required empty public constructor
    }

    private RecyclerView mFollowList;
    private DatabaseReference mFollowedReference;
    private FirebaseUser mCurrentUser;
    private   Map<String,String> followlist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_follow, container, false);
        mFollowList = (RecyclerView)view.findViewById(R.id.follow_list);
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        mFollowedReference= FirebaseDatabase.getInstance().getReference().child("Followed").child(mCurrentUser.getUid());
        mFollowList.setHasFixedSize(true);
        mFollowList.setLayoutManager(new LinearLayoutManager(getActivity()));
        new LoadFollowers().execute();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


    }
    private class LoadFollowers extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... voids) {
             //final  Map<String,String> followlist;
            mFollowedReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    followlist = dataSnapshot.getValue(Map.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
