package com.example.hidnam.firebase_1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Printer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register_Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText mName;
    private EditText  mEmail;
    private EditText mPass;
    private Button mSignUp;
    private CheckBox mCheckbox;
    private Toolbar mToolbar;
    private ProgressBar mProgressbar;
    private DatabaseReference mDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");


        mSignUp =(Button)findViewById(R.id.btn_create);
        mName =(EditText) findViewById(R.id.reg_name);
        mEmail =(EditText) findViewById(R.id.reg_email);
        mPass =(EditText) findViewById(R.id.reg_pass);
        mCheckbox = (CheckBox)findViewById(R.id.checkBox);
        mProgressbar =(ProgressBar)findViewById(R.id.reg_progress);
        mProgressbar.setVisibility(View.INVISIBLE);
        mToolbar = (Toolbar)findViewById(R.id.reg_tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mCheckbox.setChecked(false);

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final String display_name = mName.getText().toString().trim();
               final String display_email = mEmail.getText().toString().trim();
               final String display_pass = mPass.getText().toString();
                if (TextUtils.isEmpty(display_name)|| TextUtils.isEmpty(display_email)|| TextUtils.isEmpty(display_pass))
                    Toast.makeText(Register_Activity.this, "Incorrect Inputs", Toast.LENGTH_SHORT).show();
                else

                {   mProgressbar.setVisibility(View.VISIBLE);
                    registerUser(display_name, display_email, display_pass);

                }
            }


        });

        mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int start,end;
                Log.i("inside checkbox chnge",""+b);
                if(!b){
                    start=mPass.getSelectionStart();
                    end=mPass.getSelectionEnd();
                    mPass.setTransformationMethod(new PasswordTransformationMethod());;
                    mPass.setSelection(start,end);
                }else{
                    start=mPass.getSelectionStart();
                    end=mPass.getSelectionEnd();
                    mPass.setTransformationMethod(null);
                    mPass.setSelection(start,end);
                }
            }
        });

    }

    private void registerUser(final String name, final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            mProgressbar.setVisibility(View.INVISIBLE);


                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                            HashMap<String,String> user_map = new HashMap<String, String>();
                            user_map.put("name",name);
                            user_map.put("image","default");
                            user_map.put("thumbnail","default");
                            mDatabaseReference.child(firebaseUser.getUid()).setValue(user_map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    mProgressbar.setVisibility(View.INVISIBLE);

                                    Intent intent  = new Intent(Register_Activity.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });



                        }

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            mProgressbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(Register_Activity.this,"Error While SignUp",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}
