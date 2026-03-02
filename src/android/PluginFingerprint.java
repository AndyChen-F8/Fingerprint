package com.ifabula.pluginfingerprint;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;

public class PluginFingerprint extends CordovaPlugin {

    private static final String AUTHENTICATE = "authenticate";
    private static final String IS_AVAILABLE = "isAvailable";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (AUTHENTICATE.equalsIgnoreCase(action)) {
            JSONObject options = args.getJSONObject(0);
            authenticate(options, callbackContext);
            return true;
        }

        if (IS_AVAILABLE.equalsIgnoreCase(action)) {
            isAvailable(callbackContext);
            return true;
        }

        return false;
    }

    private void isAvailable(CallbackContext callbackContext) {

        BiometricManager biometricManager = BiometricManager.from(cordova.getActivity());

        int result = biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
        );

        if (result == BiometricManager.BIOMETRIC_SUCCESS) {
            callbackContext.success("AVAILABLE");
        } else {
            callbackContext.error("NOT_AVAILABLE");
        }
    }

    private void authenticate(JSONObject options, CallbackContext callbackContext) {

        cordova.getActivity().runOnUiThread(() -> {

            FragmentActivity activity = (FragmentActivity) cordova.getActivity();
            Executor executor = ContextCompat.getMainExecutor(activity);

            BiometricPrompt biometricPrompt =
                    new BiometricPrompt(activity, executor,
                            new BiometricPrompt.AuthenticationCallback() {

                                @Override
                                public void onAuthenticationSucceeded(
                                        @NonNull BiometricPrompt.AuthenticationResult result) {
                                    callbackContext.success("AUTH_SUCCESS");
                                }

                                @Override
                                public void onAuthenticationFailed() {
                                    callbackContext.error("AUTH_FAILED");
                                }

                                @Override
                                public void onAuthenticationError(int errorCode,
                                        @NonNull CharSequence errString) {
                                    callbackContext.error(errString.toString());
                                }
                            });

            String title = options.optString("title", "Fingerprint Authentication");
            String subtitle = options.optString("message", "Scan your fingerprint");

            BiometricPrompt.PromptInfo promptInfo =
                    new BiometricPrompt.PromptInfo.Builder()
                            .setTitle(title)
                            .setSubtitle(subtitle)
                            .setNegativeButtonText("Cancel")
                            .build();

            biometricPrompt.authenticate(promptInfo);
        });
    }
}
