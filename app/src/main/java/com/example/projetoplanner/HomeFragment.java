package com.example.projetoplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projetoplanner.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.btnEntrar.setOnClickListener(v ->
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_homeFragment_to_loginFragment)
        );

        binding.btnCadastrar.setOnClickListener(v ->
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_homeFragment_to_cadastroFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}