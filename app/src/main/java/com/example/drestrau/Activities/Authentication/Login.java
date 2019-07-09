package com.example.drestrau.Activities.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drestrau.Activities.ChefActivity;
import com.example.drestrau.Activities.Manager.ManagerActivity;
import com.example.drestrau.Activities.ReceptionistActivity;
import com.example.drestrau.Activities.SimpleStaffActivity;
import com.example.drestrau.Activities.User.GeneralUserNew;
import com.example.drestrau.Objects.users;
import com.example.drestrau.R;
import com.example.drestrau.TestingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
EditText emailTV,passwordTV;
Button login,create;
//To delete After Testing
    Button man,user,recep,cook,chef,bar,guard,waiter,cleaner;
RelativeLayout Progbar;
FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth=FirebaseAuth.getInstance();
        login=findViewById(R.id.loginAuth);
        create=findViewById(R.id.createAuth);
        emailTV=findViewById(R.id.usernameAuth);
        passwordTV=findViewById(R.id.passwordAuth);
        Progbar=findViewById(R.id.login_firstView);
        //-------------------------
        //delete between this
        initButtons();
        setupClickListeners();
        //--------------------------

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }
    //-------------------------------------------------
//delete this function
    private void setupClickListeners() {
        man.setOnClickListener(new myClickListener("Manager@gmail.com"));
        user.setOnClickListener(new myClickListener("User@gmail.com"));
        recep.setOnClickListener(new myClickListener("Receptionist@gmail.com"));
        cook.setOnClickListener(new myClickListener("Cook@gmail.com"));
        chef.setOnClickListener(new myClickListener("Chef@gmail.com"));
        bar.setOnClickListener(new myClickListener("Bartender@gmail.com"));
        guard.setOnClickListener(new myClickListener("Guard@gmail.com"));
        waiter.setOnClickListener(new myClickListener("Waiter@gmail.com"));
        cleaner.setOnClickListener(new myClickListener("Cleaner@gmail.com"));

    }
    //delete this class
    class myClickListener implements View.OnClickListener {
        String mail;
        public myClickListener(String email){
            mail=email;
        }
        @Override
        public void onClick(View v) {
            emailTV.setText(mail);
            passwordTV.setText("123456789");
            login();
        }
    }

    //Delete this function
    private void initButtons() {
        man=findViewById(R.id.login_btn_manager);
        user=findViewById(R.id.login_btn_user);
        recep=findViewById(R.id.login_btn_recep);
        cook=findViewById(R.id.login_btn_cook);
        chef=findViewById(R.id.login_btn_chef);
        bar=findViewById(R.id.login_btn_bartender);
        guard=findViewById(R.id.login_btn_guard);
        waiter=findViewById(R.id.login_btn_waiter);
        cleaner=findViewById(R.id.login_btn_cleaner);
    }
    //--------------------------------------------------

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser current=mAuth.getCurrentUser();
        if(current!=null){
            Progbar.setVisibility(View.VISIBLE);
        }
        //do when already entered
        enterUser(current);
    }

    private void login(){
        Progbar.setVisibility(View.VISIBLE);
        String email=emailTV.getText().toString();
        String  password=passwordTV.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
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
                        Toast.makeText(Login.this, "Login failed.",
                                Toast.LENGTH_SHORT).show();
                        enterUser(null);
                    }
}
});
}

        private void enterUser(final FirebaseUser user){

           // final int[] desig=new int[]{-1};
            if(user==null){
                Progbar.setVisibility(View.GONE);
                return;
            }
            FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    users usrs=dataSnapshot.getValue(users.class);
                   // desig[0]=usrs.getDesig();
                    Log.e("entreuser", "enterUser: "+usrs.getDesig() );
                    switch (usrs.getDesig()){
                        case 0:{
                            Intent intent=new Intent(Login.this, ReceptionistActivity.class);
                            intent.putExtra("rid",usrs.getRid());
                            startActivity(intent);
                            break;
                        }
                        case 1:{//chef from which restaurant

                        }
                        case 2:{Intent intent=new Intent(Login.this, ChefActivity.class);
                            intent.putExtra("rid",usrs.getRid());
                            startActivity(intent);
                            break;
                        }
                        case 3:{Intent intent=new Intent(Login.this, SimpleStaffActivity.class);
                            intent.putExtra("rid",usrs.getRid());
                            intent.putExtra("desig",3);
                            startActivity(intent);
                            break;
                        }
                        case 4:{Intent intent=new Intent(Login.this, SimpleStaffActivity.class);
                            intent.putExtra("rid",usrs.getRid());
                            intent.putExtra("desig",4);
                            startActivity(intent);
                            break;
                        }
                        case 5:{Intent intent=new Intent(Login.this, SimpleStaffActivity.class);
                            intent.putExtra("rid",usrs.getRid());
                            intent.putExtra("desig",5);
                            startActivity(intent);
                            break;
                        }
                        case 6:{Intent intent=new Intent(Login.this, SimpleStaffActivity.class);
                            intent.putExtra("rid",usrs.getRid());
                            intent.putExtra("desig",6);
                            startActivity(intent);
                            break;
                        }
                        case 7:{Intent intent=new Intent(Login.this, SimpleStaffActivity.class);
                            intent.putExtra("rid",usrs.getRid());
                            intent.putExtra("desig",7);
                            startActivity(intent);
                            break;
                        }
                        case 8:{
                            Intent intent=new Intent(Login.this, ManagerActivity.class);
                            intent.putExtra("rid", usrs.getRid());
                            startActivity(intent);
                            break;
                        }
                        default:{
                            Intent intent=new Intent(Login.this, GeneralUserNew.class);
                            intent.putExtra("uid",user.getUid());
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onCancelled( DatabaseError databaseError) {

                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_testing:{
                Intent intent=new Intent(Login.this, TestingActivity.class);
                startActivity(intent);
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }
    }
}