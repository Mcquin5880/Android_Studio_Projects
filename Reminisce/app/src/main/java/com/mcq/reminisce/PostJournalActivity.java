package com.mcq.reminisce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public class PostJournalActivity extends AppCompatActivity {

    private Button saveButton;
    private ProgressBar progressBar;
    private ImageView addImageButton;
    private TextView currentUserTextView;
    private EditText titleET;
    private EditText thoughtsET;

    private String currentUserID;
    private String currentUsername;

    // ---------------------------------------------------------------------------------------------
    // Firebase
    // ---------------------------------------------------------------------------------------------
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    // connection to Firestore
    private FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private CollectionReference collectionReference = firebaseDB.collection("Journal");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_journal);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.post_progressBar);
        currentUserTextView = findViewById(R.id.post_username_textview);
        titleET = findViewById(R.id.post_title_ET);
        thoughtsET = findViewById(R.id.post_description_ET);


    }
}