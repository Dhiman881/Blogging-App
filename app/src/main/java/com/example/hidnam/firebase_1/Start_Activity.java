package com.example.hidnam.firebase_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Start_Activity extends AppCompatActivity {
    private Button mSignup;
    private Button mSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_);
        mSignup =(Button)findViewById(R.id.btn_sign_up);
        mSignIn =(Button)findViewById(R.id.btn_sign_in);
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg_intent = new Intent(Start_Activity.this,Register_Activity.class);
                startActivity(reg_intent);
            }
        });
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent log_intent = new Intent(Start_Activity.this,Login_Activity.class);
                startActivity(log_intent);
            }
        });
    }
}
