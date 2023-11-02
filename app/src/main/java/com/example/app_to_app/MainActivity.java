package com.example.app_to_app;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;


import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nexmo.client.NexmoCall;
import com.nexmo.client.NexmoCallEventListener;
import com.nexmo.client.NexmoCallMemberStatus;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoMediaActionState;
import com.nexmo.client.NexmoMember;
import com.nexmo.client.request_listener.NexmoApiError;
import com.nexmo.client.request_listener.NexmoConnectionListener;
import com.nexmo.client.request_listener.NexmoRequestListener;






public class MainActivity extends AppCompatActivity {

    private NexmoClient client;
    private String otherUser = "";
    private NexmoCall onGoingCall;


    private TextView connectionStatusTextView;
    private TextView waitingForIncomingCallTextView;
    private Button loginAsAlice;
    private Button loginAsBob;
    private Button startCallButton;
    private Button answerCallButton;
    private Button rejectCallButton;
    private Button endCallButton;


    private NexmoCallEventListener callListener;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // request permissions
        String[] callsPermissions = {Manifest.permission.RECORD_AUDIO};
        ActivityCompat.requestPermissions(this, callsPermissions, 123);


        // init views
        connectionStatusTextView = findViewById(R.id.connectionStatusTextView);
        waitingForIncomingCallTextView = findViewById(R.id.waitingForIncomingCallTextView);
        loginAsAlice = findViewById(R.id.loginAsAlice);
        loginAsBob = findViewById(R.id.loginAsBob);
        startCallButton = findViewById(R.id.startCallButton);
        answerCallButton = findViewById(R.id.answerCallButton);
        rejectCallButton = findViewById(R.id.rejectCallButton);
        endCallButton = findViewById(R.id.endCallButton);


        // Initialize the client object
        client = new NexmoClient.Builder().build(this);

// Add null checks before calling any methods on the client object
        if (client != null) {
            // Set the connection listener
            client.setConnectionListener((connectionStatus, connectionStatusReason) -> {
                runOnUiThread(() -> {
                    connectionStatusTextView.setText(connectionStatus.toString());
                });

                if (connectionStatus == NexmoConnectionListener.ConnectionStatus.CONNECTED) {
                    runOnUiThread(() -> {
                        hideUI();
                        connectionStatusTextView.setVisibility(View.VISIBLE);
                        startCallButton.setVisibility(View.VISIBLE);
                        waitingForIncomingCallTextView.setVisibility(View.VISIBLE);
                    });
                }
            });

            // Add incoming call listener
            client.addIncomingCallListener(it -> {
                onGoingCall = it;

                runOnUiThread(() -> {
                    hideUI();
                    answerCallButton.setVisibility(View.VISIBLE);
                    rejectCallButton.setVisibility(View.VISIBLE);
                });
            });
        } else {
            // Handle null client object
            Log.e(TAG, "Client object is null");
        }

        loginAsAlice.setOnClickListener(v -> loginAsAlice());
        loginAsBob.setOnClickListener(v -> loginAsBob());

        if (client!=null){
            client.setConnectionListener((connectionStatus, connectionStatusReason) -> {
                runOnUiThread(() -> {
                    connectionStatusTextView.setText(connectionStatus.toString());
                });

                if (connectionStatus == NexmoConnectionListener.ConnectionStatus.CONNECTED) {
                    runOnUiThread(() -> {
                        hideUI();
                        connectionStatusTextView.setVisibility(View.VISIBLE);
                        startCallButton.setVisibility(View.VISIBLE);
                        waitingForIncomingCallTextView.setVisibility(View.VISIBLE);
                    });
                }
            });

            client.addIncomingCallListener(it -> {
                onGoingCall = it;

                runOnUiThread(() -> {
                    hideUI();
                    answerCallButton.setVisibility(View.VISIBLE);
                    rejectCallButton.setVisibility(View.VISIBLE);
                });
            });
        }



