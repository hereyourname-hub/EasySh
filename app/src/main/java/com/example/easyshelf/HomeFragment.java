package com.example.easyshelf;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.easyshelf.R; // Replace with the correct package name for your project

public class HomeFragment extends Fragment {

    private Button btnLogin;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the button
        btnLogin = rootView.findViewById(R.id.btn_login);

        // Set an OnClickListener for the button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to open the "activity_frame18" activity
                Intent intent = new Intent(getActivity(), Frame18.class); // Replace with the correct activity name

                // Start the new activity
                startActivity(intent);
            }
        });

        return rootView;
    }
}
