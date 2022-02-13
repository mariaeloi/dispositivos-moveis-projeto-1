package br.com.ufrn.imd.dispositivos.todolist.service;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ufrn.imd.dispositivos.todolist.model.TodoItem;
import br.com.ufrn.imd.dispositivos.todolist.utils.NotificationHelper;

public class NotificationLoginService extends IntentService {
    private static final SimpleDateFormat FORMATADOR_DATA = new SimpleDateFormat("dd/MM/yyyy");

    public static final String TASKS = "tasks";

    private static final String GROUP_KEY = "LOGIN_NOTIFICATIONS";

    public static final String CHANNEL_ID = "5453";

    public NotificationLoginService() {
        super("NotificationLoginService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(CHANNEL_ID, GROUP_KEY, this,
                "Channel for login notifications.", 3);
        List<TodoItem> tarefasUsuarioLogado = (ArrayList<TodoItem>) intent.getExtras()
                .getSerializable(TASKS);

        try {
            // não leva em consideração o horário
            Date dataAtual = FORMATADOR_DATA.parse( FORMATADOR_DATA.format(new Date()) );

            List<Notification> notifications = new ArrayList<>();
            for (TodoItem tarefa : tarefasUsuarioLogado) {
                // Se o prazo da tarefa for a data atual, cria uma notificação relacionada
                if (tarefa.getDeadLine().equals(dataAtual)) {
                    notifications.add(notificationHelper.newExpiringTaskNotification(tarefa, 0));
                }
            }
            notificationHelper.createExpiringTaskNotificationsGroup(notifications);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
