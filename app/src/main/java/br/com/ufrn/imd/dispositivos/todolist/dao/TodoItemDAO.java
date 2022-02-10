package br.com.ufrn.imd.dispositivos.todolist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.ufrn.imd.dispositivos.todolist.model.TodoItem;
import br.com.ufrn.imd.dispositivos.todolist.utils.DBHelper;

public class TodoItemDAO {

    private final SQLiteDatabase escreve;
    private final SQLiteDatabase le;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");


    public TodoItemDAO(Context context){
        DBHelper dbHelper = new DBHelper(context);
        escreve = dbHelper.getWritableDatabase();
        le = dbHelper.getReadableDatabase();
    }

    public boolean create (TodoItem todoItem) {

        ContentValues cv = new ContentValues();
        cv.put("id",todoItem.getId());
        cv.put("title",todoItem.getTitle());
        cv.put("description",todoItem.getDescription());
        cv.put("deadline",new SimpleDateFormat("dd/MM/yyyy").format(todoItem.getDeadLine()));
        try {
            escreve.insert(DBHelper.TABELA_TODO,null,cv);
            Log.i("alerta","Item salvo com sucesso");
        }catch (Exception e){
            Log.i("alerta","Erro ao salvar item da lista");
            return false;
        }

        return true;
    }
    public  boolean update(TodoItem todoItem){
        ContentValues cv = new ContentValues();
        cv.put("id",todoItem.getId());
        cv.put("title",todoItem.getTitle());
        cv.put("description",todoItem.getDescription());
        cv.put("deadline",new SimpleDateFormat("dd/MM/yyyy").format(todoItem.getDeadLine()));

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

    public List<TodoItem> load(){
        List<TodoItem> todoItemList = new ArrayList<>();
        String sql = "SELECT * FROM "+DBHelper.TABELA_TODO;
        Cursor c = le.rawQuery(sql,null);

       if(c.moveToFirst()) {

          do{


           try {
               TodoItem todoItem = new TodoItem();

               todoItem.setId(c.getInt(c.getColumnIndexOrThrow("id")));
               todoItem.setTitle(c.getString(c.getColumnIndexOrThrow("title")));

               todoItem.setDeadLine(formatter.parse( c.getString(c.getColumnIndexOrThrow("deadline"))) );


               todoItem.setDescription(c.getString(c.getColumnIndexOrThrow("description")));
               todoItemList.add(todoItem);


           } catch (Exception e) {
               e.printStackTrace();
           }} while ( c.moveToNext());
       }

        return  todoItemList;
    }




}
