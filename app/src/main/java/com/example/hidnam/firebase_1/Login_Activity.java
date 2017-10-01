package com.example.hidnam.firebase_1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText mEmail;
    private EditText mPass;
    private Button mSignIn;
    private CheckBox mCheckbox;
    private Toolbar mToolbar;
    private ProgressBar mProgressbar;

    //private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mSignIn = (Button) findViewById(R.id.btn_login);
        mEmail = (EditText) findViewById(R.id.login_email);
        mPass = (EditText) findViewById(R.id.login_pass);
        mCheckbox = (CheckBox) findViewById(R.id.checkBox_login);
        mCheckbox.setChecked(false);
        mProgressbar =(ProgressBar)findViewById(R.id.login_progress);
        mProgressbar.setVisibility(View.INVISIBLE);
        // mAuth = FirebaseAuth.getInstance();
        mToolbar = (Toolbar) findViewById(R.id.login_tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int start, end;
                // Log.i("inside checkbox chnge",""+b);
                if (!b) {
                    start = mPass.getSelectionStart();
                    end = mPass.getSelectionEnd();
                    mPass.setTransformationMethod(new PasswordTransformationMethod());
                    ;
                    mPass.setSelection(start, end);
                } else {
                    start = mPass.getSelectionStart();
                    end = mPass.getSelectionEnd();
                    mPass.setTransformationMethod(null);
                    mPass.setSelection(start, end);
                }
            }
        });
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String display_email =mEmail.getText().toString();
                String display_pass  = mPass.getText().toString();
                if(!TextUtils.isEmpty(display_email) && !TextUtils.isEmpty(display_pass)) {
                    mProgressbar.setVisibility(View.VISIBLE);
                    login(display_email, display_pass);
                }
                else{
                    Toast.makeText(Login_Activity.this,"Incorrect Input",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        ///Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if(task.isSuccessful()){
                            mProgressbar.setVisibility(View.INVISIBLE);
                            Intent intent  = new Intent(Login_Activity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }
                        if (!task.isSuccessful()) {
                            mProgressbar.setVisibility(View.INVISIBLE);
                            //Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(Login_Activity.this,"Email/Password Incorrect",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}


