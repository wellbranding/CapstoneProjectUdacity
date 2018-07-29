package udacityteam.healthapp.activities;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import udacityteam.healthapp.Model.Result;
import udacityteam.healthapp.Model.UserRetrofitGood;
import udacityteam.healthapp.Model.Userretrofit;
import udacityteam.healthapp.PHP_Retrofit_API.APIService;
import udacityteam.healthapp.PHP_Retrofit_API.APIUrl;
import udacityteam.healthapp.R;


public class LoginWithMailFragment extends Fragment implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    public static UserRetrofitGood currentuser;

    // [START declare_auth]
    private FirebaseAuth mAuth;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    LoginRegisterViewModel viewModel;
    // [END declare_auth]


    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(LoginRegisterViewModel.class);
        super.onCreate(savedInstanceState);

        // Views

        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login_with_mail_pasword,
                container, false);
        mStatusTextView = view.findViewById(R.id.status);
        mDetailTextView = view.findViewById(R.id.detail);
        mEmailField = view.findViewById(R.id.field_email);
        mPasswordField = view.findViewById(R.id.field_password);

        view.findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
     view.findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
    view.findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);

        view.findViewById(R.id.email_sign_in_button).setOnClickListener(this);

        return view;
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void signIn(final String email, final String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

     //   showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser user = mAuth.getCurrentUser();
                            // Sign in success, update UI with the signed-in user's information
                            if(user!=null  && user.isEmailVerified()) {


                                //Defining the user object as we need to pass it with the call
                                Userretrofit retrofituser = new Userretrofit(user.getDisplayName(), user.getEmail(), mAuth.getCurrentUser().getUid());
                                viewModel.getRegisterWithGoogleSignInResponse(retrofituser).observe(requireActivity(),
                                        result ->
                                        {
                                            if(result!=null)
                                                if(!result.getError())
                                                {
                                                    Intent intent = new Intent(requireActivity(), MainActivity.class);
                                                    startActivity(intent);
                                                    requireActivity().finish();
                                                }
                                                else
                                                {
                                                    Toast.makeText(requireActivity(), result.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                        });

                            }
                            else
                            {
                                Toast.makeText(requireActivity(), "User Email is not verified", Toast.LENGTH_SHORT).show();
                            }



                            updateUI(user);
                        } else {
                            updateUI(null);
                        }
                        if (!task.isSuccessful()) {
                            mStatusTextView.setText("failed");
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
       // hideProgressDialog();
        if (user != null) {
            if(user.isEmailVerified())
            {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        }
            else {
            mStatusTextView.setText("sitnout");
            mDetailTextView.setText(null);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
       if (i == R.id.email_sign_in_button) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }
}
