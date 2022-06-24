package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

public class RegisterFragment extends Fragment implements View.OnClickListener {
    private IAddFragment fragmentListener;
    private IToastFromFragmentToMain toastListener;

    // UI ELements
    private EditText editTextUsername, editTextEmail,
            editTextPassword, editTextRepeatPassword;
    private Button buttonRegister;
    private ImageView backButton;

    // Firebase-related items
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser mUser;
    private String cloudImagePath;
    private boolean photoTaken;

    public RegisterFragment() {}

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing Firebase Firestore and Authentication
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        cloudImagePath = String.format("profiles/%d.jpg", System.currentTimeMillis());
        photoTaken = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        getActivity().setTitle("Register");

        // Defining UI Elements
        editTextUsername = view.findViewById(R.id.register_editText_username);
        editTextEmail = view.findViewById(R.id.register_editText_email);
        editTextPassword = view.findViewById(R.id.register_editText_password);
        editTextRepeatPassword = view.findViewById(R.id.register_editText_passwordConfirm);
        buttonRegister = view.findViewById(R.id.register_button);
        buttonRegister.setClickable(true);
        buttonRegister.setOnClickListener(this);
        backButton = view.findViewById(R.id.register_imageView_backbutton);
        backButton.setClickable(true);
        backButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof IAddFragment) {
            fragmentListener = (IAddFragment) context;
        }
        if (context instanceof IToastFromFragmentToMain) {
            toastListener = (IToastFromFragmentToMain) context;
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.register_imageView_backbutton){
            toggleClickable(false);
            fragmentListener.addOnboardingFragment();
        }
        else if (v.getId() == R.id.register_button) {
            if (!emptyField() && validPassword()) {
                toggleClickable(false);
                mAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mUser = task.getResult().getUser();
                                    registerUser();
                                }
                                else {
                                    toastListener.toastFromFragment("Username already exists. Please try again.");
                                    toggleClickable(true);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                toastListener.toastFromFragment("Check username and password. Please try again.");
                                toggleClickable(true);
                            }
                        });
            }
        }
    }

    // Checking that the user left no fields empty
    private boolean emptyField() {
        if (editTextUsername.getText().toString().equals("")) {
            toastListener.toastFromFragment("Username field must be filled out");
            return true;
        }
        else if (editTextEmail.getText().toString().equals("")) {
            toastListener.toastFromFragment("Email field must be filled out");
            return true;
        }
        else if (editTextPassword.getText().toString().equals("")) {
            toastListener.toastFromFragment("Password field must be filled out");
            return true;
        }
        else if (editTextRepeatPassword.getText().toString().equals("")) {
            toastListener.toastFromFragment("Repeat Password field must be filled out");
            return true;
        }
        else {
            return false;
        }
    }

    // Checking that the user entered their password correctly twice
    private boolean validPassword() {
        if (editTextPassword.getText().toString().equals(editTextRepeatPassword.getText().toString())) {
            return true;
        }
        else {
            toastListener.toastFromFragment("Password must be the same for both fields.");
            return false;
        }
    }

    // Registering the given user
    private void registerUser() {
        // UPDATE IMAGE PROFILE PHOTO
        // Adding username to the FirebaseUser
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(editTextUsername.getText().toString())
                .build();
        mUser.updateProfile(profileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Updating database with user's information
                            User userObj = new User(
                                    editTextUsername.getText().toString(),
                                    editTextEmail.getText().toString());
                            db.collection("users")
                                    .document()
                                    .set(userObj)
                                    .addOnCompleteListener(task1 -> {
                                        Runnable onSyncComplete = () -> fragmentListener.addCreatePaletteOptionsFragment();
                                        try {
                                            Utils.syncLocalPaletteDataToCloud(getActivity(), onSyncComplete, onSyncComplete);
                                        } catch (IOException e) {
                                            onSyncComplete.run();
                                        }
                                    });
                        }
                    }
                });
    }

    private void toggleClickable(boolean clickable){
        backButton.setClickable(clickable);
        buttonRegister.setClickable(clickable);
        editTextEmail.setClickable(clickable);
        editTextPassword.setClickable(clickable);
        editTextUsername.setClickable(clickable);
        editTextRepeatPassword.setClickable(clickable);
    }
}