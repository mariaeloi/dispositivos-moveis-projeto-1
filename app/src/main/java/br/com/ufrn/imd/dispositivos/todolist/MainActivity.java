package br.com.ufrn.imd.dispositivos.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.com.ufrn.imd.dispositivos.todolist.model.TodoItem;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener{

    RecyclerViewAdapter adapter;
    SearchView simpleSearchView;
    RecyclerView rvTodoList;
    List<TodoItem> todoItemList;
    List<TodoItem> todoItemListCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }


    @Override
    public void onItemClick(View view, int position) {

    }



}