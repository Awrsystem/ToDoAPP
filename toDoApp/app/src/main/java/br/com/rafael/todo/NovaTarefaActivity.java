package br.com.rafael.todo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

import br.com.rafael.todo.entity.Tarefa;
import br.com.rafael.todo.repository.RepositorioDeTarefas;


public class NovaTarefaActivity extends Activity {

    public int year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_tarefa);

        ((Button) findViewById(R.id.btnSalvar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insereTarefa();

                setResult(RESULT_OK);

                finish();
            }
        });
    }

    public void insereTarefa() {

        final Tarefa tarefa = new Tarefa();

        tarefa.titulo = ((EditText) findViewById(R.id.edtTitulo)).getText().toString();
        tarefa.descricao = ((EditText) findViewById(R.id.edtDescricao)).getText().toString();

        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minute);

        tarefa.dataDoAlarme = c.getTimeInMillis();

        new Thread(new Runnable() {
            @Override
            public void run() {
                RepositorioDeTarefas repositorio = new RepositorioDeTarefas(NovaTarefaActivity.this);
                Log.i("salvando", tarefa.descricao);
                tarefa.id = repositorio.salvarTarefa(tarefa);
                repositorio.fechar();

                /*
                 * Agendar alarme...
                 */
                Intent it = new Intent("HORA_DA_TAREFA");
                it.putExtra("tarefa", tarefa);

                PendingIntent pIt = PendingIntent.getBroadcast(NovaTarefaActivity.this, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, tarefa.dataDoAlarme, pIt);
            }
        }).start();
    }
}