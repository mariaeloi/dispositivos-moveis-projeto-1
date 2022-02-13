package br.com.ufrn.imd.dispositivos.todolist.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;

import br.com.ufrn.imd.dispositivos.todolist.R;
import br.com.ufrn.imd.dispositivos.todolist.model.TodoItem;

public class NotificationHelper {
    private final Context context;

    private final String CHANNEL_ID;

    private final String GROUP_KEY;

    public NotificationHelper(String channelID, String groupKey, Context context,
                              String description, int importance) {
        this.context = context;
        this.CHANNEL_ID = channelID;
        this.GROUP_KEY = groupKey;

        this.createNotificationChannel(description, importance);
    }

    private void createNotificationChannel(String description, int importance) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, GROUP_KEY, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public Notification newExpiringTaskNotification(TodoItem item, int daysRemaning) {
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentTitle(item.getTitle())
                .setContentText("O prazo da tarefa \"" + item.getTitle() + "\" encerra "
                        + this.daysRemaningToString(daysRemaning))
                .setGroup(GROUP_KEY)
                .build();
    }

    private String daysRemaningToString(int daysRemaning) {
        String text;
        switch (daysRemaning) {
            case 0:
                text = "hoje";
                break;
            case 1:
                text = "amanhã";
                break;
            default:
                text = "em " + daysRemaning + " dias";
        }

        return text;
    }

    public void createExpiringTaskNotificationsGroup(List<Notification> notifications) {
        if(!notifications.isEmpty()) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancelAll();

            Notification summaryNotification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.sym_def_app_icon)
                    .setContentTitle(String.valueOf(R.string.app_name))
                    .setStyle(new NotificationCompat.InboxStyle()
                            .setSummaryText(notifications.size() + " tarefas encerrando"))
                    .setGroup(GROUP_KEY)
                    .setGroupSummary(true)
                    .build();

            for (int i = 1; i <= notifications.size(); i++)
                notificationManager.notify(i, notifications.get(i - 1));
            notificationManager.notify(0, summaryNotification);
        }
    }
}
