package br.com.ufrn.imd.dispositivos.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import br.com.ufrn.imd.dispositivos.todolist.dao.UsuarioDAO;
import br.com.ufrn.imd.dispositivos.todolist.model.Usuario;

public class EditUserActivity  extends AppCompatActivity {

    private EditText usernameET;
    private EditText passwordET;
    private Button confimarAtualizarPerfilBtn;
    private Button confimarDeletarPefilBtn;
    private EditText cityET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        UsuarioDAO usuarioDAO;
        Usuario usuario;
        usernameET = findViewById(R.id.atualizarUsernameTV);
        cityET = findViewById(R.id.atualizarCity);
        passwordET = findViewById(R.id.atualizarPasswordTV);
        confimarAtualizarPerfilBtn = findViewById(R.id.confimarAtualizarPefilBtn);
        confimarDeletarPefilBtn = findViewById(R.id.confimarDeletarPefilBtn);

        usuarioDAO = new UsuarioDAO(getApplicationContext());
        usuario = usuarioDAO.getUsuarioLogado();

        usernameET.setText(usuario.getUsername().toString());
        cityET.setText(usuario.getCity().toString());
        passwordET.setText(usuario.getPassword().toString());

        confimarDeletarPefilBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Tem certeza disso?")
                    .setMessage("Essa operação não tem como ser desfeita")

                    .setPositiveButton("EXCLUIR", (new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(usuarioDAO.deletar(usuario)) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    }))

                    .setNegativeButton(R.string.button_sair, null)
                    .show();

        });
        confimarAtualizarPerfilBtn.setOnClickListener(v -> {
            String username = usernameET.getText().toString();
            String password = passwordET.getText().toString();
            String city = cityET.getText().toString();


                usuario.setUsername(username);
                usuario.setPassword(password);
                usuario.setCity(city);

                if(usuarioDAO.atualizar(usuario)){
                    Toast.makeText(getApplicationContext(), "Usuário atualizado", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Usuário não atualizado", Toast.LENGTH_SHORT).show();
                    finish();
                }
        });
    }


}
