package br.com.rafael.todo.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.rafael.todo.sql.GeneralSQL;
import br.com.rafael.todo.sql.TarefaSQL;

/**
 * Created by Rafael on 27/07/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context) {
        super(context, GeneralSQL.NOME_BANCO, null, GeneralSQL.VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TarefaSQL.SCRIPT_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int novaVersao) {
        db.execSQL(TarefaSQL.SCRIPT_DELETE);

        onCreate(db);
    }
}