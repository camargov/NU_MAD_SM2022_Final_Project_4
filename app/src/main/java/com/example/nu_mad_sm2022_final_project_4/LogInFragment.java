package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;

public class LogInFragment extends Fragment implements View.OnClickListener {
    private IAddFragment fragmentListener;
    private IToastFromFragmentToMain toastListener;

    // UI Elements
    private EditText editTextUsername, editTextPassword;
    private Button buttonLogIn;

    // Firebase-related items
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public LogInFragment() {}

    public static LogInFragment newInstance() {
        LogInFragment fragment = new LogInFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        getActivity().setTitle("Log In");

        // Defining UI Elements
        editTextUsername = view.findViewById(R.id.login_editText_username);
        editTextPassword = view.findViewById(R.id.login_editText_password);
        buttonLogIn = view.findViewById(R.id.login_button);
        buttonLogIn.setClickable(true);
        buttonLogIn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof IToastFromFragmentToMain) {
            toastListener = (IToastFromFragmentToMain) context;
        }
        if (context instanceof  IAddFragment) {
            fragmentListener = (IAddFragment) context;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_button) {
            if (editTextUsername.getText().toString().equals("")
                    || editTextPassword.getText().toString().equals("")) {
                toastListener.toastFromFragment("Username and Password must not be empty.");
            }
            else {
                buttonLogIn.setClickable(false);
                getEmail(editTextUsername.getText().toString());
            }
        }
    }

    // Checking if username exists in the database and logging-in user if true
    private void getEmail(String username) {
        DocumentReference docRef;
        db.collection("users")
                .whereEqualTo("displayName", username)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        QueryDocumentSnapshot chosenSnapshot = null;
                        for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots) {
                            chosenSnapshot = snapshot;
                        }
                        if (chosenSnapshot != null) {
                            db.collection("users")
                                    .document(chosenSnapshot.getId())
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            loginUser(documentSnapshot.getString("email"));
                                        }
                                    });
                        }
                        else {
                            toastListener.toastFromFragment("Incorrect username and/or password. Please try again.");
                            buttonLogIn.setClickable(true);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        toastListener.toastFromFragment("Username does not exist. Please try again");
                    }
                });
    }

    // Logging-in the user using the given email and the password from the EditText field
    private void loginUser(String email) {
        mAuth.signInWithEmailAndPassword(email, editTextPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Runnable onSyncComplete = () -> fragmentListener.addCreatePaletteOptionsFragment();
                            try {
                                Utils.syncLocalPaletteDataToCloud(getActivity(), onSyncComplete, onSyncComplete);
                            } catch (IOException e) {
                                onSyncComplete.run();
                            }
                        }
                        else {
                            toastListener.toastFromFragment("Incorrect password. Please try again.");
                            buttonLogIn.setClickable(true);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        toastListener.toastFromFragment("Incorrect password. Please try again.");
                        buttonLogIn.setClickable(true);
                    }
                });
    }
}