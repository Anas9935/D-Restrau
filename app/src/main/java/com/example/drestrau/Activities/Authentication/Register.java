package com.example.drestrau.Activities.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drestrau.Activities.User.GeneralUserNew;
import com.example.drestrau.R;
import com.example.drestrau.Objects.users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
EditText number,email,password;
ImageView save;
FirebaseAuth mAuth;
FirebaseDatabase db;
DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reguster);
    initialize();
    mAuth=FirebaseAuth.getInstance();
    db=FirebaseDatabase.getInstance();
    mRef=db.getReference("users");
    save.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createUser();
        }
    });
    }
    private void createUser(){
        String e=email.getText().toString();
        String p=password.getText().toString();
        mAuth.createUserWithEmailAndPassword(e, p)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("thisReg", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            savedata(user);

                            loginUser(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("register", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                });
    }
    private void loginUser(FirebaseUser user){
        if(user==null){
            return;}
        String e=email.getText().toString();
        String  p=password.getText().toString();
        mAuth.signInWithEmailAndPassword(e,p)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("ThisAuth", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            enterUser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("thisAuth", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                            enterUser(null);
                        }
                    }
                });
    }
    private void savedata(FirebaseUser user) {
      //  Log.e("this", "savedata: data saved");
        String phno=number.getText().toString();
        users newuser=new users();
        newuser.setUid(user.getUid());
        newuser.setPhno1(Long.parseLong(phno));
        newuser.setEmail(email.getText().toString());
        mRef.child(user.getUid()).setValue(newuser);
    }

    private void enterUser(FirebaseUser user){
        if(user==null){
            return;
        }

        Intent intent=new Intent(Register.this, GeneralUserNew.class);
        intent.putExtra("uid",user.getUid());
        startActivity(intent);
        finish();
    }
    private void initialize(){
        number=findViewById(R.id.reg_phNumber);
        email=findViewById(R.id.reg_email);
        password=findViewById(R.id.reg_password);
        save=findViewById(R.id.reg_saveBtn);
    }
}
