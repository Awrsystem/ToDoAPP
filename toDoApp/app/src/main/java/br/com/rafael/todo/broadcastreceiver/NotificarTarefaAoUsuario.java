package br.com.rafael.todo.broadcastreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import br.com.rafael.todo.R;
import br.com.rafael.todo.entity.Tarefa;

public class NotificarTarefaAoUsuario extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Tarefa tarefa = (Tarefa) intent.getExtras().get("tarefa");
		
		Intent it = new Intent("USUARIO_NOTIFICADO");
		it.putExtra("tarefa", tarefa);
		
		PendingIntent pIt = PendingIntent.getBroadcast(context, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		
		Notification n = new Notification.Builder(context)
										 .setTicker("Nova tarefa!")
										 .setContentTitle(tarefa.titulo)
										 .setSmallIcon(R.drawable.ic_launcher)
										 .setContentText(tarefa.descricao)
										 .setContentIntent(pIt)
										 .setWhen(tarefa.dataDoAlarme)
										 .setAutoCancel(true)
										 .build();
		
		notificationManager.notify(1, n);
	}
}