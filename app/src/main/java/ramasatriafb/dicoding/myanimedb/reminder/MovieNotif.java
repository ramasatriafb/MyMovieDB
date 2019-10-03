package ramasatriafb.dicoding.myanimedb.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.List;

import ramasatriafb.dicoding.myanimedb.R;
import ramasatriafb.dicoding.myanimedb.data.ResultMovie;

public class MovieNotif extends BroadcastReceiver {
    private static final int NOTIF_ID_REPEATING = 101;
    private static int notifId;

    public MovieNotif(){
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        notifId = intent.getIntExtra("id", 0);
        String title = intent.getStringExtra("movieTitle");
        String message = context.getString(R.string.notifTodayMovie);
        String app = context.getString(R.string.app_name);

        showAlarmNotification(context, title, notifId, message, app);
        Log.d("TES", "Alarm film");
    }

    private void showAlarmNotification(Context context, String title, int notifId, String message, String app) {
        String CHANNEL_ID = "Channel_2";
        String CHANNEL_NAME = "Movie Release channel";

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentTitle(app)
                .setContentText(message+" " + title + " "+R.string.release)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, notification);
        }
    }


    public void setRepeatingAlarm(Context context, List<ResultMovie> resultMovieList) {

        int notifDelay = 0;

        for (int i = 0; i < resultMovieList.size(); i++) {
            cancelAlarm(context);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(context, MovieNotif.class);
            intent.putExtra("movieTitle", resultMovieList.get(i).getTitle());
            intent.putExtra("id", notifId);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_REPEATING, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 00);
            calendar.set(Calendar.SECOND, 00);

            if (alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + notifDelay,  AlarmManager.INTERVAL_DAY,pendingIntent);
            }

            notifId ++;
            notifDelay += 1000;
        }

        Toast.makeText(context,R.string.setUpNotif, Toast.LENGTH_SHORT).show();

    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(getPendingIntent(context));
        }
        Toast.makeText(context, R.string.cancelNotif, Toast.LENGTH_SHORT).show();
    }

    private static PendingIntent getPendingIntent(Context context) {

        Intent intent = new Intent(context, MovieNotif.class);
        return PendingIntent.getBroadcast(context,NOTIF_ID_REPEATING, intent, PendingIntent.FLAG_CANCEL_CURRENT);

    }

}
