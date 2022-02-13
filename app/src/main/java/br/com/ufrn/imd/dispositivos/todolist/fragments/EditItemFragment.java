package br.com.ufrn.imd.dispositivos.todolist.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.ufrn.imd.dispositivos.todolist.R;
import br.com.ufrn.imd.dispositivos.todolist.model.TodoItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditItemFragment extends DialogFragment implements DatePickerFragment.OnDateSet{
    public static final String DIALOG_TAG = "editTodoItem";

    private EditText etTitle;
    private EditText etDescription;
    private Button btnSalvar;
    private Button btnDelete;
    private Button btnCancelar;
    private Button btnDeadLine;
    private TodoItem itemSelected;

    public EditItemFragment() {
        // Required empty public constructor
    }

    public static EditItemFragment newInstance(TodoItem itemSelect) {
        EditItemFragment fragment = new EditItemFragment();
        Bundle args = new Bundle();
        args.putSerializable("itemSelect",itemSelect);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle args = getArguments();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        View layout = inflater.inflate(R.layout.fragment_edit_item, container, false);
        getDialog().setTitle("EDIT ITEM");

        itemSelected = (TodoItem) args.getSerializable("itemSelect");

        etTitle = layout.findViewById(R.id.etTitle);
        etDescription = layout.findViewById(R.id.etDescription);

        etTitle.setText(itemSelected.getTitle());
        etDescription.setText(itemSelected.getDescription());

        btnSalvar = layout.findViewById(R.id.btnSave);
        btnDelete = layout.findViewById(R.id.btnDelete);
        btnCancelar = layout.findViewById(R.id.btnCancel);
        btnDeadLine = layout.findViewById(R.id.btnDeadLine);

        btnDeadLine.setText(formatter.format(itemSelected.getDeadLine()));

        btnDeadLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance();
                datePickerFragment.show(getChildFragmentManager(), DatePickerFragment.DIALOG_TAG);
            }
        });

        btnSalvar.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem();
                dismiss();
            }
        }));

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
                dismiss();
            }
        });

        return  layout;
    }

    public void updateItem(){
        Activity activity = getActivity();

        if(activity instanceof OnUpdateItem){
            String title, description, deadline;

            title = etTitle.getText().toString();
            description = etDescription.getText().toString();
            deadline = btnDeadLine.getText().toString();

            if(title.isEmpty() || description.isEmpty() || deadline.isEmpty()) {
                Snackbar.make(
                        getView(),
                        "Por favor, preencha todos os campos",
                        Snackbar.LENGTH_LONG
                ).show();
                return;
            }

            itemSelected.setTitle(title);
            itemSelected.setDescription(description);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try {
                itemSelected.setDeadLine(formatter.parse(deadline));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            OnUpdateItem listener = (OnUpdateItem) activity;
            listener.updateItem(itemSelected);

        }
    }

    public void deleteItem() {
        Activity activity = getActivity();

        if (activity instanceof OnDeleteItem) {
            OnDeleteItem listener = (OnDeleteItem) activity;
            listener.deleteItem(itemSelected);
        }
    }

    public interface OnUpdateItem{
        void updateItem(TodoItem todoItem);
    }

    public interface OnDeleteItem{
        void deleteItem(TodoItem todoItem);
    }

    @Override
    public void setDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        btnDeadLine.setText(formatter.format(date));
    }

}