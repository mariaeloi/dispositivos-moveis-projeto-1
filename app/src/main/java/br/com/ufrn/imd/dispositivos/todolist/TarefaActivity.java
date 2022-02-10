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
import java.util.List;

import br.com.ufrn.imd.dispositivos.todolist.fragments.EditItemFragment;
import br.com.ufrn.imd.dispositivos.todolist.fragments.TodoItemDialog;
import br.com.ufrn.imd.dispositivos.todolist.model.TodoItem;
import br.com.ufrn.imd.dispositivos.todolist.dao.TodoItemDAO;

public class TarefaActivity extends AppCompatActivity
        implements RecyclerViewAdapter.ItemClickListener, TodoItemDialog.OnSaveTodoItem, EditItemFragment.OnUpdateItem, EditItemFragment.OnDeleteItem {

    FragmentManager fragmentManager;
    RecyclerViewAdapter adapter;
    SearchView simpleSearchView;
    RecyclerView rvTodoList;
    List<TodoItem> todoItemList;
    List<TodoItem> todoItemListCopy;
    TodoItemDAO todoItemDAO;

    private FloatingActionButton facbnewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefa);

        fragmentManager = getSupportFragmentManager();

        todoItemList = new ArrayList<>();
        todoItemListCopy = new ArrayList<>();

        todoItemDAO = new TodoItemDAO(getApplicationContext());
        todoItemList.addAll(todoItemDAO.load());
        todoItemListCopy.addAll(todoItemList);

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
//        Integer id;
//        if (todoItemList.size() == 0) {
//            id = 0;
//        }
//        else {
//            id = todoItemList.get(todoItemList.size() - 1).getId() + 1;
//        }
//
//
//        todoItem.setId(id);
        if( todoItemDAO.create(todoItem)){
            Toast.makeText(getApplicationContext(), "Tarefa cadatrada", Toast.LENGTH_SHORT).show();
            // TODO atualizar `todoItemList`
//            todoItemList.add(todoItem);
//            todoItemListCopy.add(todoItem);
//            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getApplicationContext(), "Erro ao cadastrar tarefa", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateItem(TodoItem todoItem) {
//        int id = todoItem.getId();
//        Log.d("id", "id: " + id);
        if(todoItemDAO.update(todoItem)){
            Toast.makeText(getApplicationContext(), "Tarefa atualizada", Toast.LENGTH_SHORT).show();
            // TODO atualizar `todoItemList`

//            todoItemList.set(id, todoItem);
//            todoItemListCopy.set(id, todoItem);
//            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getApplicationContext(), "Erro ao atualizar tarefa", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void deleteItem(TodoItem todoItem)
    {
//        int id = todoItem.getId();
//        todoItemList.remove(id-1);
//        todoItemListCopy.remove(id-1);
//        updateIndex(id);
//        adapter.notifyDataSetChanged();

        if(todoItemDAO.delete(todoItem)) {
            Toast.makeText(getApplicationContext(), "Tarefa removida", Toast.LENGTH_SHORT).show();
            // TODO atualizar `todoItemList`
        } else {
            Toast.makeText(getApplicationContext(), "Erro ao remover tarefa", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateIndex(int initialId){
        for (int i=initialId+1; i<=todoItemList.size()+1; i++) {
            TodoItem item = todoItemList.get(i-2);
            item.setId(i-1);
            todoItemList.set(i-2, item);
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
}