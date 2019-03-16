package com.yourselectronics.gauridev.yourselectronics.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yourselectronics.gauridev.yourselectronics.MainActivity;
import com.yourselectronics.gauridev.yourselectronics.R;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    Bitmap image;
    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        try {
            URL url = new URL(remoteMessage.getData().get("img_url"));
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            sendNotification(remoteMessage,image);
        } catch(IOException e) {
            System.out.println(e);
        }
     /*  NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "Notification";





        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("id", "openOrder");
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);


        try {
            URL url = new URL("https://storiesflistgv2.blob.core.windows.net/stories/2017/11");
             image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            Toast.makeText(getBaseContext(),image.toString(),Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            System.out.println(e);

        }

        //Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.bc_img);
        NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(image);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setContentInfo("Your order updated")
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setContentInfo("Info")
                .setSound(defaultSoundUri)
                .setStyle(s)
                .setChannelId(CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        int randomID = new Random().nextInt(9999-1)+1;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription("Order Update");
            mChannel.enableLights(true);
            mChannel.setLightColor(ContextCompat.getColor(getApplicationContext(),R.color
                    .colorPrimary));
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(randomID,builder.build());

*/


    }

    private void sendNotification(RemoteMessage remoteMessage, Bitmap bitmap) {

      //  String notificationTitle = remoteMessage.getNotification().getTitle();
      //  String notificationMessage = remoteMessage.getNotification().getBody();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "Notification";






        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("id", "openOrder");
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(bitmap);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setContentInfo("Your order updated")
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
                .setContentInfo("Info")
                .setSound(defaultSoundUri)
                .setStyle(s)
                .setChannelId(CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        int randomID = new Random().nextInt(9999-1)+1;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription("Order Update");
            mChannel.enableLights(true);
            mChannel.setLightColor(ContextCompat.getColor(getApplicationContext(),R.color
                    .colorPrimary));
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(randomID,builder.build());
    }
}
