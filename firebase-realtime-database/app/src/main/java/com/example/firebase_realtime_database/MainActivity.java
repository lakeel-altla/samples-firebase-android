package com.example.firebase_realtime_database;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static class User {

        public String id = "user001";

        public String name = "Legend";

        @Override
        public String toString() {
            return "ID: " + id + " Name: " + name;
        }
    }

    private static final String TAG = MainActivity.class.getSimpleName();

    private DatabaseReference mDatabaseReference;

    @BindView(R.id.text)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // NOTE:
        // Firebase requires user authentication.
        // This sample app does not authenticate to Firebase.
        // Therefore, you need to change Firebase rules that does not require authentication.
        // Rules here(https://firebase.google.com/docs/database/security/?hl=ja)

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    @OnClick(R.id.add)
    public void onAdd(View view) {
        mDatabaseReference.push().setValue(new User())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Succeed to add user");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Failed to add user");
                    }
                });
    }

    @OnClick(R.id.find)
    public void onFind(View view) {
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StringBuilder builder = new StringBuilder();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    builder.append(user.toString());
                    builder.append(System.getProperty("line.separator"));
                }

                mTextView.setText(builder.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Canceled to find user", databaseError.toException());
            }
        });
    }

    @OnClick(R.id.update)
    public void onUpdate(View view) {
        // Find users.
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    user.name = "Works";

                    // Update user.
                    mDatabaseReference.child(snapshot.getKey()).setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "Succeed to update user");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Failed to update user");
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Canceled to find user", databaseError.toException());
            }
        });
    }

    @OnClick(R.id.remove)
    public void onRemove(View view) {
        mDatabaseReference.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.d(TAG, "Succeed to remove user");
                } else {
                    Log.d(TAG, "Failed to remove user");
                }
            }
        });
    }
}
