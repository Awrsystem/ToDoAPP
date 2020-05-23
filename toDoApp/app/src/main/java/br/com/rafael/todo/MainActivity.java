package br.com.rafael.todo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.List;

import br.com.rafael.todo.adpter.TarefaAdapter;
import br.com.rafael.todo.entity.Tarefa;
import br.com.rafael.todo.repository.RepositorioDeTarefas;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    private List<Tarefa> tarefas;
    private ListView listagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //listagem = (ListView) findViewById(R.id.listaDeTarefas);
    }

    @AfterViews
    public void carregaDados(){
        listagem = (ListView) findViewById(R.id.listaDeTarefas);
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarLista();

        registerReceiver(mBroadcastReceiver, new IntentFilter("ATUALIZAR_LISTAGEM"));
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            atualizarLista();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Nova Tarefa").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, NovaTarefaActivity.class));
                return false;
            }
        });
        return true;
    }

    public void atualizarLista() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RepositorioDeTarefas repositorio = new RepositorioDeTarefas(MainActivity.this);
                tarefas = repositorio.buscarTodasAsTarefas();
                repositorio.fechar();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listagem.setAdapter(new TarefaAdapter(MainActivity.this, tarefas));
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            atualizarLista();
        }
    }
}