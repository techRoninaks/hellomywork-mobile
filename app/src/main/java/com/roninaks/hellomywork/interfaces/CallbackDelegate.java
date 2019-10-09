package com.roninaks.hellomywork.interfaces;

import java.util.HashMap;

public interface CallbackDelegate {
    void onResultReceived(String type, boolean resultCode, HashMap<String, String> extras);
}
