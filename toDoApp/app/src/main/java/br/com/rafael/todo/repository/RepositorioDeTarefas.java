package br.com.rafael.todo.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.rafael.todo.entity.Tarefa;
import br.com.rafael.todo.helper.SQLiteHelper;
import br.com.rafael.todo.infra.log.LogConstants;
import br.com.rafael.todo.sql.TarefaSQL;
import br.com.rafael.todo.sql.TarefaSQL.Tarefas;

/**
 * Created by Rafael on 27/07/2015.
 */
public class RepositorioDeTarefas {

    private SQLiteHelper dbHelper;

    private SQLiteDatabase db;

    public RepositorioDeTarefas(Context ctx) {
        dbHelper = new SQLiteHelper(ctx);

        db = dbHelper.getWritableDatabase();
    }

    public List<Tarefa> buscarTodasAsTarefas() {
        Cursor c = getCursor();

        List<Tarefa> tarefas = new ArrayList<>();

        if (c.moveToFirst()) {
            int idxId = c.getColumnIndex(Tarefas._ID);
            int idxTitulo = c.getColumnIndex(Tarefas.TITULO);
            int idxDescricao = c.getColumnIndex(Tarefas.DESCRICAO);
            int idxDataAlarme = c.getColumnIndex(Tarefas.DATA_DO_ALARME);

            do {
                Tarefa tarefa = new Tarefa();
                tarefas.add(tarefa);

                tarefa.id = c.getLong(idxId);
                tarefa.titulo = c.getString(idxTitulo);
                tarefa.descricao = c.getString(idxDescricao);
                tarefa.dataDoAlarme = c.getLong(idxDataAlarme);

            } while (c.moveToNext());
        }

        return tarefas;
    }

    private Cursor getCursor() {
        try {
            return db.query(TarefaSQL.NOME_TABELA, TarefaSQL.COLUNAS, null, null, null, null, null, null);
        } catch (SQLException e) {
            Log.e(LogConstants.LOG_TAG, "Erro ao buscar os carros: " + e.toString());
            return null;
        }
    }

    public void excluirTarefa(long id) {
        String where = Tarefas._ID + "=?";

        String _id = String.valueOf(id);

        String[] whereArgs = new String[] { _id };

        db.delete(TarefaSQL.NOME_TABELA, where, whereArgs);
    }

    public long salvarTarefa(Tarefa tarefa) {
        long id = 0;

        if (tarefa.id == 0) {
            id = inserir(tarefa);
            return id;
        }

        atualizar(tarefa);

        return tarefa.id;
    }

    private long inserir(Tarefa tarefa) {
        ContentValues values = getDefaultContentValues(tarefa);
        return inserir(values);
    }

    private long inserir(ContentValues valores) {
        return db.insert(TarefaSQL.NOME_TABELA, "", valores);
    }

    private int atualizar(Tarefa tarefa) {
        ContentValues values = getDefaultContentValues(tarefa);

        String _id = String.valueOf(tarefa.id);

        String where = Tarefas._ID + "=?";
        String[] whereArgs = new String[] { _id };

        int count = atualizar(values, where, whereArgs);

        return count;
    }

    private ContentValues getDefaultContentValues(Tarefa tarefa) {
        ContentValues values = new ContentValues();

        values.put(Tarefas.TITULO, tarefa.titulo);
        values.put(Tarefas.DESCRICAO, tarefa.descricao);
        values.put(Tarefas.DATA_DO_ALARME, tarefa.dataDoAlarme);

        return values;
    }

    private int atualizar(ContentValues valores, String where, String[] whereArgs) {
        int count = db.update(TarefaSQL.NOME_TABELA, valores, where, whereArgs);
        Log.i(LogConstants.LOG_TAG, "Atualizou [" + count + "] registros");
        return count;
    }

    public void fechar() {
        if (db != null) {
            db.close();
        }

        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}