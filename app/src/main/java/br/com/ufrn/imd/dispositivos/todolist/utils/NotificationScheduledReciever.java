package br.com.ufrn.imd.dispositivos.todolist.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ufrn.imd.dispositivos.todolist.R;
import br.com.ufrn.imd.dispositivos.todolist.model.TodoItem;

public class NotificationScheduledReciever extends BroadcastReceiver {
    private static final SimpleDateFormat FORMATADOR_DATA = new SimpleDateFormat("dd/MM/yyyy");
    private static final int MILISSEGUNDOS_DIA =  1000 * 60 * 60 * 24;

    String GROUP_KEY_EXPIRING_TASKS = "EXPIRING_TASKS_SCHEDULED";

    public static final String CHANNEL_ID = "3545";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO buscar tarefas no banco de dados
        List<TodoItem> tarefasParaNotificar = new ArrayList<>();

        long dataAtual = System.currentTimeMillis();
        // Dados para teste
        for(int i=0; i<5; i++) {
            tarefasParaNotificar.add(new TodoItem(i, "Tarefa " + i, "Descrição", new Date()));
            //

            long dataTarefa = tarefasParaNotificar.get(i).getDeadLine().getTime();
            long diasAtePrazo = (dataTarefa - dataAtual)/MILISSEGUNDOS_DIA;
            if(diasAtePrazo < 0 || diasAtePrazo > 5)
                tarefasParaNotificar.remove(i);
        }

        this.createNotificationsGroup(context, tarefasParaNotificar);
    }

    private void createNotificationsGroup(Context context, List<TodoItem> tasks) {
        if(!tasks.isEmpty()) {
            this.createNotificationChannel(context);

            List<Notification> notifications = new ArrayList<>();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            for (TodoItem task : tasks)
                notifications.add(this.newNotificationExpiringTask(context, task));

            Notification summaryNotification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.sym_def_app_icon)
                    .setContentTitle(String.valueOf(R.string.app_name))
                    .setStyle(new NotificationCompat.InboxStyle()
                            .setSummaryText(tasks.size() + " tarefas encerram em breve"))
                    .setGroup(GROUP_KEY_EXPIRING_TASKS)
                    .setGroupSummary(true)
                    .build();

            for (int i = 1; i <= notifications.size(); i++)
                notificationManager.notify(i, notifications.get(i - 1));
            notificationManager.notify(0, summaryNotification);
        }
    }

    private Notification newNotificationExpiringTask(Context context, TodoItem item) {
        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentTitle(item.getTitle())
                .setContentText("O prazo da tarefa \"" + item.getTitle() + "\" encerra em "
                        + FORMATADOR_DATA.format(item.getDeadLine()))
                .setGroup(GROUP_KEY_EXPIRING_TASKS)
                .build();
    }

    private void createNotificationChannel(Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notifications_scheduled_channel";
            String description = "Channel for scheduled notifications.";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
