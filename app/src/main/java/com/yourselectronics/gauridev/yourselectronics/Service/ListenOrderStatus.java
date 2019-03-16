package com.yourselectronics.gauridev.yourselectronics.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yourselectronics.gauridev.yourselectronics.MainActivity;
import com.yourselectronics.gauridev.yourselectronics.Model.OrderModel;
import com.yourselectronics.gauridev.yourselectronics.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ListenOrderStatus extends Service implements ChildEventListener {
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private String user_uid;
    private Query query;
    public ListenOrderStatus() {
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }



    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ref = FirebaseDatabase.getInstance().getReference().child("order_requests");
        mAuth = FirebaseAuth.getInstance();
            String user_uid = mAuth.getCurrentUser().getUid();

         query = ref.orderByChild("user_id").equalTo(user_uid);

        if (mAuth.getCurrentUser()!=null) {
            query.addChildEventListener(this);
        }

        return super.onStartCommand(intent, flags, startId);

    }

    private void showNotificationNewOrder(String key, String status) {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("user_id","abcd");
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());


       // Bitmap largeIcon = getBitmapfromUrl(orderModel.getItem_img());


        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("Yours Electronics")
                .setContentInfo("Order Placed Successfully")
                .setContentText("Order #"+key+ status)
                .setContentTitle( "Order Placed Successfully")
                .setContentIntent(pendingIntent)
                .setContentInfo("Info")
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());


    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        OrderModel orderModel = dataSnapshot.getValue(OrderModel.class);

        getStatus(dataSnapshot.getKey(),orderModel);

    }

    private void getStatus(final String key, final OrderModel orderModel) {

         ref.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status = dataSnapshot.child("order_status").getValue(String.class);
                //Toast.makeText(getBaseContext(), status,Toast.LENGTH_LONG).show();
                showNotification(key,status,orderModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void showNotification(String key, String s, OrderModel orderModel) {

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("user_id","abcd");
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());


      //  Bitmap largeIcon = getBitmapfromUrl(orderModel.getItem_img());


        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("Yours Electronics")
                .setContentInfo("Your order updated")
                .setContentTitle(orderModel.getItem_name())
                .setContentText("Order #"+key+ " is updated to " +convertCodeToStatus(s))
                .setContentIntent(pendingIntent)
                .setContentInfo("Info")
                .setSmallIcon(R.mipmap.ic_launcher);
        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());


    }



    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
    private String convertCodeToStatus(String order_status) {
        switch (order_status) {
            case "0":
                return "Placed";
            case "1":
                return "Dispatched";
            default:
                return "Delivered";
        }
    }
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

}
