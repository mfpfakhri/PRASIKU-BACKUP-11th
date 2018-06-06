package clouwiko.dev.prasiku.activity.other;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.File;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.activity.AppAcceptedReviewActivity;

/**
 * Created by muham on 06/06/2018.
 */

public class NotificationGenerator {

    private static final int NOTIFICATION_ID_OPEN_FOLDER = 9;

    public static void openPdfNotification(Context context) {
        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(context);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + "Surat Perjanjian Adopsi SIKUCING.pdf");
        Uri path = Uri.fromFile(pdfFile);
        Intent intentOpenFolder = new Intent(Intent.ACTION_VIEW);
        intentOpenFolder.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentOpenFolder.setDataAndType(path, "application/pdf");

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentOpenFolder, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            notificationCompat.setContentIntent(pendingIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Tidak ada Aplikasi yang Dapat Membuka PDF, Periksa Direktori Unduhan", Toast.LENGTH_SHORT).show();
        }

        notificationCompat.setSmallIcon(R.drawable.logo_01);
        notificationCompat.setAutoCancel(true);
        notificationCompat.setContentTitle("Surat Perjanjian Adopsi SIKUCING");
        notificationCompat.setContentText("Tekan Untuk Membuka Surat Perjanjian");

        notificationManager.notify(NOTIFICATION_ID_OPEN_FOLDER, notificationCompat.build());
    }
}
