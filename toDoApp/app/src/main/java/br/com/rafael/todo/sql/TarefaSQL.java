package br.com.rafael.todo.sql;

import android.provider.BaseColumns;

public class TarefaSQL {

	public static final String NOME_TABELA = "tarefa";

	public static final String[] COLUNAS = new String[] { Tarefas._ID, Tarefas.TITULO, Tarefas.DESCRICAO, Tarefas.DATA_DO_ALARME };

	public static final String SCRIPT_CREATE = "CREATE TABLE " + NOME_TABELA + " ( " + Tarefas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Tarefas.TITULO + " TEXT NOT NULL, " + Tarefas.DESCRICAO + " TEXT NOT NULL, " + Tarefas.DATA_DO_ALARME + " INTEGER NOT NULL);";
	public static final String SCRIPT_DELETE = "DROP TABLE IF EXISTS " + NOME_TABELA;

	/**
	 * Classe interna para representar as colunas.
	 * Filha de BaseColumns que já define (_id e _count), para seguir o padrão Android.
	 */
	public static final class Tarefas implements BaseColumns {
		private Tarefas() {}
	
		public static final String DEFAULT_SORT_ORDER = "_id ASC";
	
		public static final String TITULO = "titulo";
		public static final String DESCRICAO = "descricao";
		public static final String DATA_DO_ALARME = "dataalarme";
	}

}
