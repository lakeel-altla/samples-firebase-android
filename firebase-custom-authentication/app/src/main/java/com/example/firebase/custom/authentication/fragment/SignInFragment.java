package com.example.firebase.custom.authentication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firebase.custom.authentication.BuildConfig;
import com.example.firebase.custom.authentication.R;
import com.example.firebase.custom.authentication.activity.MainActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public final class SignInFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = SignInFragment.class.getSimpleName();

    private static final int REQUEST_GOOGLE_SIGN_IN = 1;

    private static final String SECONDARY_FIREBASE_APPLICATION_ID = BuildConfig.SECONDARY_FIREBASE_APPLICATION_ID;

    private static final String SECONDARY_GOOGLE_PROJECT_API_KEY = BuildConfig.SECONDARY_GOOGLE_PROJECT_API_KEY;

    private static final String SECONDARY_FIREBASE_DB_URL = BuildConfig.SECONDARY_FIREBASE_DB_URL;

    private static final String SECONDARY_FIREBASE_APP_NAME = BuildConfig.SECONDARY_FIREBASE_APP_NAME;

    private static final String CUSTOM_AUTH_SERVER_URL = BuildConfig.CUSTOM_AUTH_SERVER_URL;

    private static final String X_FIREBASE_TOKEN = "X-FIREBASE-TOKEN";

    private GoogleApiClient googleApiClient;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        SignInButton signInButton = (SignInButton) view.findViewById(R.id.buttonSignIn);
        signInButton.setOnClickListener(view1 -> {
            // Sign in with google.
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(signInIntent, REQUEST_GOOGLE_SIGN_IN);
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.title_sign_in);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        FirebaseAuth.getInstance().signOut();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Failed to connect to Google api client. Error message=" + connectionResult.getErrorMessage());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle Google sign in.
        if (requestCode == REQUEST_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = result.getSignInAccount();

            // Sign in with Primary Firebase.
            signInWithPrimaryFirebase(account, task -> {
                // User is signed in with Primary Firebase.
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    user.getToken(false).addOnSuccessListener(result1 -> {
                        // Get a token from the primary Firebase.
                        final String token = result1.getToken();
                        Log.d(TAG, "Token:" + token);

                        new Thread(() -> {
                            try {
                                // Send token to custom auth server.
                                String customToken = sendToken(token);
                                Log.d(TAG, "Custom token:" + customToken);

                                // Sign in with the secondary Firebase.
                                // When signed in with the secondary Firebase, show main fragment.
                                signInWithSecondaryFirebase(customToken, result2 -> ((MainActivity) getActivity()).showMainFragment());
                            } catch (IOException e) {
                                Log.e(TAG, "Failed to send token.", e);
                            }
                        }).start();
                    });
                }
            });
        } else {
            Log.e(TAG, "Failed to sign in with google.");
        }
    }

    private void signInWithPrimaryFirebase(GoogleSignInAccount account, OnSuccessListener<AuthResult> onSuccessListener) {
        // Sign in with Primary Firebase project.
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnSuccessListener(onSuccessListener);
    }

    private String sendToken(String token) throws IOException {
        // Use OKHttp.
        Request request = new Request.Builder()
                .url(CUSTOM_AUTH_SERVER_URL)
                .post(null)
                .header(X_FIREBASE_TOKEN, token)
                .build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private void signInWithSecondaryFirebase(String customToken, OnSuccessListener<AuthResult> result) {
        // Secondary Firebase project settings.
        FirebaseOptions options = new FirebaseOptions.Builder()
                // Required for Analytics.
                .setApplicationId(SECONDARY_FIREBASE_APPLICATION_ID)
                // Required for Authentication.
                .setApiKey(SECONDARY_GOOGLE_PROJECT_API_KEY)
                // Required for Realtime Database.
                .setDatabaseUrl(SECONDARY_FIREBASE_DB_URL)
                .build();
        FirebaseApp.initializeApp(getActivity(), options, SECONDARY_FIREBASE_APP_NAME);

        // Sign in with the secondary Firebase.
        FirebaseApp secondaryApp = FirebaseApp.getInstance(SECONDARY_FIREBASE_APP_NAME);
        FirebaseAuth.getInstance(secondaryApp).signInWithCustomToken(customToken)
                .addOnSuccessListener(result);
    }
}
