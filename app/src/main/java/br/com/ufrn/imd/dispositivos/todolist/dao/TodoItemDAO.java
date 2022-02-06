package br.com.ufrn.imd.dispositivos.todolist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ufrn.imd.dispositivos.todolist.model.TodoItem;
import br.com.ufrn.imd.dispositivos.todolist.utils.DBHelper;

public class TodoItemDAO {

    private final SQLiteDatabase escreve;
    private final SQLiteDatabase le;


    public TodoItemDAO(Context context){
        DBHelper dbHelper = new DBHelper(context);
        escreve = dbHelper.getWritableDatabase();
        le = dbHelper.getReadableDatabase();
    }

    public boolean create (TodoItem todoItem) {

        ContentValues cv = new ContentValues();
        cv.put("title",todoItem.getTitle());
        cv.put("description",todoItem.getDescription());
        cv.put("deadline",todoItem.getDeadLine().toString());

        try {
            escreve.insert(DBHelper.TABELA_TODO,null,cv);
            Log.i("alerta","Item salvo com sucesso");
        }catch (Exception e){
            Log.i("alerta","Erro ao salvar item da lista");
            return false;
        }

        return true;
    }
    public  boolean atualizar(TodoItem todoItem){
        ContentValues cv = new ContentValues();
        cv.put("title",todoItem.getTitle());
        cv.put("description",todoItem.getDescription());
        cv.put("deadline",todoItem.getDeadLine().toString());

        try{
            String[] args = {todoItem.getId().toString()};

            escreve.update(DBHelper.TABELA_TODO,cv,"id=?",args);
            Log.i("INFO","Registro atualizado com sucesso!");
        }catch (Exception e){
            Log.i("INFO","Erro ao atualizar registro!" + e.getMessage());
            return false;
        }
        return  true;
    }

    public boolean delete(TodoItem todoItem){
        try{
            String [] args ={todoItem.getId().toString()};

            escreve.delete(DBHelper.TABELA_TODO,"id=?",args);
            Log.i("INFO","item deletado com sucesso!");

        }catch (Exception e){
            Log.i("INFO","Erro ao Deletar item!" + e.getMessage());
            return false;
        }
        return  true;
    }

    public List<TodoItem> carregar(){
        List<TodoItem> todoItemList = new ArrayList<>();
        String sql = "SELECT * FROM "+DBHelper.TABELA_TODO;
        Cursor c = le.rawQuery(sql,null);
        c.moveToFirst();

        try {
        TodoItem todoItem = new TodoItem();

        todoItem.setId(c.getColumnIndexOrThrow("id"));
        todoItem.setTitle(c.getString(c.getColumnIndexOrThrow("title")));
        todoItem.setDeadLine( c.getString(c.getColumnIndexOrThrow("deadline")));
        todoItem.setDescription(c.getString(c.getColumnIndexOrThrow("descricao")));

        todoItemList.add(todoItem);

        c.moveToNext();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return  todoItemList;
    }




}
