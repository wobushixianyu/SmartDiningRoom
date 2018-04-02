package com.david.smartdiningroom.utils;

import android.content.pm.PackageInfo;
import android.content.pm.Signature;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class SignatureHelper {
    //we store the hash of the signture for a little more protection
    private static final String APP_SIGNATURE = "1038C0E34658923C4192E61B16846";

    /**
     * Query the signature for this application to detect whether it matches the
     * signature of the real developer. If it doesn't the app must have been
     * resigned, which indicates it may been tampered with.
     *
     * @return true if the app's signature matches the expected signature.
     */
    public static boolean validateAppSignature(PackageInfo packageInfo) throws NoSuchProviderException, NoSuchAlgorithmException {
//        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
        //note sample just checks the first signature
        for (Signature signature : packageInfo.signatures) {
            // SHA1 the signature
            String sha1 = getSHA1(signature.toByteArray());
            // check is matches hardcoded value
            return APP_SIGNATURE.equals(sha1);
        }
        return false;
    }

    // computed the sha1 hash of the signature
    public static String getSHA1(byte[] sig) throws NoSuchProviderException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA1", "BC");
        digest.update(sig);
        byte[] hashText = digest.digest();
        return bytesToHex(hashText);
    }

    // util method to convert byte array to hex string
    private static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
