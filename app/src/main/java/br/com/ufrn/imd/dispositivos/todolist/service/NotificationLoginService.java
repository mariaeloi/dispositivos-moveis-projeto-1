package br.com.ufrn.imd.dispositivos.todolist.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ufrn.imd.dispositivos.todolist.R;
import br.com.ufrn.imd.dispositivos.todolist.model.TodoItem;

public class NotificationLoginService extends IntentService {
    private static final SimpleDateFormat FORMATADOR_DATA = new SimpleDateFormat("dd/MM/yyyy");

    public static final String TASKS = "tasks";

    String GROUP_KEY_EXPIRING_TASKS = "EXPIRING_TASKS";

    public static final String CHANNEL_ID = "5453";

    public NotificationLoginService() {
        super("NotificationLoginService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        List<TodoItem> tarefasUsuarioLogado = (ArrayList<TodoItem>) intent.getExtras()
                .getSerializable(TASKS);

        Date dataAtual = null;
        try {
            // não leva em consideração o horário
            dataAtual = FORMATADOR_DATA.parse( FORMATADOR_DATA.format(new Date()) );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.createNotificationChannel();
        List<Notification> notifications = new ArrayList<>();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        for (TodoItem tarefa : tarefasUsuarioLogado) {
            // Se o prazo da tarefa for a data atual, cria uma notificação relacionada
            if (tarefa.getDeadLine().equals(dataAtual)) {
                notifications.add(newNotificationExpiringTask(tarefa));
            }
        }

        if(!notifications.isEmpty()) {
            Notification summaryNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.sym_def_app_icon)
                    .setContentTitle(String.valueOf(R.string.app_name))
                    .setStyle(new NotificationCompat.InboxStyle()
                            .setSummaryText(notifications.size() + " tarefas encerrando"))
                    .setGroup(GROUP_KEY_EXPIRING_TASKS)
                    .setGroupSummary(true)
                    .build();

            for (int i = 1; i <= notifications.size(); i++)
                notificationManager.notify(i, notifications.get(i - 1));
            notificationManager.notify(0, summaryNotification);
        }
    }

    private Notification newNotificationExpiringTask(TodoItem item) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentTitle(item.getTitle())
                .setContentText("O prazo da tarefa \"" + item.getTitle() + "\" encerra hoje")
                .setGroup(GROUP_KEY_EXPIRING_TASKS)
                .build();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notifications_login_channel";
            String description = "Channel for login notifications.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
