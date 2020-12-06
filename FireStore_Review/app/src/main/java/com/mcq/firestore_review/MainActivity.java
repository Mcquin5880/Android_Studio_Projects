package com.mcq.firestore_review;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    private EditText enterTitle, enterThought;
    private Button saveButton, show_Data_Button;
    private TextView recTitle, recThought;

    public static final String KEY_TITLE = "title";
    public static final String KEY_THOUGHT = "thought";

    private FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();
    private DocumentReference journalReference = firebaseDB.collection("Journal").document("First Thoughts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterTitle = findViewById(R.id.edit_text_title);
        enterThought = findViewById(R.id.edit_text_thought);
        saveButton = findViewById(R.id.save_button);
        show_Data_Button = findViewById(R.id.show_data_button);
        recTitle = findViewById(R.id.rec_title);
        recThought = findViewById(R.id.rec_thought);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = enterTitle.getText().toString().trim();
                String thought = enterThought.getText().toString().trim();

                Map<String, Object> data = new HashMap<>();
                data.put(KEY_TITLE, title);
                data.put(KEY_THOUGHT, thought);

                journalReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });
            }
        });

        show_Data_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                journalReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            String title = documentSnapshot.getString(KEY_TITLE);
                            String thought = documentSnapshot.getString(KEY_THOUGHT);

                            recTitle.setText(title);
                            recThought.setText(thought);

                        } else {
                            Toast.makeText(MainActivity.this, "No data exists.", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        journalReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Toast.makeText(MainActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                }

                if (value != null && value.exists()) {

                    String title = value.getString(KEY_TITLE);
                    String thought = value.getString(KEY_THOUGHT);

                    recTitle.setText(title);
                    recThought.setText(thought);

                }
            }
        });
    }
}