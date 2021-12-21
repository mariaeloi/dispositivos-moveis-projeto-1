package br.com.ufrn.imd.dispositivos.todolist.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Date;

import br.com.ufrn.imd.dispositivos.todolist.R;
import br.com.ufrn.imd.dispositivos.todolist.model.TodoItem;

/**
 * Use the {@link TodoItemDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoItemDialog extends DialogFragment {

    public static final String DIALOG_TAG = "addTodoItem";

    private EditText etTitle;
    private EditText etDeadLine;
    private EditText etDescription;

    private Button btnSave;
    private Button btnCancel;

    private TodoItem todoItem;

    public TodoItemDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment TodoItemDialog.
     */
    public static TodoItemDialog newInstance() {
        TodoItemDialog fragment = new TodoItemDialog();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todoItem = new TodoItem();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_todo_item_dialog, container, false);
        getDialog().setTitle("ADD ITEM");

        etTitle = layout.findViewById(R.id.etTitle);
        etDeadLine = layout.findViewById(R.id.etDeadLine);
        etDescription = layout.findViewById(R.id.etDescription);

        btnSave = layout.findViewById(R.id.btnSave);
        btnCancel = layout.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTodoItem();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // close dialog
                dismiss();
            }
        });

        return layout;
    }

    public void saveTodoItem() {
        Activity activity = getActivity();

        if(activity instanceof OnSaveTodoItem) {
            String title, description, deadline;

            title = etTitle.getText().toString();
            description = etDescription.getText().toString();
            deadline = etDeadLine.getText().toString();

            if(title.isEmpty() || description.isEmpty() || deadline.isEmpty()) {
                Snackbar.make(
                        getView(),
                        "Please fill out all fields.",
                        Snackbar.LENGTH_LONG
                ).setActionTextColor(getResources().getColor(R.color.design_default_color_error)).show();
                return;
            }

            todoItem.setTitle(etTitle.getText().toString());
            todoItem.setDescription(etDescription.getText().toString());

            todoItem.setDeadLine(Date.valueOf(deadline.replace('/', '-')));
        }

        OnSaveTodoItem listener = (OnSaveTodoItem) activity;
        listener.saveTodoItem(todoItem);

        // close dialog
        dismiss();
    }

    public interface OnSaveTodoItem {
        void saveTodoItem(TodoItem todoItem);
    }
}