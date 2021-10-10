package primeiro.cliente.gestaoclientes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class FormLogin extends AppCompatActivity {

    private TextView text_tela_cadastro;
    private EditText edit_email, edit_senha;
    private Button bt_entrar;
    private ProgressBar progressBar;
    String[] mensagens = {"Prencha todos os campos"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

       //Esconde a barra de ação do aplicativo na tela de login
       getSupportActionBar().hide();

       IniciarComponentes();

       //Direciona o usuário para o formulário de cadastro de usuário.
       text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(FormLogin.this,FormCadUsuario.class);
               startActivity(intent);
           }
       });

       bt_entrar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String email = edit_email.getText().toString();
               String senha = edit_senha.getText().toString();

               //Verifica se os campos estão vazios, se sim, apresenta mensagens[0].
               if (email.isEmpty() || senha.isEmpty()){
                   Snackbar snackbar = Snackbar.make(view,mensagens[0],Snackbar.LENGTH_SHORT);
                   snackbar.setBackgroundTint(Color.rgb(220,53,69));
                   snackbar.setTextColor(Color.WHITE);
                   snackbar.show();
               }else{
                   AutenticarUsuario(view);
               }
           }
       });
    }


    // Método onStart: Verifica se o usuário já esta logado no Firebase, se sim ao acessar o app será direcionado a TelaPrincipal, caso contrario para o FormLogin.
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if(usuarioAtual != null){
            TelaPrincipal();
        }

    }

    private void AutenticarUsuario(View view){
        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        //Comunica com Frirebase utilizando e autentica usando o email e senha
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //Se a autenticação for realizada com sucesso, a progressbar fica visivel, deixando o efeito de animação por 3 seg.
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TelaPrincipal();
                        }
                    }, 3000);
                }else{
                    String erro;
                    try {
                        throw task.getException();

                    }catch (FirebaseAuthInvalidUserException e) {
                        erro = "E-mail invalido ou não cadastrado";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "Senha invalida";
                    }catch (Exception e){
                        erro = "Erro ao cadastrar usuário";
                    }
                    Snackbar snackbar = Snackbar.make(view,erro,Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.rgb(220,53,69));
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();

                }
            }
        });
    }

    private void TelaPrincipal(){
        Intent intent = new Intent(FormLogin.this,TelaPrincipal.class);
        startActivity(intent);
        finish();
    }

    //pega o id do componente da tela, quando o usuário clicar para realizar o cadastro.
    private void IniciarComponentes(){
        text_tela_cadastro =findViewById(R.id.text_tela_cadastro);
        edit_email =findViewById(R.id.edit_email);
        edit_senha =findViewById(R.id.edit_senha);
        bt_entrar =findViewById(R.id.bt_entrar);
        progressBar =findViewById(R.id.progressbar);

    }
}