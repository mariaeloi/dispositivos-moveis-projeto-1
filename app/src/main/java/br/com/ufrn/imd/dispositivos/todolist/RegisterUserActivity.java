package br.com.ufrn.imd.dispositivos.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.ufrn.imd.dispositivos.todolist.dao.UsuarioDAO;
import br.com.ufrn.imd.dispositivos.todolist.model.Usuario;

public class RegisterUserActivity  extends AppCompatActivity {

    private EditText cadastrarUsernameTV;
    private EditText cadastrarPasswordTV;
    private Button confimarCadastroBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        cadastrarUsernameTV = findViewById(R.id.cadastrarUsernameTV);
        cadastrarPasswordTV = findViewById(R.id.cadastrarPasswordTV);
        confimarCadastroBtn = findViewById(R.id.confimarCadastroBtn);

        confimarCadastroBtn.setOnClickListener(v -> {
            String username = cadastrarUsernameTV.getText().toString();
            String password = cadastrarPasswordTV.getText().toString();
            if(!username.isEmpty() && username != null && !password.isEmpty() && password != null){
                UsuarioDAO usuarioDAO = new UsuarioDAO(getApplicationContext());
                Usuario usuario = new Usuario();
                usuario.setUsername(username);
                usuario.setPassword(password);
                if(usuarioDAO.salvar(usuario)){
                    Toast.makeText(getApplicationContext(), "Usuário cadatrado", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Usuário não cadatrado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
