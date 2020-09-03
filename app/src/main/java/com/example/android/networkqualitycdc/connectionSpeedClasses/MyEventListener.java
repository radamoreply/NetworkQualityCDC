package com.example.android.networkqualitycdc.connectionSpeedClasses;

import com.example.android.networkqualitycdc.myapplication.ChooseConnectivityCheckActivity.SPEED;

public interface MyEventListener {
    public void onEventCompleted(SPEED speed);
    public void onEventFailed();
}