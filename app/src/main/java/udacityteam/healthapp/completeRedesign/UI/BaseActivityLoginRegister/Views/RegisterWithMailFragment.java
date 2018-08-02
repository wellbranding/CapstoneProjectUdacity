package udacityteam.healthapp.completeRedesign.UI.BaseActivityLoginRegister.Views;


import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import udacityteam.healthapp.R;


public class RegisterWithMailFragment extends Fragment implements
        View.OnClickListener {

    private EditText mEmailField;
    private EditText mPasswordField;

    private FirebaseAuth mAuth;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (RegisterWithMailFragment.RegisterSuccessListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }

    RegisterWithMailFragment.RegisterSuccessListener mCallback;

    public interface RegisterSuccessListener {
        public void onUserRegistered();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_register_with_mail_pasword,
                container, false);
        mEmailField = view.findViewById(R.id.email_field_edit_text);
        mPasswordField = view.findViewById(R.id.password_field_edit_text);

        view.findViewById(R.id.register_account_with_name).setOnClickListener(this);
        view.findViewById(R.id.verify_email_button).setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        determineCurrentState(currentUser);
    }

    private void createAccount(final String email, final String password) {
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(requireActivity(), R.string.successful_regiser_verify_toast, Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        determineCurrentState(user);
                    } else {
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthWeakPasswordException e) {
                            Toast.makeText(requireActivity(), R.string.password_short_error,
                                    Toast.LENGTH_SHORT).show();

                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(requireActivity(), R.string.email_is_badly_formatted_error,
                                    Toast.LENGTH_SHORT).show();

                        } catch (FirebaseAuthUserCollisionException e) {
                            Toast.makeText(requireActivity(), R.string.email_is_already_in_use_error,
                                    Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        determineCurrentState(null);
                    }

                });
    }


    private void sendEmailVerification() {
        requireActivity().findViewById(R.id.verify_email_button).setEnabled(false);
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            requireActivity().findViewById(R.id.verify_email_button).setEnabled(true);

                            if (task.isSuccessful()) {
                                Toast.makeText(requireActivity(),
                                        String.format("%s%s", getString(R.string.verification_email_send_to), user.getEmail()),
                                        Toast.LENGTH_SHORT).show();
                                mCallback.onUserRegistered();
                            }
                        }
                    });
        } else {
            Toast.makeText(requireActivity(),
                    "Could not find a registration request",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            valid = false;
            Toast.makeText(requireActivity(), R.string.password_required_error, Toast.LENGTH_SHORT).show();
        }

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(requireActivity(), R.string.email_required_error, Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void determineCurrentState(FirebaseUser user) {
        if (user != null) {
            requireActivity().findViewById(R.id.verify_email_button).setVisibility(View.VISIBLE);

        }
        if (user != null && !user.isEmailVerified())
            requireActivity().findViewById(R.id.register_account_with_name).setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.register_account_with_name) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());

        } else if (i == R.id.verify_email_button) {
            sendEmailVerification();
        }
    }
}
