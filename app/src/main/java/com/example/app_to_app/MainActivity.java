package com.example.app_to_app;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;


import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
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




    private void loginAsAlice() {
        otherUser = "Bob";

        client.login("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2OTg2NTUwMDAsImp0aSI6IjcyODliNmYwLTc2ZmYtMTFlZS1hMzJkLWIxMzUwNTJiMDkwZSIsImFwcGxpY2F0aW9uX2lkIjoiMzQ4MmEyMDktZmZmMC00YWUwLWFlNWEtMThmYTA4N2I0Mjg1Iiwic3ViIjoiQWxpY2UiLCJleHAiOjE2OTg2NTUwMjIxNDIsImFjbCI6eyJwYXRocyI6eyIvKi91c2Vycy8qKiI6e30sIi8qL2NvbnZlcnNhdGlvbnMvKioiOnt9LCIvKi9zZXNzaW9ucy8qKiI6e30sIi8qL2RldmljZXMvKioiOnt9LCIvKi9pbWFnZS8qKiI6e30sIi8qL21lZGlhLyoqIjp7fSwiLyovYXBwbGljYXRpb25zLyoqIjp7fSwiLyovcHVzaC8qKiI6e30sIi8qL2tub2NraW5nLyoqIjp7fSwiLyovbGVncy8qKiI6e319fX0.k6gJMsz-HRgmv4-p_VrBM5F-YHvyWrCNIpIVp8sF7pUWsgGkxby3p9oSDXOVYz6kEiNTFQcNT6HzRMQbC921NxhLPf8zp2YgHkKP7nRrx9OtRfXPga0erCa9iBoQuwcHGW31TkdfUcvg-IAhmy9RqmIqxMh5wYxa1NB64mpecfbrji0aj7YXRIFqgLxZSiiXXPUD8goGF9KYAh8cQqunJ6SeZR01L41tgxbv6zkBw7wn7e5SOY4qnyyZ2xEjmA3AglEasnX3yzv2EL4Y_-T1G7LuHTFo0SwFDGP4_9zX4WJgMPvnQzO3XssAzMic5V_M0vEpVBjjN4vhlalVPZftFQ");
    }

    private void loginAsBob() {
        otherUser = "Alice";

        client.login("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2OTg2NTUwNTAsImp0aSI6IjkwOTdhNzYwLTc2ZmYtMTFlZS1iZGEzLTgzYzNkNmNiOWEwZiIsImFwcGxpY2F0aW9uX2lkIjoiMzQ4MmEyMDktZmZmMC00YWUwLWFlNWEtMThmYTA4N2I0Mjg1Iiwic3ViIjoiQm9iIiwiZXhwIjoxNjk4NjU1MDcyNTY2LCJhY2wiOnsicGF0aHMiOnsiLyovdXNlcnMvKioiOnt9LCIvKi9jb252ZXJzYXRpb25zLyoqIjp7fSwiLyovc2Vzc2lvbnMvKioiOnt9LCIvKi9kZXZpY2VzLyoqIjp7fSwiLyovaW1hZ2UvKioiOnt9LCIvKi9tZWRpYS8qKiI6e30sIi8qL2FwcGxpY2F0aW9ucy8qKiI6e30sIi8qL3B1c2gvKioiOnt9LCIvKi9rbm9ja2luZy8qKiI6e30sIi8qL2xlZ3MvKioiOnt9fX19.HYP8mfjxhA8OsYad1KIEeew5uSKZroFUJWBfZhJi5lv7clOMkPUctao43CpNJNmtv6bYm8e41vTf1Vvn7UN9j4Q458d1V5YwAs9xezcjEqmksvKv-pHSwukFn76mAAZXlcMOA7IKQj_WRhXz8OoHgXZlj9i9iPi1nuPvlI30ZNBPF82BQ2Lg0ugj0cqfUPqYV0Dz9gVATLtCEID05BxHvoWkk7FWSk9fiL56T7Ry4ik5OneghusNwZPlLXd_SYGE7VSRpqsuBYLm6rLcTjbqSDsPLWnXckNakMYvXMRK21bPhC3-eebtgFAODOMS4CaUpu_mvf6_tyxvGEVGi0M_1w");
        // TODO: update body
    }

    private void hideUI() {
        LinearLayout content = findViewById(R.id.content);

        for (int i = 0; i < content.getChildCount(); i++) {
            View view = content.getChildAt(i);
            view.setVisibility(View.GONE);
        }
    }


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

        loginAsAlice.setOnClickListener(v -> loginAsAlice());
        loginAsBob.setOnClickListener(v -> loginAsBob());

        client = new NexmoClient.Builder().build(this);
//        NexmoClient client = new NexmoClient.Builder().build(this);
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

        answerCallButton.setOnClickListener(view -> {
            answerCall();
        });
        rejectCallButton.setOnClickListener(view -> {
            rejectCall();
        });
        endCallButton.setOnClickListener(view -> {
            endCall();
        });


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


}