        answerCallButton.setOnClickListener(view -> { answerCall();});
        rejectCallButton.setOnClickListener(view -> { rejectCall();});
        endCallButton.setOnClickListener(view -> { endCall();});


        callListener = new NexmoCallEventListener() {
            @Override
            public void onMemberStatusUpdated(NexmoCallMemberStatus callStatus, NexmoMember NexmoMember) {
                if (callStatus == NexmoCallMemberStatus.COMPLETED || callStatus == NexmoCallMemberStatus.CANCELLED) {
                    onGoingCall = null;

                    runOnUiThread(() -> {
                        hideUI();
                        waitingForIncomingCallTextView.setVisibility(View.VISIBLE);
                        startCallButton.setVisibility(View.VISIBLE);
                    });
                }
            }

            @Override
            public void onMuteChanged(NexmoMediaActionState newState, NexmoMember member) {}
            @Override
            public void onEarmuffChanged(NexmoMediaActionState newState, NexmoMember member) {}
            @Override
            public void onDTMF(String dtmf, NexmoMember member) {}
        };

        startCallButton.setOnClickListener(v -> startCall());

    }

    private void loginAsAlice() {
        otherUser = "Bob";
        client.login("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2OTg5MDg0NDksImp0aSI6IjhlMjY0NzIwLTc5NGQtMTFlZS04YzMxLTNkMTg5MzlmN2VjYSIsImFwcGxpY2F0aW9uX2lkIjoiMzQ4MmEyMDktZmZmMC00YWUwLWFlNWEtMThmYTA4N2I0Mjg1Iiwic3ViIjoiQWxpY2UiLCJleHAiOjE2OTg5MDg0NzE1MzQsImFjbCI6eyJwYXRocyI6eyIvKi91c2Vycy8qKiI6e30sIi8qL2NvbnZlcnNhdGlvbnMvKioiOnt9LCIvKi9zZXNzaW9ucy8qKiI6e30sIi8qL2RldmljZXMvKioiOnt9LCIvKi9pbWFnZS8qKiI6e30sIi8qL21lZGlhLyoqIjp7fSwiLyovYXBwbGljYXRpb25zLyoqIjp7fSwiLyovcHVzaC8qKiI6e30sIi8qL2tub2NraW5nLyoqIjp7fSwiLyovbGVncy8qKiI6e319fX0.Z0ismGtqRD-7NSeqQEeIqPFek_0npcjgKd9wC7pdMvhKl-dACLjy8uXgvT7MRmNC8SJYOvS-AqHkdBaJ2M5c9iv1-DWYlQ_vvHjzUxCVHQQKN6lYPCCq7i9H9XaCVvC2Fh-sdV2KK6_-xFSvYzDm5oBDeJdm0e0wJboh4V-shwKpGwiNtV09TLdjEhY4uNd-ogJ8bMhhVjthkFgG5Ih9CBNP8-Nqp8mDS8ubqKBrWue1veT624MAa3YiLRP0ZqSZcW_TxydA7K6zI562NWr_11Tq-2UJBKDlfSyuyeqOzzwcYjSwqIX3FfQ3fJygNvN7rMG2ZTJxxeXXVwPINwcK8A");

    }

    private void loginAsBob() {
        otherUser = "Alice";
        client.login("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2OTg5MDg3MzIsImp0aSI6IjM2YjFiNTUwLTc5NGUtMTFlZS05NGQyLWVmMDIxNWU4ODM1ZSIsImFwcGxpY2F0aW9uX2lkIjoiMzQ4MmEyMDktZmZmMC00YWUwLWFlNWEtMThmYTA4N2I0Mjg1Iiwic3ViIjoiQm9iIiwiZXhwIjoxNjk4OTA4NzU0MzA4LCJhY2wiOnsicGF0aHMiOnsiLyovdXNlcnMvKioiOnt9LCIvKi9jb252ZXJzYXRpb25zLyoqIjp7fSwiLyovc2Vzc2lvbnMvKioiOnt9LCIvKi9kZXZpY2VzLyoqIjp7fSwiLyovaW1hZ2UvKioiOnt9LCIvKi9tZWRpYS8qKiI6e30sIi8qL2FwcGxpY2F0aW9ucy8qKiI6e30sIi8qL3B1c2gvKioiOnt9LCIvKi9rbm9ja2luZy8qKiI6e30sIi8qL2xlZ3MvKioiOnt9fX19.k-R0anNnlh0DSZbP3IZ7QFCpKBG4vsReFsHaQ84GqrN3r4ba51uggVen4nA5F_yM3K4Ozg2XRkrLRo9KZPeQRhPXBw48E52tG1s2Jqdpz3UKW1kcA8mpoUgsyrF-kqNqm2uTxFomwpRQj8bpq5DQrFo7q88nqZG9yi0gdrLOVM-O7sO5mdyQQ95MDWWqUBM-No2MnElktCa6Xw91ag8VOSL518gZ1wiDCJjGFOCMHLutRszKTj8rqVtgI758c-zEZ4Afe23jfr69Ca9QHNXdALCDHexLl7cLDWXtn-u30_TKBumMDbh4U9DFinKRpM9q3VgFEXyy2dchNVJ0t7IPRg");

    }

    private void hideUI() {
        LinearLayout content = findViewById(R.id.content);

        for (int i = 0; i < content.getChildCount(); i++) {
            View view = content.getChildAt(i);
            view.setVisibility(View.GONE);
        }
    }


    @SuppressLint("MissingPermission")
    private void answerCall() {
        onGoingCall.answer(new NexmoRequestListener<NexmoCall>() {
            @Override
            public void onError(@NonNull NexmoApiError nexmoApiError) {

            }

            @Override
            public void onSuccess(@Nullable NexmoCall nexmoCall) {
                onGoingCall.addCallEventListener(callListener);
                runOnUiThread(() -> {
                    hideUI();
                    endCallButton.setVisibility(View.VISIBLE);
                });
            }
        });
    }

    private void rejectCall() {
        onGoingCall.hangup(new NexmoRequestListener<NexmoCall>() {
            @Override
            public void onError(@NonNull NexmoApiError nexmoApiError) {

            }

            @Override
            public void onSuccess(@Nullable NexmoCall nexmoCall) {
                runOnUiThread(() -> {
                    hideUI();
                    startCallButton.setVisibility(View.VISIBLE);
                    waitingForIncomingCallTextView.setVisibility(View.VISIBLE);
                });
            }
        });
        onGoingCall = null;
    }
    private void endCall() {
        onGoingCall.hangup(new NexmoRequestListener<NexmoCall>() {
            @Override
            public void onError(@NonNull NexmoApiError nexmoApiError) {

            }

            @Override
            public void onSuccess(@Nullable NexmoCall nexmoCall) {
                runOnUiThread(() -> {
                    hideUI();
                    startCallButton.setVisibility(View.VISIBLE);
                    waitingForIncomingCallTextView.setVisibility(View.VISIBLE);
                });
            }
        });

        onGoingCall = null;
    }

    @SuppressLint("MissingPermission")
    private void startCall() {
        client.serverCall(otherUser, null, new NexmoRequestListener<NexmoCall>() {
            @Override
            public void onError(@NonNull NexmoApiError nexmoApiError) {

            }

            @Override
            public void onSuccess(@Nullable NexmoCall call) {
                runOnUiThread(() -> {
                    hideUI();
                    endCallButton.setVisibility(View.VISIBLE);
                    waitingForIncomingCallTextView.setVisibility(View.INVISIBLE);
                });

                onGoingCall = call;

                onGoingCall.addCallEventListener(callListener);
            }
        });
    }


}

