package com.mcq.reminisce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

import model.Journal;
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
    private String TAG = "PostJournalActivity";


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

        final String title = titleET.getText().toString().trim();
        final String thoughts = thoughtsET.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(thoughts) && imageURI != null) {

            final StorageReference filePath = storageReference.child("journal_images").child("my_image_" + Timestamp.now().getSeconds()); // timestamp to ensure each image has a unique file id

            filePath.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String imageURL = uri.toString();

                            Journal journal = new Journal();
                            journal.setTitle(title);
                            journal.setThought(thoughts);
                            journal.setImageURL(imageURL);
                            journal.setTimeAdded(new Timestamp(new Date()));
                            journal.setUsername(currentUsername);
                            journal.setUserID(currentUserID);


                            collectionReference.add(journal).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    startActivity(new Intent(PostJournalActivity.this, JournalListActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.getMessage());
                                }
                            });

                            //todo: save journal instance
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