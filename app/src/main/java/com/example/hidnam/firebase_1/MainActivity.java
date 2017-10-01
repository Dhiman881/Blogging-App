package com.example.hidnam.firebase_1;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ViewPager mViewpager;
    private SectionPageAdapter mSectionPageAdaper;
    private TabLayout mTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();


        mToolbar =(Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Blogging App");

        mViewpager =(ViewPager)findViewById(R.id.main_tab_pager);
        mSectionPageAdaper = new SectionPageAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(mSectionPageAdaper);


        mTabLayout =(TabLayout)findViewById(R.id.main_tab_layout);
        mTabLayout.setupWithViewPager(mViewpager);


    }
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser =mAuth.getCurrentUser();
        if(currentUser == null){

            sendToStart();
        }

    }

    private void sendToStart() {
        Intent i = new Intent(MainActivity.this,Start_Activity.class);

        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.main_log_out_btn){
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(MainActivity.this,Start_Activity.class);
            startActivity(i);
            finish();
        }
        if(item.getItemId()==R.id.action_add){
            Intent intent  = new Intent(MainActivity.this,Post_Activity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_settings){
            Intent intent  = new Intent(MainActivity.this,Account_Settings.class);
            startActivity(intent);
        }
        if(item.getItemId() ==R.id.all_Users_Details){
            Intent intent  = new Intent(MainActivity.this,All_Users_Activity.class);
            startActivity(intent);
        }
        return true;
    }
}
