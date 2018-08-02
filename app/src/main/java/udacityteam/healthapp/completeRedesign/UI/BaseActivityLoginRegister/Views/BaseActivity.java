package udacityteam.healthapp.completeRedesign.UI.BaseActivityLoginRegister.Views;

import android.app.FragmentManager;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import udacityteam.healthapp.R;
import udacityteam.healthapp.completeRedesign.Data.Networking.Models.Userretrofit;
import udacityteam.healthapp.completeRedesign.UI.BaseActivityLoginRegister.ViewModels.LoginRegisterViewModel;
import udacityteam.healthapp.completeRedesign.UI.MainActivity.Views.MainActivity;

public class BaseActivity extends AppCompatActivity implements
        View.OnClickListener, RegisterWithMailFragment.RegisterSuccessListener {
    private static final int RC_SIGN_IN = 1000;
    private static final String BACK_STACK_ROOT_TAG_LOGIN = "login";

    private FirebaseAuth mAuth;


    private GoogleSignInClient mGoogleSignInClient;

    @Inject
    ViewModelProvider.Factory ViewModelFactory;

    @Inject
    DispatchingAndroidInjector<android.support.v4.app.Fragment> dispatchingAndroidInjector;

    LoginRegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1025887070439-pa8ivq2h24eigj8vv8h66e43ng7fgefh.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_home);
        findViewById(R.id.google_sign_in).setOnClickListener(this);
        viewModel = ViewModelProviders.of(this, ViewModelFactory).
                get(LoginRegisterViewModel.class);

        Button registermail = findViewById(R.id.mailregister);
        Button loginmail = findViewById(R.id.mailogin);


        registermail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterWithMailFragment fragment = new RegisterWithMailFragment();
                android.support.v4.app.FragmentManager fragmentManager = BaseActivity.this.getSupportFragmentManager();
                fragmentManager.popBackStack(BACK_STACK_ROOT_TAG_LOGIN, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction().
                        replace(R.id.fragmentContainer, fragment)
                        .addToBackStack(BACK_STACK_ROOT_TAG_LOGIN)
                        .commit();
                fragmentManager.executePendingTransactions();
            }
        });
        loginmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginWithMailFragment fragment = new LoginWithMailFragment();
                android.support.v4.app.FragmentManager fragmentManager = BaseActivity.this.getSupportFragmentManager();
                fragmentManager.popBackStack(BACK_STACK_ROOT_TAG_LOGIN, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentManager.beginTransaction().
                        replace(R.id.fragmentContainer, fragment)
                        .addToBackStack(BACK_STACK_ROOT_TAG_LOGIN)
                        .commit();
                fragmentManager.executePendingTransactions();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        determineCurrentState(currentUser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                authWithGoogleUsingFirebase(account);
            } catch (ApiException e) {
                determineCurrentState(null);
            }
        }
    }

    private void authWithGoogleUsingFirebase(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        final FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Userretrofit retrofituser = new Userretrofit(user.getDisplayName(), user.getEmail(), mAuth.getCurrentUser().getUid());
                            viewModel.getRegisterWithGoogleSignInResponse(retrofituser).observe(this, result ->
                            {
                                if (result != null)
                                    if (!result.getError()) {
                                        Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else
                                        Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();

                            });

                        } else {
                            determineCurrentState(null);
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void determineCurrentState(FirebaseUser user) {
        if (user != null && user.isEmailVerified()) {
            Intent intent = new Intent(BaseActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.google_sign_in) {
            signIn();
        }

    }

    @Override
    public void onUserRegistered() {
        LoginWithMailFragment fragment = new LoginWithMailFragment();
        android.support.v4.app.FragmentManager fragmentManager = BaseActivity.this.getSupportFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG_LOGIN, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().
                replace(R.id.fragmentContainer, fragment)
                .commit();
        fragmentManager.executePendingTransactions();
    }
}


