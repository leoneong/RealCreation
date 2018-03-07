package com.example.realcreation;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.CognitoCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentityprovider.model.AttributeType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AppHelper {
    private static final String TAG = "AppHelper";
    // App settings

    private static List<String> attributeDisplaySeq;
    private static Map<String, String> signUpFieldsC2O;
    private static Map<String, String> signUpFieldsO2C;

    private static AppHelper appHelper;
    private static CognitoUserPool userPool;
    private static String user;
    private static CognitoDevice newDevice;

    private static CognitoUserAttributes attributesChanged;
    private static List<AttributeType> attributesToDelete;

    private static  int itemCount;

    private static int trustedDevicesCount;
    private static List<CognitoDevice> deviceDetails;
    private static CognitoDevice thisDevice;
    private static boolean thisDeviceTrustState;

    private static Map<String, String> firstTimeLogInUserAttributes;
    private static List<String> firstTimeLogInRequiredAttributes;
    private static int firstTimeLogInItemsCount;
    private static Map<String, String> firstTimeLogInUpDatedAttributes;
    private static String firstTimeLoginNewPassword;

    private static List<String> mfaAllOptionsCode;

    // Change the next three lines of code to run this demo on your user pool

    private static final String userPoolId = "us-east-1_zSLbuwVxl";
    private static final String clientId = "u10vh7arll97hgp8qjn5vc70g";
    private static final String clientSecret = "1l8bdamfargi2j6q4i2eo5q6rsp28go62bisn0srlql0oot56dj";

    /**
     * Set Your User Pools region.
     * e.g. if your user pools are in US East (N Virginia) then set cognitoRegion = Regions.US_EAST_1.
     */
    private static final Regions cognitoRegion = Regions.US_EAST_1;

    // User details from the service
    private static CognitoUserSession currSession;
    private static CognitoUserDetails userDetails;

    // User details to display - they are the current values, including any local modification


    private static Set<String> currUserAttributes;

    private static CognitoCredentialsProvider cognitoCredentialsProvider;

    public static void init(Context context) {

        if (appHelper != null && userPool != null) {
            return;
        }

        if (appHelper == null) {
            appHelper = new AppHelper();
        }

        if (userPool == null) {
            userPool = new CognitoUserPool(context, userPoolId, clientId, clientSecret, cognitoRegion);
        }

        firstTimeLogInUpDatedAttributes= new HashMap<String, String>();

        newDevice = null;
        thisDevice = null;
        thisDeviceTrustState = false;

    }

    public static CognitoUserPool getPool() {
        return userPool;
    }

    public static void setUser(String user) {
        AppHelper.user = user;
    }

    public static String getUser() {
        return AppHelper.user;
    }

    public static CognitoUserSession getCurrSession() {
        return currSession;
    }

    public static void setCurrSession(CognitoUserSession currSession) {
        AppHelper.currSession = currSession;
    }
    public static void newDevice(CognitoDevice device) {
        newDevice = device;
    }

    public static String formatException(Exception exception) {
        String formattedString = "Internal Error";
        Log.e(TAG, " -- Error: "+exception.toString());
        Log.getStackTraceString(exception);

        String temp = exception.getMessage();

        if(temp != null && temp.length() > 0) {
            formattedString = temp.split("\\(")[0];
            if(temp != null && temp.length() > 0) {
                return formattedString;
            }
        }

        return  formattedString;
    }


    public static CognitoCredentialsProvider getCognitoCredentialsProvider() {
        return cognitoCredentialsProvider;
    }

    public static void setCognitoCredentialsProvider(CognitoCredentialsProvider cognitoCredentialsProvider) {
        AppHelper.cognitoCredentialsProvider = cognitoCredentialsProvider;
    }
}
