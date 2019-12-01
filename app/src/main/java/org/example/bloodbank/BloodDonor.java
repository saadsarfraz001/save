package org.example.bloodbank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.example.database.DatabaseRepo;

import java.util.regex.Pattern;

public class BloodDonor extends Fragment {

    private DatabaseReference mDatabase;
    FirebaseDatabase database;
    GetDonorFromDatabase donors;
    DatabaseReference myRef;
    public static final Pattern contact =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=\\S+$)" +
                    ".{11,}" +
                    "$"
            );
    public static final Pattern age =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=\\S+$)" +
                    ".{2,}" +
                    "$"
            );

    EditText nm;
    EditText cn;
    EditText add;
    EditText ag;
    Button signup;
    DatabaseRepo db;
    Spinner spin;
    String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.blooddonor, container, false);
        db = new DatabaseRepo(getContext());
        signup = (Button) view.findViewById(R.id.button11);
        nm = (EditText) view.findViewById(R.id.editText1);
        add = view.findViewById(R.id.editText31);
        cn = (EditText) view.findViewById(R.id.editText41);
        ag = (EditText) view.findViewById(R.id.editText51);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String[] blood = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        spin = (Spinner) view.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, blood);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                donors=GetDonorFromDatabase.getInstance();
                myRef = database.getReference("Donors");
                if (true) {
                    db.insertDonor(nm.getText().toString(), add.getText().toString(), cn.getText().toString()
                            , Integer.parseInt(ag.getText().toString()), spin.getSelectedItem().toString());
                    userId = myRef.push().getKey();
                    writeNewDonor(nm.getText().toString(), add.getText().toString(), cn.getText().toString(), ag.getText().toString(), spin.getSelectedItem().toString());
                    //check();
                }
            }
        });
        return view;
    }

    public void check() {
        Intent intent = new Intent(getActivity(), Thankyou.class);
        startActivity(intent);
    }

    private boolean validateContact() {
        String contactInput = cn.getText().toString();
        if (contactInput.isEmpty()) {
            cn.setError("Field can't be empty");
            return false;
        } else if (!contact.matcher(contactInput).matches()) {
            cn.setError("Please enter a valid contact number");
            return false;
        } else {
            cn.setError(null);
            return true;
        }
    }

    private boolean validateAge() {
        String ageInput = ag.getText().toString();
        if (ageInput.isEmpty()) {
            ag.setError("Field can't be empty");
            return false;
        } else if (!age.matcher(ageInput).matches()) {
            ag.setError("Please enter a valid age");
            return false;
        } else {
            ag.setError(null);
            return true;
        }
    }

    public boolean confirmInput(View v) {
        if (!validateContact() | validateAge()) {
            return false;
        } else {
            return true;
        }
    }

    private void writeNewDonor(String name, String address, String contact, String age, String bloodGroup) {
        DonorData data = new DonorData(name, address, contact, age, bloodGroup);
        mDatabase.child("Donors").child(userId).setValue(data);
    }

}
