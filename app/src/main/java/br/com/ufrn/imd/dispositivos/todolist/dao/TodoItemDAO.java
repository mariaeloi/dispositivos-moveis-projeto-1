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
        cv.put("title",todoItem.getTitle());
        cv.put("description",todoItem.getDescription());
        cv.put("deadline", formatter.format(todoItem.getDeadLine()));
        cv.put("id_usuario", todoItem.getIdUsuario());
        try {
            escreve.insert(DBHelper.TABELA_TODO,null,cv);
            Log.i("INFO DB","Item salvo com sucesso");
        }catch (Exception e){
            Log.i("INFO DB","Erro ao salvar item da lista");
            return false;
        }

        return true;
    }
    public  boolean update(TodoItem todoItem){
        ContentValues cv = new ContentValues();
        cv.put("title",todoItem.getTitle());
        cv.put("description",todoItem.getDescription());
        cv.put("deadline", formatter.format(todoItem.getDeadLine()));

        try{
            String[] args = {todoItem.getId().toString()};

            escreve.update(DBHelper.TABELA_TODO,cv,"id=?",args);
            Log.i("INFO DB","Registro atualizado com sucesso!");
        }catch (Exception e){
            Log.i("INFO DB","Erro ao atualizar registro!" + e.getMessage());
            return false;
        }
        return  true;
    }

    public boolean delete(TodoItem todoItem){
        try{
            String [] args ={todoItem.getId().toString()};

            escreve.delete(DBHelper.TABELA_TODO,"id=?",args);
            Log.i("INFO DB","item deletado com sucesso!");

        }catch (Exception e){
            Log.i("INFO DB","Erro ao Deletar item!" + e.getMessage());
            return false;
        }
        return  true;
    }

    public List<TodoItem> load(Integer id){
        List<TodoItem> todoItemList = new ArrayList<>();
        String sql = "SELECT * FROM "+DBHelper.TABELA_TODO+" WHERE id_usuario="+id;
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

    public List<TodoItem> findByIdUsuario(Integer idUsuario){
        List<TodoItem> lisTtodoItems = new ArrayList<>();

        //1. string sql de consulta
        String sql = "SELECT * FROM "+DBHelper.TABELA_TODO+ ";";

        //2. Cursor para acesso aos dados
        Cursor c = le.rawQuery(sql,null);

        //3. percorrer o cursor
        c.moveToFirst();
        try{
            while(c.moveToNext()){

                TodoItem todoItem = new TodoItem();

                //Long id = c.getLong( 0 );
                Integer id = c.getInt( c.getColumnIndexOrThrow("id") );
                String title = c.getString(c.getColumnIndexOrThrow("title"));
                String description = c.getString(c.getColumnIndexOrThrow("description"));
                String deadline = c.getString(c.getColumnIndexOrThrow("deadline"));


                todoItem.setId(id);
                todoItem.setTitle(title);
                todoItem.setDescription(description);
                todoItem.setDeadLine(formatter.parse(deadline));
                todoItem.setIdUsuario(idUsuario);

                lisTtodoItems.add(todoItem);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        c.close();
        return lisTtodoItems;
    }



}
