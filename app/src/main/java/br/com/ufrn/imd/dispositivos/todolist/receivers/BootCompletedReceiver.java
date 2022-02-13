package br.com.ufrn.imd.dispositivos.todolist.receivers;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import br.com.ufrn.imd.dispositivos.todolist.dao.UsuarioDAO;

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        UsuarioDAO usuarioDAO = new UsuarioDAO(context);
        if (usuarioDAO.usuariosLogados() >= 1 &&
                intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Define alarme de notificação agendada novamente após reiniciar o dispositivo
            Intent intentAlarme = new Intent(context, NotificationScheduledReciever.class);
            // FLAG_UPDATE_CURRENT indica que a intenção pendente criada pode ser atualizada no futuro
            PendingIntent pendingIntentAlarme = PendingIntent
                    .getBroadcast( context, 0, intentAlarme, PendingIntent.FLAG_UPDATE_CURRENT );

            // Define o alarme para iniciar aproximadamente às 8:00 da manhã
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis( System.currentTimeMillis() );
            calendar.set(Calendar.HOUR_OF_DAY, 8);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

            // AlarmManager.RTC_WAKEUP garante que o alarme será acionado mesmo que o dispositivo entre no modo de suspensão
            // AlarmManager.INTERVAL_DAY indica que a periocidade é diária
            // O alarme será acionado todos os dias no horário definido em `calender`
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntentAlarme);

            // Com intervalo de 1 minuto para teste
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                    1000 * 60, pendingIntentAlarme);
            Log.i("INFO NOTI", "Alarme de notificação agendada criado após reiniciar dispositivo");
        }
    }
}
