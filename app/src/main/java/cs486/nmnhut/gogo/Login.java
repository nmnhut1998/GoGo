package cs486.nmnhut.gogo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Button btnLogin;
    Button btnSignUp;
    EditText txtUsername;
    EditText txtPassword;
    private FirebaseAuth mAuth;

    final int NetworkPermission = 100;
    final int LocationPermission = 101 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUIVariables();

        Toast t = Toast.makeText(this,"Loading",Toast.LENGTH_LONG);
        t.show();

        btnSignUp.setEnabled(false);
        btnLogin.setEnabled(false);

        checkPermission_and_set_Events_to_button();

    }

    private void checkPermission_and_set_Events_to_button() {
        if (ContextCompat.checkSelfPermission(Login.this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            Toast t = Toast.makeText(this,"Not granted",Toast.LENGTH_SHORT);
            t.show();
            requestPermissions(new String[] {Manifest.permission.INTERNET},NetworkPermission);
        }
        else
        {
            Toast t = Toast.makeText(this,"Granted",Toast.LENGTH_SHORT);
            t.show();
            initializeButtons();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NetworkPermission && grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            initializeButtons();
        }


    }

    private void initializeButtons() {
        mAuth = FirebaseAuth.getInstance();
        btnSignUp.setEnabled(true);
        btnLogin.setEnabled(true);

        setLoginClick();

        setSignUpClick();
    }

    private void setSignUpClick() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                mAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast t = Toast.makeText(Login.this,"Sign up successfully! Now you can login",Toast.LENGTH_SHORT);
                            t.show();
                        }
                        else
                        {
                            Toast t = Toast.makeText(Login.this,"Sign up fail! Make sure email is of right format and password length is at least 6", Toast.LENGTH_SHORT);
                            t.show();
                        }
                    }
                });
            }
        });
    }

    private void setLoginClick() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast t =  Toast.makeText(Login.this,"Login successfully!",Toast.LENGTH_SHORT);
                            t.show();
                            Intent intent = new Intent(Login.this,MainActivity.class);
                            startActivity(intent);

                        }
                        else
                        {
                            Toast t =  Toast.makeText(Login.this,"Login failed!",Toast.LENGTH_SHORT);
                            t.show();
                        }
                    }
                });
            }
        });
    }

    private void setUIVariables() {
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtPassword = findViewById(R.id.txtPassword);
        txtUsername = findViewById(R.id.txtUsername);
    }
}
