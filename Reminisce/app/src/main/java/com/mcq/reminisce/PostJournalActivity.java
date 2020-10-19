package com.mcq.reminisce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import util.JournalAPI;

public class PostJournalActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_CODE = 1;
    private Button saveButton;
    private ProgressBar progressBar;
    private ImageView addImageButton;
    private ImageView imageView;
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
    private Uri imageURI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_journal);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.post_progressBar);
        currentUserTextView = findViewById(R.id.post_username_textview);
        titleET = findViewById(R.id.post_title_ET);
        thoughtsET = findViewById(R.id.post_description_ET);
        imageView = findViewById(R.id.post_ImageView);

        saveButton = findViewById(R.id.post_save_journal_BTN);
        saveButton.setOnClickListener(this);
        addImageButton = findViewById(R.id.cameraPostButton);
        addImageButton.setOnClickListener(this);

        progressBar.setVisibility(View.INVISIBLE);

        if (JournalAPI.getInstance() != null) {
            currentUserID = JournalAPI.getInstance().getUserID();
            currentUsername = JournalAPI.getInstance().getUsername();

            currentUserTextView.setText(currentUsername);
        }

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {

                } else {

                }
            }
        };


    }

    private void saveJournal() {
        String title = titleET.getText().toString().trim();
        String thoughts = thoughtsET.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(thoughts) && imageURI != null) {

            StorageReference filePath = storageReference.child("journal_images").child("my_image_" + Timestamp.now().getSeconds()); // timestamp to ensure each image has a unique file id
            filePath.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.INVISIBLE);
                    // todo: create journal object
                    // todo: invoke our collection reference
                    //todo: save journal instance
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    // blank for now
                }
            });

        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                imageURI = data.getData();
                imageView.setImageURI(imageURI);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.post_save_journal_BTN:
                //
                break;
            case R.id.cameraPostButton:
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
                break;
        }
    }
}