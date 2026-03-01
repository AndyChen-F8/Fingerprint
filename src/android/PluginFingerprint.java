package com.ifabula.pluginfingerprint;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PluginFingerprint extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals("authenticate")) {

            JSONObject options = args.getJSONObject(0);
            String message = options.optString("message", "Default authenticate message");

            // Dummy success response
            callbackContext.success("Authenticated: " + message);
            return true;
        }

        if (action.equals("isAvailable")) {
            callbackContext.success("Fingerprint Available");
            return true;
        }

        return false;
    }
}