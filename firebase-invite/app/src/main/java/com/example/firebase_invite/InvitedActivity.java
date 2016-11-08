package com.example.firebase_invite;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This activity will be launched when you tap invitation URL.
 * Invitation URL should be matched to intent filter in AndroidManifest.xml.
 */
public class InvitedActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = InvitedActivity.class.getSimpleName();

    @BindView(R.id.textViewDeepLink)
    TextView mDeepLink;

    @BindView(R.id.textViewInvitationId)
    TextView mInvitationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invited);

        ButterKnife.bind(this);

        // Create Google API Client instance.
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppInvite.API)
                .enableAutoManage(this, this)
                .build();

        // Parse intent.
        AppInvite.AppInviteApi.getInvitation(googleApiClient, this, false)
                .setResultCallback(new ResultCallback<AppInviteInvitationResult>() {
                    @Override
                    public void onResult(@NonNull AppInviteInvitationResult result) {
                        Log.d(TAG, "getInvitation:onResult:" + result.getStatus());

                        if (result.getStatus().isSuccess()) {
                            Intent intent = result.getInvitationIntent();
                            String deepLink = AppInviteReferral.getDeepLink(intent);
                            String invitationId = AppInviteReferral.getInvitationId(intent);

                            Log.d(TAG, "DeepLink is..." + deepLink);
                            Log.d(TAG, "Invitation id is..." + invitationId);

                            mDeepLink.setText(deepLink);
                            mInvitationId.setText(invitationId);
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}
