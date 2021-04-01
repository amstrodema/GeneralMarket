package comgalaxyglotech.confirmexperts.generalmarket.BL.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import comgalaxyglotech.confirmexperts.generalmarket.Controller.Notification.NotificationView;
import comgalaxyglotech.confirmexperts.generalmarket.R;

/**
 * Created by ELECTRON on 05/12/2020.
 */

public class Notify {
    Context context;
    public void notifyAlert(Context dacontext, String title, String content, int id){
        context = dacontext;
        createNotificationChannel();
        Intent intent =  new Intent(context, NotificationView.class);
        // startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent =PendingIntent.getActivity(context,0,intent,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "123")
                .setSmallIcon(R.drawable.face)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
      /*  Random random = new Random();
        int id = random.nextInt();*/
        notificationManager.notify(id, builder.build());
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channelName);
            String description = context.getString(R.string.channelDescription);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("123", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
