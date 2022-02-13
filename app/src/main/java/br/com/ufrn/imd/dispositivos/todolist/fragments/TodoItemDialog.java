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

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import br.com.ufrn.imd.dispositivos.todolist.R;
import br.com.ufrn.imd.dispositivos.todolist.model.TodoItem;

/**
 * Use the {@link TodoItemDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoItemDialog extends DialogFragment
        implements DatePickerFragment.OnDateSet {

    public static final String DIALOG_TAG = "addTodoItem";

    private EditText etTitle;
    private EditText etDescription;

    private Button btnDeadLine;
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
        etDescription = layout.findViewById(R.id.etDescription);

        btnDeadLine = layout.findViewById(R.id.btnDeadLine);
        btnSave = layout.findViewById(R.id.btnSave);
        btnCancel = layout.findViewById(R.id.btnCancel);

        btnDeadLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance();
                datePickerFragment.show(getChildFragmentManager(), DatePickerFragment.DIALOG_TAG);
            }
        });

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
            deadline = btnDeadLine.getText().toString();

            if(title.isEmpty() || description.isEmpty() || deadline.isEmpty()) {
                Snackbar.make(
                        getView(),
                        "Please fill out all fields.",
                        Snackbar.LENGTH_LONG
                ).setActionTextColor(getResources().getColor(R.color.design_default_color_error)).show();
                return;
            }

            todoItem.setTitle(title);
            todoItem.setDescription(description);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try {
                todoItem.setDeadLine(formatter.parse(deadline));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            OnSaveTodoItem listener = (OnSaveTodoItem) activity;
            listener.saveTodoItem(todoItem);

        }

        // close dialog
        dismiss();
    }

    @Override
    public void setDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        btnDeadLine.setText(formatter.format(date));
    }

    public interface OnSaveTodoItem {
        void saveTodoItem(TodoItem todoItem);
    }
}