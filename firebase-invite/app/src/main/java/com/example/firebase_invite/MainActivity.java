package com.example.firebase_invite;

import com.google.android.gms.appinvite.AppInviteInvitation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This activity send a invitation message.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_CODE_INVITE = 999;

    private static final String INVITATION_TITLE = "Share my recipe!!";

    private static final String INVITATION_MESSAGE = "You have to try my recipe";

    // Custom URI Scheme
    // This uri is written in AndroidManifest.xml.
    private static final String DEEP_LINK = "my-app://example.com/recipes/111";

    private static final String INVITATION_CUSTOM_IMAGE = "http://blog-imgs-60.fc2.com/r/a/m/ramenrecipe/IMG_0073.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get Invitation ID.
                // Invitation ID is a unique ID.
                String ids[] = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                Log.d(TAG, "Failed to send invitation");
            }
        }
    }

    @OnClick(R.id.button)
    public void onClick(View view) {
        // Create invitation intent.
        Intent intent = new AppInviteInvitation.IntentBuilder(INVITATION_TITLE).
                setMessage(INVITATION_MESSAGE)
                .setDeepLink(Uri.parse(DEEP_LINK))
                .setCustomImage(Uri.parse(INVITATION_CUSTOM_IMAGE))
                .build();
        startActivityForResult(intent, REQUEST_CODE_INVITE);
    }
}
