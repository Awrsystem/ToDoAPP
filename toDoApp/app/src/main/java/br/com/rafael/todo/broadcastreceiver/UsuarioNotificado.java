package br.com.rafael.todo.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import br.com.gilberto.todo.entity.Tarefa;
import br.com.gilberto.todo.repository.RepositorioDeTarefas;

public class UsuarioNotificado extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		final Tarefa tarefa = (Tarefa) intent.getExtras().get("tarefa");
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				RepositorioDeTarefas repositorio = new RepositorioDeTarefas(context);
				repositorio.excluirTarefa(tarefa.id);
				repositorio.fechar();

				context.sendBroadcast(new Intent("ATUALIZAR_LISTAGEM"));
			}
		}).start();
	}

}
