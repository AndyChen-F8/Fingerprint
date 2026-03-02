var exec = require('cordova/exec');

var PluginFingerprint = {

    authenticate: function (options, success, error) {
        exec(success, error, "PluginFingerprint", "authenticate", [options]);
    },

    isAvailable: function (success, error) {
        exec(success, error, "PluginFingerprint", "isAvailable", []);
    }

};

window.Fingerprint = PluginFingerprint;
