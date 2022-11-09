package com.lenzzo.updatedVersionChecker;

public interface VersionCheckListner {
    public void onGetResponse(boolean isUpdateAvailable);
}