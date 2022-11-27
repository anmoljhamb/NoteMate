package com.example.notemate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.notemate.Fragments.DiscoverFragment;
import com.example.notemate.Fragments.DoubtFragment;
import com.example.notemate.Fragments.HomeFragment;
import com.example.notemate.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {



    ActivityHomeBinding binding;
    private final String TAG = "HomeActivity";
    FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if ( auth.getCurrentUser() == null )
        {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setCurrentFragment(new HomeFragment());

        binding.bottomNavigationView.setSelectedItemId(R.id.menuHome);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId() )
            {
                case R.id.menuHome:
                    setCurrentFragment(new HomeFragment());
                    break;
//                case R.id.menuDiscover:
//                    setCurrentFragment(new DiscoverFragment());
//                    break;
                case R.id.menuDoubt:
                    setCurrentFragment(new DoubtFragment());
                    break;
            }
            return true;
        });



        ImageView profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            finish();
        });
        
    }

    private void setCurrentFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameView, fragment);
        fragmentTransaction.commit();

    }
}