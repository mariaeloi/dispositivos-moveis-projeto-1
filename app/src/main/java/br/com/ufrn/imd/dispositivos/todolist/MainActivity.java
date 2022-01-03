package br.com.ufrn.imd.dispositivos.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.com.ufrn.imd.dispositivos.todolist.fragments.TodoItemDialog;
import br.com.ufrn.imd.dispositivos.todolist.model.TodoItem;

public class MainActivity extends AppCompatActivity
        implements RecyclerViewAdapter.ItemClickListener, TodoItemDialog.OnSaveTodoItem {

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
}