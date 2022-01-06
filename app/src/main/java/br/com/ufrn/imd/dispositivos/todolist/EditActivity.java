package br.com.ufrn.imd.dispositivos.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.ufrn.imd.dispositivos.todolist.fragments.DatePickerFragment;
import br.com.ufrn.imd.dispositivos.todolist.model.TodoItem;

public class EditActivity extends AppCompatActivity implements DatePickerFragment.OnDateSet {
    EditText etTitle;
    EditText etDescription;
    Button btnSalvar;
    Button btnCancelar;
    Button btnDeadLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        TodoItem it = (TodoItem) getIntent().getSerializableExtra("todoItem");

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);

        btnSalvar = findViewById(R.id.btnSave);
        btnCancelar = findViewById(R.id.btnCancel);
        btnDeadLine = findViewById(R.id.btnDeadLine);

        etTitle.setText(it.getTitle());
        etDescription.setText(it.getDescription());

        btnDeadLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance();
                datePickerFragment.show(getSupportFragmentManager(),  DatePickerFragment.DIALOG_TAG);

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.err.println("AAAA");

                System.out.println(btnDeadLine.getText().toString());
            }
        });


    }

   // @Override
   // public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
    //    Calendar c = Calendar.getInstance();
     //   c.set(Calendar.YEAR,year);
       // c.set(Calendar.MONTH,month);
        // c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
    //     String currentDateString = DateFormat.getDateInstance().format(c.getTime());
    //    btnDeadLine.setText(currentDateString);
    //}


    @Override
    public void setDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        btnDeadLine.setText(formatter.format(date));
    }
}