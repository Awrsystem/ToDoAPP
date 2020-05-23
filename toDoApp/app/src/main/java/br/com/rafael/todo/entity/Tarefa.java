package br.com.rafael.todo.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Rafael on 27/07/2015.
 */
public class Tarefa implements Serializable {

    public Long id = 0L;
    public String titulo;
    public String descricao;
    public Long dataDoAlarme;

    public Tarefa(){
    }

    public Tarefa(Long id, String nome, Long data){
        this.id =id;
        this.descricao = nome;
        this.dataDoAlarme = data;
    }

    public Tarefa(String titulo){
        this.titulo = titulo;
    }
    public Date getDate(){
        return new Date(this.dataDoAlarme);
    }
}