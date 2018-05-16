package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clouwiko.dev.prasiku.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ProgressBar progressBar;
    private EditText newPassword;
    private Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //Get Firebase Auth Instance
        auth = FirebaseAuth.getInstance();

        //Get Current User
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user == null) {
//                    // user auth state is changed - user is null
//                    // launch login activity
//                    startActivity(new Intent(ChangePasswordActivity.this, SignInActivity.class));
//                    finish();
//                }
//            }
//        };

        newPassword = (EditText) findViewById(R.id.new_password);
        btnChangePassword = (Button) findViewById(R.id.action_change_password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                final String validPassword = "^(?=\\S+$).{4,}$";
                Matcher matcherPassword = Pattern.compile(validPassword).matcher(newPassword.getText().toString().trim());
                if (user != null && !newPassword.getText().toString().trim().equals("")) {
                    if (newPassword.getText().toString().trim().length() < 6) {
                        newPassword.setError("Password too Short, Enter Minimum 6 Characters");
                        progressBar.setVisibility(View.GONE);
                    }
                    if (matcherPassword.matches()) {
                        user.updatePassword(newPassword.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ChangePasswordActivity.this, "Password is Updated, Sign In with New Password", Toast.LENGTH_SHORT).show();
                                            auth.signOut();
                                            startActivity(new Intent(ChangePasswordActivity.this, SignInActivity.class));
                                            progressBar.setVisibility(View.GONE);
                                            finish();
                                        } else {
                                            Toast.makeText(ChangePasswordActivity.this, "Failed to Update Password", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    } else {
                        newPassword.setError("Enter Valid Password");
                        progressBar.setVisibility(View.GONE);
                    }
                } else if (newPassword.getText().toString().trim().equals("")) {
                    newPassword.setError("Enter Password");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
