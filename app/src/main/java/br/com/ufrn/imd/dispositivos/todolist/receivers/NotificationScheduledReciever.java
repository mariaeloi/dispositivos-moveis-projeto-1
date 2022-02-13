package br.com.ufrn.imd.dispositivos.todolist.receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import br.com.ufrn.imd.dispositivos.todolist.dao.TodoItemDAO;
import br.com.ufrn.imd.dispositivos.todolist.dao.UsuarioDAO;
import br.com.ufrn.imd.dispositivos.todolist.model.TodoItem;
import br.com.ufrn.imd.dispositivos.todolist.model.Usuario;
import br.com.ufrn.imd.dispositivos.todolist.utils.NotificationHelper;

public class NotificationScheduledReciever extends BroadcastReceiver {
    private static final SimpleDateFormat FORMATADOR_DATA = new SimpleDateFormat("dd/MM/yyyy");

    private static final int MILISSEGUNDOS_DIA =  1000 * 60 * 60 * 24;

    private static final String GROUP_KEY = "SCHEDULED_NOTIFICATIONS";

    public static final String CHANNEL_ID = "3545";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("INFO NOTI", "Tentando realizar notificação agendada");

        UsuarioDAO usuarioDAO = new UsuarioDAO(context);
        if(usuarioDAO.usuariosLogados() == 1) {
            NotificationHelper notificationHelper = new NotificationHelper(CHANNEL_ID, GROUP_KEY, context,
                    "Channel for scheduled notifications.", 4);

            Usuario usuarioLogado = usuarioDAO.getUsuarioLogado();
            TodoItemDAO todoItemDAO = new TodoItemDAO(context);
            List<TodoItem> tarefasUsuarioLogado = todoItemDAO.load(usuarioLogado.getId());

            try {
                // não leva em consideração o horário
                long dataAtual = Objects.requireNonNull(FORMATADOR_DATA.parse(FORMATADOR_DATA
                        .format(new Date()))).getTime();

                List<Notification> notifications = new ArrayList<>();
                for (TodoItem tarefa : tarefasUsuarioLogado) {
                    long dataTarefa = tarefa.getDeadLine().getTime();
                    int diasRestantes = (int) ((dataTarefa - dataAtual) / MILISSEGUNDOS_DIA);

                    if (diasRestantes >= 0 && diasRestantes <= 5)
                        notifications.add(notificationHelper.newExpiringTaskNotification(tarefa, diasRestantes));
                }
                notificationHelper.createExpiringTaskNotificationsGroup(notifications);
                Log.i("INFO NOTI", "Criação da notificação finalizada");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.e("INFO NOTI", "Erro ao tentar realizar notificação agendada: nenhum usuário logado");

            // Cancelar alarme se não houver nenhum usuário logado
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(context, 0, intent,
                            PendingIntent.FLAG_NO_CREATE);
            if (pendingIntent != null && alarmManager != null) {
                alarmManager.cancel(pendingIntent);
                Log.i("INFO NOTI", "Alarme de notificação agendada cancelado");
            }
        }
    }
}
