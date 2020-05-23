package br.com.rafael.todo.adpter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.rafael.todo.MainActivity;
import br.com.rafael.todo.NovaTarefaActivity;
import br.com.rafael.todo.R;
import br.com.rafael.todo.entity.Tarefa;
import br.com.rafael.todo.repository.RepositorioDeTarefas;

/**
 * Created by Rafael on 27/07/2015.
 */
public class TarefaAdapter extends BaseAdapter {

    Context context;
    List<Tarefa> tarefas;

    public TarefaAdapter(Context context, List<Tarefa> myList){
        this.context = context;
        this.tarefas = myList;
    }

    @Override
    public int getCount() {
        return this.tarefas.size();
    }

    @Override
    public Object getItem(int position) {
        return this.tarefas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.tarefas.get(position).id;
    }

    //https://github.com/ricardolonga/todo/blob/master/app/src/main/java/br/com/ricardolonga/todo/adapter/TarefaListAdapter.java

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Recupera o Carro da posição atual
        final Tarefa tarefa = tarefas.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.template_tarefa, null);

        // Atualiza o valor do TextView
        TextView titulo = (TextView) view.findViewById(R.id.titulo);
        titulo.setText(tarefa.titulo);

        TextView descricao = (TextView) view.findViewById(R.id.descricao);
        descricao.setText(tarefa.descricao);

        TextView dataalarme = (TextView) view.findViewById(R.id.dataalarme);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(tarefa.dataDoAlarme);
        dataalarme.setText(pad(c.get(Calendar.DAY_OF_MONTH)) + "/" +
                pad(c.get(Calendar.MONTH) + 1) + "/" +
                c.get(Calendar.YEAR) + " - " +
                pad(c.get(Calendar.HOUR_OF_DAY)) + ":" +
                pad(c.get(Calendar.MINUTE)));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RepositorioDeTarefas repositorio = new RepositorioDeTarefas(context);
                        repositorio.excluirTarefa(tarefa.id);
                        repositorio.fechar();

                        ((MainActivity) context).atualizarLista();

                        /*
                         * Removendo alarme...
                         */
                        Intent it = new Intent("HORA_DA_TAREFA");
                        it.putExtra("tarefa", tarefa);
                        PendingIntent pIt = PendingIntent.getBroadcast(context, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        alarmManager.cancel(pIt);
                    }
                }).start();
            }
        });
        return view;
    }
    private static String pad(int c) {
        if (c >= 10) {
            return String.valueOf(c);
        } else {
            return "0" + String.valueOf(c);
        }
    }
}