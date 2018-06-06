package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import clouwiko.dev.prasiku.R;
import clouwiko.dev.prasiku.activity.model.User;

public class ChangePasswordActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AuthCredential credential;
    private ProgressBar progressBar;
    private EditText currentPassword, newPassword;
    private TextView helpPassword;
    private Button btnChangePassword;
    private DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //Get Firebase Auth Instance
        auth = FirebaseAuth.getInstance();

        //Get Current User
        user = FirebaseAuth.getInstance().getCurrentUser();

        //Get Current User Id
        String userId = getIntent().getStringExtra("userId");

        //Database Reference
        databaseUsers = FirebaseDatabase.getInstance().getReference().child(userId);

        currentPassword = findViewById(R.id.current_password);
        newPassword = findViewById(R.id.new_password);
        helpPassword = findViewById(R.id.help_password);
        Typeface help_password = Typeface.createFromAsset(getAssets(), "fonts/segoeuil.ttf");
        helpPassword.setTypeface(help_password);
        btnChangePassword = findViewById(R.id.action_change_password);

        progressBar = findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        helpPassword.setVisibility(View.GONE);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpPassword.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                String userEmail = user.getEmail();
                String userCurrentPassword = currentPassword.getText().toString().trim();
                String userNewPassword = newPassword.getText().toString().trim();
                String validPassword = "^(?=\\S+$).{4,}$";
                if (userCurrentPassword.isEmpty()) {
                    currentPassword.setError("Enter Your Current Password");
                    progressBar.setVisibility(View.GONE);
                    return;
                } else {

                }
                Matcher matcherCurrentPassword = Pattern.compile(validPassword).matcher(userCurrentPassword);
                if (matcherCurrentPassword.matches()) {

                } else {
                    currentPassword.setError("Enter Valid Password");
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (userNewPassword.isEmpty()) {
                    helpPassword.setVisibility(View.VISIBLE);
                    newPassword.setError("Enter Your New Password");
                    progressBar.setVisibility(View.GONE);
                    return;
                } else {

                }
                if (userNewPassword.length() < 6) {
                    helpPassword.setVisibility(View.VISIBLE);
                    newPassword.setError("Password too Short, Enter Minimum 6 Characters");
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                Matcher matcherNewPassword = Pattern.compile(validPassword).matcher(userNewPassword);
                if (matcherNewPassword.matches()) {

                } else {
                    helpPassword.setVisibility(View.VISIBLE);
                    newPassword.setError("Enter Valid Password");
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                credential = EmailAuthProvider.getCredential(userEmail, userCurrentPassword);
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    String userNewPassword = newPassword.getText().toString().trim();
                                    user.updatePassword(userNewPassword)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(), "Password is Updated, Sign In with New Password", Toast.LENGTH_SHORT).show();
                                                        auth.signOut();
                                                        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                                        progressBar.setVisibility(View.GONE);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Failed to Update Password", Toast.LENGTH_SHORT).show();
                                                        progressBar.setVisibility(View.GONE);
                                                        startActivity(getIntent());
                                                    }
                                                }
                                            });
                                } else {
                                    currentPassword.setError("Your Current Password is Wrong");
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
    }
}
