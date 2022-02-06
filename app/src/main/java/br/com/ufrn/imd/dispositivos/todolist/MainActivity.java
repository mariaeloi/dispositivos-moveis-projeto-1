package br.com.ufrn.imd.dispositivos.todolist;

import static android.view.View.INVISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.ufrn.imd.dispositivos.todolist.dao.UsuarioDAO;
import br.com.ufrn.imd.dispositivos.todolist.model.Usuario;

public class MainActivity extends AppCompatActivity {

    private EditText usernameET;
    private EditText passwordET;
    private Button cadastrarUserBtn;
    private Button loginBtn;
    UsuarioDAO usuarioDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameET = findViewById(R.id.cadastrarUsernameTV);
        passwordET = findViewById(R.id.cadastrarPasswordTV);
        cadastrarUserBtn = findViewById(R.id.goToCadastrarBtn);
        loginBtn = findViewById(R.id.confimarCadastroBtn);
        usuarioDAO = new UsuarioDAO(getApplicationContext());
        if(usuarioDAO.usuariosLogados() >= 1){
            cadastrarUserBtn.setVisibility(INVISIBLE);
        } else {
            cadastrarUserBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), RegisterUserActivity.class);
                    startActivity(intent);
                }
            });
        }

        loginBtn.setOnClickListener(v -> {
            String username = usernameET.getText().toString();
            String password = passwordET.getText().toString();
            if(!username.isEmpty() && username != null && !password.isEmpty() && password != null){
                Usuario usuario = new Usuario();
                usuario.setUsername(username);
                usuario.setPassword(password);
                if(usuarioDAO.login(usuario)){
                    Toast.makeText(getApplicationContext(), "Login Realizado!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), TarefaActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Verifique suas credenciais!", Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(getApplicationContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();            }

        });



    }

}
