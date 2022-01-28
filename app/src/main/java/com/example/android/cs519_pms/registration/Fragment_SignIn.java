package com.example.android.cs519_pms.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.android.cs519_pms.R;
import com.example.android.cs519_pms.database.Constants;
import com.example.android.cs519_pms.database.RequestHandler;
import com.example.android.cs519_pms.database.SharedPrefManager;
import com.example.android.cs519_pms.user_admin.AdminActivity;
import com.example.android.cs519_pms.user_customer.CustomerActivity;
import com.example.android.cs519_pms.user_pharmacy.PharmacyActivity;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_SignIn#} factory method to
 * create an instance of this fragment.
 */
public class Fragment_SignIn extends Fragment {
    private TextView notHaveAnAccount;
    private FrameLayout parentFrameLayout;
    private ProgressBar progressBar;

    public Fragment_SignIn() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reg_signin, container, false);
        notHaveAnAccount = view.findViewById(R.id.tv_sign_up);
        parentFrameLayout = requireActivity().findViewById(R.id.registration_frameLayout);
        progressBar = view.findViewById(R.id.sign_in_progressBar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notHaveAnAccount.setOnClickListener(view1 -> setFragment());
        Button btn_sign_In = view.findViewById(R.id.sign_in_btn);
        btn_sign_In.setOnClickListener(view1 -> {
            TextInputEditText mEmail = requireActivity().findViewById(R.id.sign_in_email);
            TextInputEditText mPassword = requireActivity().findViewById(R.id.sign_in_password);
            final String email = Objects.requireNonNull(mEmail.getText()).toString().trim();
            final String password = Objects.requireNonNull(mPassword.getText()).toString().trim();
            if (!email.isEmpty() && !password.isEmpty()) {
                userSignIn(email, password);
            } else {
                Toast.makeText(requireActivity(),
                        "Email or Password incorrect.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setFragment() {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(), new Fragment_SignUp());
        fragmentTransaction.commit();
    }

    private void userSignIn(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean("error")) {
                            SharedPrefManager.getInstance(requireActivity().getApplicationContext()).userLogin(jsonObject.getInt("userType"),
                                    jsonObject.getInt("id"), jsonObject.getString("name"),
                                    jsonObject.getString("number"), jsonObject.getString("location"));
                            Toast.makeText(
                                    requireActivity(),
                                    "Sign In Successful!",
                                    Toast.LENGTH_LONG).show();
                            dashboardActivity(getActivity());
                        } else {
                            Toast.makeText(
                                    requireActivity(),
                                    jsonObject.getString("message"),
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.d("myTag", "This is my message error exception"+error.getMessage());
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("pass", password);
                return params;
            }
        };

        //Singleton Request handler with StringRequest.
        RequestHandler.getInstance(requireActivity()).addToRequestQueue(stringRequest);

    }

    private void dashboardActivity(Context context) {
        if (SharedPrefManager.getInstance(requireActivity()).getUserType() == 1) {
            Intent intent = new Intent(context, AdminActivity.class);
            startActivity(intent);
            requireActivity().finish();
        } else if (SharedPrefManager.getInstance(requireActivity()).getUserType() == 2) {
            Intent intent = new Intent(context, PharmacyActivity.class);
            startActivity(intent);
            requireActivity().finish();
        } else if (SharedPrefManager.getInstance(requireActivity()).getUserType() == 3) {
            Intent intent = new Intent(context, CustomerActivity.class);
            startActivity(intent);
            requireActivity().finish();
        }    }
}