package clouwiko.dev.prasiku.activity.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import clouwiko.dev.prasiku.R;

public class SignInActivity extends AppCompatActivity {

    private TextView title;
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignIn, btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Get Firebase Auth Instance
        auth = FirebaseAuth.getInstance();

        title = findViewById(R.id.signin_title);
        Typeface facetitles = Typeface.createFromAsset(getAssets(),"fonts/segoeuisl.ttf");
        title.setTypeface(facetitles);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignIn = (Button) findViewById(R.id.action_sign_in_button);
        Typeface facesignin = Typeface.createFromAsset(getAssets(),"fonts/segoeuil.ttf");
        btnSignIn.setTypeface(facesignin);
        btnResetPassword = (Button) findViewById(R.id.intent_reset_password_button);
        Typeface facesigninreset = Typeface.createFromAsset(getAssets(),"fonts/segoeuil.ttf");
        btnResetPassword.setTypeface(facesigninreset);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //Authenticate User
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    //There was an Error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(SignInActivity.this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    loadUserInformation();
                                }
                            }
                        });
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ResetPasswordActivity.class));
                finish();
            }
        });
    }

    private void loadUserInformation() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.isEmailVerified()) {
            Intent intent = new Intent(SignInActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SignInActivity.this, VerificationActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //Get Current User
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startActivity(new Intent(SignInActivity.this, LandingActivity.class));
            finish();
        }
    }
}