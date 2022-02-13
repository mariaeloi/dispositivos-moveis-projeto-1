package br.com.ufrn.imd.dispositivos.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.content.DialogInterface;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import br.com.ufrn.imd.dispositivos.todolist.dao.UsuarioDAO;
import br.com.ufrn.imd.dispositivos.todolist.fragments.EditItemFragment;
import br.com.ufrn.imd.dispositivos.todolist.fragments.TodoItemDialog;
import br.com.ufrn.imd.dispositivos.todolist.model.TodoItem;
import br.com.ufrn.imd.dispositivos.todolist.dao.TodoItemDAO;
import br.com.ufrn.imd.dispositivos.todolist.model.Usuario;

public class TarefaActivity extends AppCompatActivity
        implements RecyclerViewAdapter.ItemClickListener, TodoItemDialog.OnSaveTodoItem, EditItemFragment.OnUpdateItem, EditItemFragment.OnDeleteItem {

    FragmentManager fragmentManager;
    RecyclerViewAdapter adapter;
    SearchView simpleSearchView;
    RecyclerView rvTodoList;
    List<TodoItem> todoItemList;
    List<TodoItem> todoItemListCopy;
    ActionMenuItemView clima;

    private FloatingActionButton facbnewItem;

    private final static String urlLocal = "https://geocoding-api.open-meteo.com/v1/search?name=Berlin";

    TodoItemDAO todoItemDAO;

    private FloatingActionButton facbnewItem2;
    private UsuarioDAO usuarioDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa);

        usuarioDAO = new UsuarioDAO(getApplicationContext());
        Usuario usuario = usuarioDAO.getUsuarioLogado();

        fragmentManager = getSupportFragmentManager();

        todoItemList = new ArrayList<>();
        todoItemListCopy = new ArrayList<>();

        todoItemDAO = new TodoItemDAO(getApplicationContext());
        todoItemList.addAll(todoItemDAO.load(usuario.getId()));
        todoItemListCopy.addAll(todoItemList);

        simpleSearchView = findViewById(R.id.simpleSearchView);

        adapter = new RecyclerViewAdapter(this, todoItemList, todoItemListCopy);
        adapter.setClickListener(this);

        rvTodoList =  findViewById(R.id.rvTodoList);
        rvTodoList.setLayoutManager(new LinearLayoutManager(this));
        rvTodoList.setAdapter(adapter);

        facbnewItem = findViewById(R.id.facbnewItem);
        facbnewItem2 = findViewById(R.id.facbnewItem2);
        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });

        facbnewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoItemDialog todoItemDialog = TodoItemDialog.newInstance();
                todoItemDialog.show(fragmentManager, TodoItemDialog.DIALOG_TAG);
            }
        });

        // consumir API de local e depois de clima
        LocalAPI local = new LocalAPI();
        local.execute(urlLocal);
        facbnewItem2.setOnClickListener(v-> {
            Intent intent = new Intent(getApplicationContext(), EditUserActivity.class);
            startActivity(intent);
        });
    }

    private void reloadTasks() {
        Usuario usuario = usuarioDAO.getUsuarioLogado();

        todoItemList.clear();
        todoItemList.addAll(todoItemDAO.load(usuario.getId()));
        todoItemListCopy.clear();
        todoItemListCopy.addAll(todoItemList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        TodoItem itemSelected = (TodoItem) todoItemList.get(position);

        EditItemFragment editItemFragment = EditItemFragment.newInstance(itemSelected);
        editItemFragment.show(fragmentManager, TodoItemDialog.DIALOG_TAG);
    }

    @Override
    public void saveTodoItem(TodoItem todoItem) {
        Usuario usuario = usuarioDAO.getUsuarioLogado();
        todoItem.setIdUsuario(usuario.getId());


        if( todoItemDAO.create(todoItem)){
            Toast.makeText(getApplicationContext(), "Tarefa cadatrada", Toast.LENGTH_SHORT).show();
            reloadTasks();
        } else {
            Toast.makeText(getApplicationContext(), "Erro ao cadastrar tarefa", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateItem(TodoItem todoItem) {
        if(todoItemDAO.update(todoItem)){
            Toast.makeText(getApplicationContext(), "Tarefa atualizada", Toast.LENGTH_SHORT).show();
            reloadTasks();
        } else {
            Toast.makeText(getApplicationContext(), "Erro ao atualizar tarefa", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void deleteItem(TodoItem todoItem)
    {
        if(todoItemDAO.delete(todoItem)) {
            Toast.makeText(getApplicationContext(), "Tarefa removida", Toast.LENGTH_SHORT).show();
            reloadTasks();
        } else {
            Toast.makeText(getApplicationContext(), "Erro ao remover tarefa", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    public void onComposeAction(MenuItem mi) {
        new AlertDialog.Builder(this)
                .setTitle("Membros da equipe")
                .setMessage(" Maria Eduarda \n Fernando Ferreira \n André Herman \n Italo Silva")

                .setPositiveButton(R.string.button_visitar_site, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        goToUrl("https://github.com/mariaeloi/dispositivos-moveis-projeto-1");
                    }
                })
                .setNegativeButton(R.string.button_sair, null)
                .show();
    }

    class LocalAPI extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            InputStream is = null;
            InputStreamReader isr = null;
            StringBuffer buffer = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                is = conn.getInputStream();
                isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr);
                buffer = new StringBuffer();
                String linha = "";

                while ((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String lat = "";
            String lon = "";

            try {
                JSONObject jo = new JSONObject(s);
                lat = jo.getJSONArray("results").getJSONObject(0).getString("latitude");
                lon = jo.getJSONArray("results").getJSONObject(0).getString("longitude");

            }
            catch (Exception e) {
                e.printStackTrace();;
            }

            String urlClima = "https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lon + "&hourly=temperature_2m";
            ClimaAPI clima = new ClimaAPI();
            clima.execute(urlClima);
        }
    }

    class ClimaAPI extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            InputStream is = null;
            InputStreamReader isr = null;
            StringBuffer buffer = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                is = conn.getInputStream();
                isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr);
                buffer = new StringBuffer();
                String linha = "";

                while ((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return buffer.toString();
        }

        @SuppressLint("RestrictedApi")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String result = "";

            try {
                JSONObject jo = new JSONObject(s);
                result = jo.getJSONObject("hourly").getJSONArray("temperature_2m").getString(0);
            }
            catch (Exception e) {
                e.printStackTrace();;
            }

            clima = findViewById(R.id.menuclima);
            clima.setTitle(result + " ºC");
        }
    }
}