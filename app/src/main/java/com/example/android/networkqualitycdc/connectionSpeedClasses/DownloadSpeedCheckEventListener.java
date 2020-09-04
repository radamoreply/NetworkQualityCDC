package com.example.android.networkqualitycdc.connectionSpeedClasses;

import com.example.android.networkqualitycdc.myapplication.ChooseConnectivityCheckActivity.SPEED;

public interface DownloadSpeedCheckEventListener {
    public void onEventCompleted(SPEED speed);
    public void onEventFailed();
}