package com.example.firebase.custom.authentication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase.custom.authentication.data.Beacon;
import com.example.firebase.custom.authentication.BuildConfig;
import com.example.firebase.custom.authentication.R;
import com.example.firebase.custom.authentication.data.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public final class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private static final String SECONDARY_FIREBASE_APP_NAME = BuildConfig.SECONDARY_FIREBASE_APP_NAME;

    private static final String PRIMARY_FIREBASE_DB_USER_URL = BuildConfig.PRIMARY_FIREBASE_DB_URL + "user";

    private static final String SECONDARY_FIREBASE_DB_BEACON_URL = BuildConfig.SECONDARY_FIREBASE_DB_URL + "beacon";

    @BindView(R.id.textViewUser)
    TextView userTextView;

    @BindView(R.id.textViewBeacon)
    TextView beaconTextView;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.title_main);

        // Check whether user ids are same.

        FirebaseAuth primaryFirebaseAuth = FirebaseAuth.getInstance();
        if (primaryFirebaseAuth.getCurrentUser() != null) {
            String uid = primaryFirebaseAuth.getCurrentUser().getUid();
            Log.d(TAG, "Primary Firebase uid:" + uid);
        }

        FirebaseApp secondaryApp = FirebaseApp.getInstance(SECONDARY_FIREBASE_APP_NAME);
        FirebaseAuth secondaryFirebaseAuth = FirebaseAuth.getInstance(secondaryApp);
        if (secondaryFirebaseAuth.getCurrentUser() != null) {
            String uid = secondaryFirebaseAuth.getCurrentUser().getUid();
            Log.d(TAG, "Secondary Firebase uid:" + uid);
        }
    }

    @OnClick(R.id.buttonFindUser)
    public void onFindUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl(PRIMARY_FIREBASE_DB_USER_URL);
        reference.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user == null) {
                            Log.d(TAG, "User not found.");
                            userTextView.setText(R.string.textView_user_not_found);
                        } else {
                            Log.d(TAG, user.toString());
                            userTextView.setText(user.toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Failed to find user.", databaseError.toException());
                    }
                });
    }

    @OnClick(R.id.buttonAddUser)
    public void onAddUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();

        User user = new User();
        user.userName = firebaseUser.getDisplayName();
        user.email = firebaseUser.getEmail();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl(PRIMARY_FIREBASE_DB_USER_URL);
        reference.child(uid)
                .setValue(user)
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), R.string.message_added, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Log.e(TAG, "Failed to add user.", e));
    }

    @OnClick(R.id.buttonRemoveUser)
    public void onRemoveUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl(PRIMARY_FIREBASE_DB_USER_URL);
        reference.child(uid)
                .removeValue()
                .addOnSuccessListener(aVoid -> {
                    userTextView.setText(null);
                    Toast.makeText(getContext(), R.string.message_removed, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to remove user.", e));
    }

    @OnClick(R.id.buttonFindBeacon)
    public void onFindBeacon() {
        FirebaseApp secondaryApp = FirebaseApp.getInstance(SECONDARY_FIREBASE_APP_NAME);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance(secondaryApp).getCurrentUser();
        String uid = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance(secondaryApp).getReferenceFromUrl(SECONDARY_FIREBASE_DB_BEACON_URL);
        reference.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Beacon beacon = dataSnapshot.getValue(Beacon.class);
                        if (beacon == null) {
                            Log.d(TAG, "Beacon not found.");
                            beaconTextView.setText(R.string.textView_beacon_not_found);
                        } else {
                            Log.d(TAG, beacon.toString());
                            beaconTextView.setText(beacon.toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Failed to find beacon.", databaseError.toException());
                    }
                });
    }

    @OnClick(R.id.buttonAddBeacon)
    public void onAddBeacon() {
        FirebaseApp secondaryApp = FirebaseApp.getInstance(SECONDARY_FIREBASE_APP_NAME);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance(secondaryApp).getCurrentUser();
        String uid = firebaseUser.getUid();

        Beacon beacon = new Beacon();
        beacon.beaconName = "Eddystone";
        beacon.price = 2000;

        DatabaseReference reference = FirebaseDatabase.getInstance(secondaryApp).getReferenceFromUrl(SECONDARY_FIREBASE_DB_BEACON_URL);
        reference.child(uid)
                .setValue(beacon)
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), R.string.message_added, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Log.e(TAG, "Failed to add beacon.", e));
    }

    @OnClick(R.id.buttonRemoveBeacon)
    public void onRemoveBeacon() {
        FirebaseApp secondaryApp = FirebaseApp.getInstance(SECONDARY_FIREBASE_APP_NAME);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance(secondaryApp).getCurrentUser();
        String uid = firebaseUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance(secondaryApp).getReferenceFromUrl(SECONDARY_FIREBASE_DB_BEACON_URL);
        reference.child(uid)
                .removeValue()
                .addOnSuccessListener(aVoid -> {
                    beaconTextView.setText(null);
                    Toast.makeText(getContext(), R.string.message_removed, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to remove beacon.", e));
    }
}
