package com.example.android.cs519_pms.registration;

import android.os.Bundle;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.android.cs519_pms.R;
import com.example.android.cs519_pms.database.Constants;
import com.example.android.cs519_pms.database.RequestHandler;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_SignUp#} factory method to
 * create an instance of this fragment.
 */
public class Fragment_SignUp extends Fragment {

    private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;
    private ProgressBar progressBar;

    public Fragment_SignUp() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reg_signup, container, false);
        alreadyHaveAnAccount = view.findViewById(R.id.tv_sign_in);
        progressBar = view.findViewById(R.id.sign_up_progressBar);
        parentFrameLayout = requireActivity().findViewById(R.id.registration_frameLayout);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyHaveAnAccount.setOnClickListener(view1 -> setFragment());

        Button btn_sign_up = view.findViewById(R.id.sign_up_btn);
        btn_sign_up.setOnClickListener(view1 -> {
            TextInputEditText mName = view.findViewById(R.id.sign_up_name);
            String name = mName.getText().toString().trim();
            TextInputEditText mMobile = view.findViewById(R.id.sign_up_mobile);
            String mobile = mMobile.getText().toString().trim();
            TextInputEditText mLongitude = view.findViewById(R.id.sign_up_longitude);
            String longitude = mLongitude.getText().toString().trim();
            TextInputEditText mLatitude = view.findViewById(R.id.sign_up_latitude);
            String latitude = mLatitude.getText().toString().trim();
            TextInputEditText mEmail = view.findViewById(R.id.sign_up_email);
            String email = mEmail.getText().toString().trim();
            TextInputEditText mPass = view.findViewById(R.id.sign_up_password);
            String password = mPass.getText().toString().trim();
            TextInputEditText mPass2 = view.findViewById(R.id.sign_up_re_password);
            String password2 = mPass2.getText().toString().trim();
            if(name.isEmpty()||mobile.isEmpty()||email.isEmpty()
                    ||longitude.isEmpty()||latitude.isEmpty()
                    ||password.isEmpty()||password2.isEmpty()||!password.equals(password2)){
                Toast.makeText(requireActivity(),
                        "Error : please check all input fields", Toast.LENGTH_LONG).show();
                return;
            }
                registerUser(name, mobile, longitude, email, password);
        });
    }

    private void setFragment() {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_left,R.anim.slide_out_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(), new Fragment_SignIn());
        fragmentTransaction.commit();
    }

    private void registerUser(String name, String mobile, String location, String email, String password) {
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Toast.makeText(requireActivity(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }, error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("mobile", mobile);
                params.put("location", location);
                params.put("email", email);
                params.put("pass", password);
                return params;
            }
        };

        //Singleton Request handler with StringRequest.
        RequestHandler.getInstance(requireActivity()).addToRequestQueue(stringRequest);
    }

}