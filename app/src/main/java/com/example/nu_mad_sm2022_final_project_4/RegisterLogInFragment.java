package com.example.nu_mad_sm2022_final_project_4;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class RegisterLogInFragment extends Fragment implements View.OnClickListener {
    private Button buttonRegister, buttonLogIn;
    private IAddFragment fragmentListener;

    public RegisterLogInFragment() {}

    public static RegisterLogInFragment newInstance() {
        RegisterLogInFragment fragment = new RegisterLogInFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_log_in, container, false);
        getActivity().setTitle("Patches");
        buttonRegister = view.findViewById(R.id.onboarding_button_register);
        buttonRegister.setOnClickListener(this);
        buttonLogIn = view.findViewById(R.id.onboarding_button_login);
        buttonLogIn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IAddFragment) {
            fragmentListener = (IAddFragment) context;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.onboarding_button_register) {
            fragmentListener.addRegisterFragment();
        }
        else if (v.getId() == R.id.onboarding_button_login) {
            fragmentListener.addLogInFragment();
        }
    }
}