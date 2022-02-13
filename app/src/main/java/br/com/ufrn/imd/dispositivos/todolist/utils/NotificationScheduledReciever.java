package br.com.ufrn.imd.dispositivos.todolist.utils;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ufrn.imd.dispositivos.todolist.model.TodoItem;

public class NotificationScheduledReciever extends BroadcastReceiver {
    private static final int MILISSEGUNDOS_DIA =  1000 * 60 * 60 * 24;

    private static final String GROUP_KEY = "SCHEDULED_NOTIFICATIONS";

    public static final String CHANNEL_ID = "3545";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(CHANNEL_ID, GROUP_KEY, context,
                "Channel for scheduled notifications.", 4);

        // Dados para teste
        List<TodoItem> tarefasParaNotificar = new ArrayList<>();
        for(int i=0; i<5; i++)
            tarefasParaNotificar.add(new TodoItem(i, "Tarefa " + i, "Descrição", new Date()));
        //
        // TODO buscar tarefas no banco de dados
        List<TodoItem> tarefasUsuarioLogado = tarefasParaNotificar;

        long dataAtual = System.currentTimeMillis();
        List<Notification> notifications = new ArrayList<>();

        for(TodoItem tarefa : tarefasUsuarioLogado) {
            long dataTarefa = tarefa.getDeadLine().getTime();
            int diasRestantes = (int) ((dataTarefa - dataAtual)/MILISSEGUNDOS_DIA);

            if(diasRestantes >= 0 && diasRestantes <= 5)
                notifications.add(notificationHelper.newExpiringTaskNotification(tarefa, diasRestantes));
        }

        notificationHelper.createExpiringTaskNotificationsGroup(notifications);
    }
}
