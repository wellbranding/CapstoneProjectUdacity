package udacityteam.healthapp.completeRedesign.UI.BaseActivityLoginRegister.Views;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import udacityteam.healthapp.completeRedesign.Data.Networking.Models.Userretrofit;
import udacityteam.healthapp.R;
import udacityteam.healthapp.completeRedesign.UI.MainActivity.Views.MainActivity;
import udacityteam.healthapp.completeRedesign.UI.BaseActivityLoginRegister.ViewModels.LoginRegisterViewModel;


public class LoginWithMailFragment extends Fragment implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private EditText mEditTextEmail;
    private EditText mEditTextPassword;

    private FirebaseAuth mAuth;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    LoginRegisterViewModel viewModel;


    @Override
    public void onAttach(Context context) {
       // AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(LoginRegisterViewModel.class);
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login_with_mail_pasword,
                container, false);
        mEditTextEmail = view.findViewById(R.id.email_field_edit_text);
        mEditTextPassword = view.findViewById(R.id.password_field_edit_text);

        view.findViewById(R.id.email_sign_in_button).setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        determineCurrentState(currentUser);
    }


    private void signIn(final String email, final String password) {
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null  && user.isEmailVerified()) {

                                Userretrofit retrofituser;
                                if(user.getDisplayName()==null)
                                {
                                    retrofituser = new Userretrofit("unknown", user.getEmail(), mAuth.getCurrentUser().getUid());
                                }
                             else   retrofituser = new Userretrofit(user.getDisplayName(), user.getEmail(), mAuth.getCurrentUser().getUid());

                                viewModel.getRegisterWithGoogleSignInResponse(retrofituser).observe(requireActivity(),
                                        result ->
                                        {
                                            if(result!=null)
                                                if(!result.getError() || result.getMessage().equals(""))
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
                            determineCurrentState(user);
                        } else {
                            Toast.makeText(requireActivity(), "You need to verify your email", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEditTextEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(requireActivity(), R.string.email_required_error, Toast.LENGTH_SHORT).show();
            valid = false;
        }

        String password = mEditTextPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(requireActivity(), R.string.password_required_error, Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    private void determineCurrentState(FirebaseUser user) {
        if (user != null) {
            if(user.isEmailVerified())
            {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
       if (i == R.id.email_sign_in_button) {
            signIn(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString());
        }
    }
}
