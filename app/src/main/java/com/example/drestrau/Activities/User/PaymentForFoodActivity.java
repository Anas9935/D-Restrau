package com.example.drestrau.Activities.User;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.drestrau.Objects.paymentObject;
import com.example.drestrau.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class PaymentForFoodActivity extends AppCompatActivity {
    private static final String TAG ="payment" ;
    private String rid;
    private String selKey;
private int payableAmt;
private int totAmt;
private RadioButton par;
    private RadioButton full;
private TextView amount;
private Button debit;
    private Button paytm;
    private Button next;
    private Button cancel;
    private Button paytm_next;
private EditText cName;
    private EditText cNumber;
    private EditText civ;
    private EditText phnPaytm;
private CheckBox isSaveCard;
private LinearLayout cardLayout;
    private LinearLayout paytmLayout;
private int month;
    private int year;
private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_for_food);
        totAmt=getIntent().getIntExtra("totalAmt",0);
        rid=getIntent().getStringExtra("rid");
        selKey=getIntent().getStringExtra("selKey");
        initialiseView();

        payableAmt=(int)(0.4*totAmt);
        Log.e(TAG, "onCreate: "+payableAmt );
        amount.setText(String.valueOf(payableAmt));
        par.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    full.setChecked(false);
                    setPayAmount();
                }
                if(!isChecked){
                    par.setChecked(false);
                    setPayAmount();
                }
            }
        });
        par.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setPayAmount();
            }
        });
        full.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setPayAmount();
            }
        });

        debit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardLayout.setVisibility(View.VISIBLE);
                paytmLayout.setVisibility(View.GONE);
                initiateCard();
            }
        });
        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardLayout.setVisibility(View.VISIBLE);
                paytmLayout.setVisibility(View.GONE);
                initiatePaytm();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initialiseView(){
        par=findViewById(R.id.payment_partial_btn);
        full=findViewById(R.id.payment_Full_btn);
        amount=findViewById(R.id.payment_amountToPay);
        debit=findViewById(R.id.payment_Card);
        paytm=findViewById(R.id.payment_paytm_btn);
        next=findViewById(R.id.payment_card_proceed_btn);
        cancel=findViewById(R.id.payment_back_btn);
        cName=findViewById(R.id.payment_card_name);
        cNumber=findViewById(R.id.payment_card_no);
        civ=findViewById(R.id.payment_card_civ);
        isSaveCard=findViewById(R.id.payment_save_card_check);
        cardLayout=findViewById(R.id.payment_Card_LL);
        phnPaytm=findViewById(R.id.payment_paytm_edit);
        paytmLayout=findViewById(R.id.payment_paytm_layout);
        paytm_next=findViewById(R.id.payment_paytm_proceed_btn);
    }
    private void initiateCard(){
        //layoutVisible
        SharedPreferences pref=getSharedPreferences("cardNumber",MODE_PRIVATE);
        editor=pref.edit();
        String sharedCardNo=pref.getString("cardNumber",null);
        String sharedName=pref.getString("cardName",null);
        month=pref.getInt("cardMonth",0);
        int yearSystem= Calendar.getInstance().get(Calendar.YEAR);
        year=pref.getInt("cardYear",yearSystem);
        cNumber.setText(sharedCardNo);
        cName.setText(sharedName);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=cName.getText().toString();
                String number=cNumber.getText().toString();
                String cvString=civ.getText().toString();

                long time=System.currentTimeMillis()/1000;
                paymentObject paymentObject = null;
                if(par.isChecked()){
                    paymentObject=new paymentObject(FirebaseAuth.getInstance().getUid(),null,totAmt,1,1,time);
                }else if(full.isChecked()){
                    paymentObject=new paymentObject(FirebaseAuth.getInstance().getUid(),null,totAmt,2,1,time);
                }

                String payKey;
                payKey= FirebaseDatabase.getInstance().getReference("payments").child(rid).push().getKey();
                if(payKey!=null&&paymentObject!=null) {
                    paymentObject.setPid(payKey);
                    FirebaseDatabase.getInstance().getReference("payments").child(String.valueOf(rid)).child(payKey).setValue(paymentObject);
                }


                if(isSaveCard.isChecked()){
                    editor.putString("cardName", name);
                    editor.putString("cardNumber",number);
                    editor.putInt("cardMonth",month);
                    editor.putInt("cardYear",year);
                    editor.commit();
                }

                //send data into fragment as selKey,payKey,rid
                Bundle bundle=new Bundle();
                bundle.putString("rid",rid);
                bundle.putString("selKey",selKey);
                bundle.putString("payKey",payKey);
                bundle.putString("totAmount",String.valueOf(totAmt));

                Fragment billFragment=new BillFragment();
                billFragment.setArguments(bundle);

                FragmentManager manager=getSupportFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.add(R.id.payment_fragment,billFragment).commit();

            }
        });
    }
    private void initiatePaytm(){
        long phno=Long.parseLong(phnPaytm.getText().toString());
        //proceed it with the payment and then show the fragment
        paytm_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payKey;

                long time=System.currentTimeMillis()/1000;
                paymentObject paymentObject = null;
                if(par.isChecked()){
                    paymentObject=new paymentObject(FirebaseAuth.getInstance().getUid(),null,totAmt,1,2,time);
                }else if(full.isChecked()){
                    paymentObject=new paymentObject(FirebaseAuth.getInstance().getUid(),null,totAmt,2,2,time);
                }
                payKey= FirebaseDatabase.getInstance().getReference("payments").child(rid).push().getKey();
                if(payKey!=null&&paymentObject!=null) {
                    paymentObject.setPid(payKey);
                    FirebaseDatabase.getInstance().getReference("payments").child(String.valueOf(rid)).child(payKey).setValue(paymentObject);
                }
                Bundle bundle=new Bundle();
                bundle.putString("rid",rid);
                bundle.putString("selKey",selKey);
                bundle.putString("payKey",payKey);
                bundle.putString("totAmount",String.valueOf(totAmt));

                Fragment billFragment=new BillFragment();
                billFragment.setArguments(bundle);

                FragmentManager manager=getSupportFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.add(R.id.payment_fragment,billFragment).commit();

            }
        });
    }
    private void setPayAmount(){
        if(par.isChecked()){
            payableAmt=(int)(0.4*totAmt);
            amount.setText(String.valueOf(payableAmt));
        }else if(full.isChecked()){
            payableAmt=totAmt;
            amount.setText(String.valueOf(payableAmt));
        }

    }
}
