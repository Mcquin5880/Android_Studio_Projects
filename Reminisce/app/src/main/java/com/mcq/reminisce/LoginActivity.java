package com.mcq.reminisce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import util.JournalAPI;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button createAccountButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressBar progressBar;
    private AutoCompleteTextView emailAddress;
    private EditText password;

    // ---------------------------------------------------------------------------------------------
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;


    private FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseDB.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.email_sign_in_button);
        createAccountButton = findViewById(R.id.create_acct_button);
        //emailEditText = findViewById(R.id.email_account);
        //passwordEditText = findViewById(R.id.password_account);
        emailAddress = findViewById(R.id.email_account);
        password = findViewById(R.id.password_account);

        progressBar = findViewById(R.id.login_progress);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserWithEmailAndPassword(emailAddress.getText().toString().trim(), password.getText().toString().trim());
            }
        });
    }

    private void loginUserWithEmailAndPassword(String email, String password) {

        progressBar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    assert firebaseUser != null;
                    String currentUserID = firebaseUser.getUid();

                    collectionReference.whereEqualTo("userID", currentUserID).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {}
                            assert value != null;
                            if (!value.isEmpty()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                for (QueryDocumentSnapshot snapshot : value) {
                                    JournalAPI journalAPI = JournalAPI.getInstance();
                                    journalAPI.setUsername(snapshot.getString("username"));
                                    journalAPI.setUserID(snapshot.getString("userID"));

                                    // Go to list activity
                                    startActivity(new Intent(LoginActivity.this, PostJournalActivity.class));
                                }
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

        } else {
            Toast.makeText(LoginActivity.this, "Please enter your email and password.", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}