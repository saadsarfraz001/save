package org.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.example.database.DatabaseRepo;
import org.example.database.Signup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    public static final Pattern PASSWORD =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=\\S+$)" +
                    ".{8,}" +
                    "$"
            );

    EditText ed;
    EditText ps;
    String content;
    String pass;
    List<Signup> list;
    DatabaseRepo db;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login;
        login = (Button) findViewById(R.id.button111);
        ed = (EditText) findViewById(R.id.editText111);
        ps = (EditText) findViewById(R.id.editText3);
        db = new DatabaseRepo(getApplicationContext());
        progressDialog = new ProgressDialog(this);
        list = new ArrayList<>();
        db = new DatabaseRepo(getApplicationContext());
        firebaseAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                content = ed.getText().toString();
                pass = ps.getText().toString();
                if (content.equals("admin@gmail.com") && pass.equals("12345678")) {
                    check();
                } else {
                    db = new DatabaseRepo(getApplicationContext());
                    try {
                        list = db.getAllUsers();
                        userLogin();
                        for (Signup user : list) {
                            if (user.getEmail().equals(content) && user.getPassword().equals(pass)) {
                                check1();
                            }
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void showPermissionAlert(Context context, AppCompatActivity activity){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, 123);
        }
    }
    private boolean validateEmail() {
        String emailInput = ed.getText().toString();
        if (emailInput.isEmpty()) {
            ed.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            ed.setError("Please enter a valid email address");
            return false;
        } else {
            ed.setError(null);
            return true;
        }
    }
    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private boolean validatePassword() {
        String passwordInput = ps.getText().toString();
        if (passwordInput.isEmpty()) {
            ps.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD.matcher(passwordInput).matches()) {
            ps.setError("Password too weak");
            return false;
        } else {
            ps.setError(null);
            return true;
        }
    }

    public boolean confirmInput(View v) {
        if (!validateEmail() | !validatePassword()) {
            return false;
        }

        String input = "Email: " + ed.getText().toString();
        input += "\n";
        input += "Password: " + ps.getText().toString();

        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
        return true;
    }

    public void check() {
        Intent intent = new Intent(Login.this, adminChoice.class);
        startActivity(intent);
    }

    public void check1() {
        Intent intent = new Intent(Login.this, Tab.class);
        startActivity(intent);
    }
    private void userLogin(){
        String email = ed.getText().toString().trim();
        String password  = ps.getText().toString().trim();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Logging in please wait....");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if(task.isSuccessful()){
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), Tab.class));
                        }
                    }
                });

    }

}
