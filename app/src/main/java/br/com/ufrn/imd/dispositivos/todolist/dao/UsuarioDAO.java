package br.com.ufrn.imd.dispositivos.todolist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.ufrn.imd.dispositivos.todolist.model.Usuario;
import br.com.ufrn.imd.dispositivos.todolist.utils.DBHelper;

public class UsuarioDAO {

    private final SQLiteDatabase escreve;
    private final SQLiteDatabase le;

    public UsuarioDAO(Context context){
        DBHelper dbHelper = new DBHelper(context);
        escreve = dbHelper.getWritableDatabase();
        le = dbHelper.getReadableDatabase();
    }
    public boolean login(Usuario usuario){
        String sql = "SELECT username, password FROM "
                +DBHelper.TABELA_USUARIO
                + " WHERE"
                +" username=?;";

        Cursor c = le.rawQuery(sql, new String[]{usuario.getUsername()});
        String password;
        try {
            c.moveToFirst();
            password = c.getString( c.getColumnIndexOrThrow("password") );
            if(password.equals(usuario.getPassword())){
                return true;
            }
        } catch(Exception e){
            return false;
        }
        return false;

    }
    public boolean salvar(Usuario usuario){
            //1. definir o conteudo a ser salvo
            ContentValues cv = new ContentValues();
            cv.put("username",usuario.getUsername());
            cv.put("password", usuario.getPassword());
            try{
                escreve.insert(DBHelper.TABELA_USUARIO,null,cv);
                Log.i("INFO","Registro salvo com sucesso!");
            }catch(Exception e){
                Log.i("INFO","Erro ao salvar registro: "+e.getMessage());
                return false;
            }
            return true;

    }



    public boolean atualizar(Usuario usuario){

        //1. definir conteudo a ser salvo
        ContentValues cv = new ContentValues();
        cv.put("username",usuario.getUsername());
        cv.put("password",usuario.getPassword());

        //2. atualizar valor no banco
        try{
            String[] args = {usuario.getId().toString()};
            //2.1 update(nome da tabela, conteudo para atualizar, clausula de atualização (where)
            // o argumento da condição --> ?)
            escreve.update(DBHelper.TABELA_USUARIO,cv,"id=?",args);
            Log.i("INFO","Registro atualizado com sucesso!");
        }catch(Exception e){
            Log.i("INFO","Erro ao atualizar registro!" + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean deletar(Usuario usuario){

        //1. deletar um registro de tarefa na tabela tarefas

        try{
            //id do registro que será deletado
            String[] args = {usuario.getId().toString()};
            escreve.delete(DBHelper.TABELA_USUARIO,"id=?",args);
            Log.i("INFO","Registro apagado com sucesso!");
        }catch(Exception e){
            Log.i("INFO","Erro apagar registro!"+e.getMessage());
            return false;
        }
        return true;

    }

    public int usuariosLogados() {
        String sql = "SELECT * FROM "
                +DBHelper.TABELA_USUARIO;

        Cursor c = le.rawQuery(sql, null);

        int count= c.getCount();
        c.close();
        return count;
    }
}
