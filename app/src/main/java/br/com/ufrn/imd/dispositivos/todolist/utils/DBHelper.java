package br.com.ufrn.imd.dispositivos.todolist.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper  extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String NOME_BD = "todo_list_DB";
    public static String TABELA_TODO = "todo_item";
    public static String TABELA_USUARIO = "usuario";

    public DBHelper(@Nullable Context context){
        super(context,NOME_BD,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String criarTabelaTodo = "CREATE TABLE IF NOT EXISTS " + TABELA_TODO
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + "title VARCHAR(50) NOT NULL, "
                + "deadline VARCHAR(50) NOT NULL, "
                + "descricao VARCHAR(50) NOT NULL);";

        String criarTabelaUsuario = "CREATE TABLE IF NOT EXISTS " + TABELA_USUARIO
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + "username VARCHAR(20) NOT NULL,"
                + "password VARCHAR(20) NOT NULL);";
        try{
            db.execSQL(criarTabelaTodo);
            Log.i("INFO DB","Sucesso ao criar ao tabela Todo!");
            db.execSQL(criarTabelaUsuario);
            Log.i("INFO DB","Sucesso ao criar ao tabela Usuário!");
        }catch(Exception e){
            Log.i("INFO DB","Erro ao criar tabela "+e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + TABELA_TODO +";";

        try{
            db.execSQL(sql);
            onCreate(db);
            Log.i("INFO DB","Sucesso ao criar ao tabela!");
        }catch(Exception e){
            Log.i("INFO DB","Erro ao criar tabela "+e.getMessage());
        }


    }
}