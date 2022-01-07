package br.com.ufrn.imd.dispositivos.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ufrn.imd.dispositivos.todolist.fragments.EditItemFragment;
import br.com.ufrn.imd.dispositivos.todolist.fragments.TodoItemDialog;
import br.com.ufrn.imd.dispositivos.todolist.model.TodoItem;

public class MainActivity extends AppCompatActivity
        implements RecyclerViewAdapter.ItemClickListener, TodoItemDialog.OnSaveTodoItem, EditItemFragment.OnUpdateItem, EditItemFragment.OnDeleteItem {

    FragmentManager fragmentManager;
    RecyclerViewAdapter adapter;
    SearchView simpleSearchView;
    RecyclerView rvTodoList;
    List<TodoItem> todoItemList;
    List<TodoItem> todoItemListCopy;

    private FloatingActionButton facbnewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);

        fragmentManager = getSupportFragmentManager();

        todoItemList = new ArrayList<>();
        todoItemListCopy = new ArrayList<>();

        todoItemList.add(new TodoItem(1, "Atividade de SO", "video aula", new Date()));
        todoItemList.add(new TodoItem(2, "Atividade de Dispositivos moveis", "este trabalho", new Date()));
        todoItemList.add(new TodoItem(3, "Atividade de Levantamento de requisitos", "Atividade da  Bel", new Date()));
        todoItemList.add(new TodoItem(4, "Atividade de Processos de Software", "Atividade do Eiji", new Date()));
        todoItemList.add(new TodoItem(5, "Atividade de MicroServiços", "Atividade do Fred", new Date()));

        todoItemListCopy.add(new TodoItem(1, "Atividade de SO", "video aula", new Date()));
        todoItemListCopy.add(new TodoItem(2, "Atividade de Dispositivos moveis", "este trabalho", new Date()));
        todoItemListCopy.add(new TodoItem(3, "Atividade de Levantamento de requisitos", "Atividade da   Bel", new Date()));
        todoItemListCopy.add(new TodoItem(4, "Atividade de Processos de Software", "Atividade do Eiji", new Date()));
        todoItemListCopy.add(new TodoItem(5, "Atividade de MicroServiços", "Atividade do Fred", new Date()));

        simpleSearchView = findViewById(R.id.simpleSearchView);

        adapter = new RecyclerViewAdapter(this, todoItemList, todoItemListCopy);
        adapter.setClickListener(this);

        rvTodoList =  findViewById(R.id.rvTodoList);
        rvTodoList.setLayoutManager(new LinearLayoutManager(this));
        rvTodoList.setAdapter(adapter);
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

        facbnewItem = findViewById(R.id.facbnewItem);
        facbnewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoItemDialog todoItemDialog = TodoItemDialog.newInstance();
                todoItemDialog.show(fragmentManager, TodoItemDialog.DIALOG_TAG);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        TodoItem itemSelected = (TodoItem) todoItemList.get(position);

        EditItemFragment editItemFragment = EditItemFragment.newInstance(itemSelected);
        editItemFragment.show(fragmentManager, TodoItemDialog.DIALOG_TAG);
    }

    @Override
    public void saveTodoItem(TodoItem todoItem) {
        // set TodoItem id
        Integer id = todoItemList.get(todoItemList.size()-1).getId() + 1;
        todoItem.setId(id);

        todoItemList.add(todoItem);
        todoItemListCopy.add(todoItem);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateItem(TodoItem todoItem) {
        int id = todoItem.getId() - 1;
        Log.d("id", "id: " + id);
        todoItemList.set(id, todoItem);
        todoItemListCopy.set(id, todoItem);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void deleteItem(TodoItem todoItem)
    {
        int id = todoItem.getId() - 1;
        Log.d("id", "id: " + id);
        todoItemList.remove(id);
        todoItemListCopy.remove(id);
        adapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(R.string.button_visitar_site, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        goToUrl("https://github.com/mariaeloi/dispositivos-moveis-projeto-1");
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(R.string.button_sair, null)
                .show();
    }
}