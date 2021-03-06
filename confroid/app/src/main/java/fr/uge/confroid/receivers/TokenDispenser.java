package fr.uge.confroid.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import fr.uge.confroid.utlis.ConfroidManagerUtils;

public class TokenDispenser extends BroadcastReceiver {

    private static final int TOKEN_LENGTH = 20;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String getToken(String receiver) {
        String token = "";
        int count = TOKEN_LENGTH;
        /* not truly random */
        Integer randomNumber = receiver.hashCode();
        while (count-- > 0) {
            randomNumber = Math.abs((randomNumber + receiver).hashCode() % CHARACTERS.length());
            token += CHARACTERS.charAt(randomNumber);
        }
        Log.e("TOKEN", receiver + " " + token);
        return token;
    }

    @Override
    public void onReceive(Context context, Intent incomingIntent) {
        String receiver = incomingIntent.getStringExtra("receiver");
        String packageName = ConfroidManagerUtils.getPackageName(receiver);
        Intent outgoingIntent = new Intent();
        outgoingIntent.setClassName(packageName, receiver);
        outgoingIntent.putExtra("token", getToken(packageName));
        context.startService(outgoingIntent);
    }

}
