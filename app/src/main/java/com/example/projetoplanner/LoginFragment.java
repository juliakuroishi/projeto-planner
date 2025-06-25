package com.example.projetoplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projetoplanner.databinding.FragmentLoginBinding;
import com.example.projetoplanner.database.PlannerDatabase;
import com.example.projetoplanner.entities.User;
import com.example.projetoplanner.dao.UserDao;
import com.example.projetoplanner.utils.PasswordUtils;

import java.util.concurrent.Executors;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private UserDao userDao;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PlannerDatabase db = PlannerDatabase.getDatabase(requireContext());
        userDao = db.userDao();


        binding.btnEntrar.setOnClickListener(v -> {
            String username = binding.editTextText.getText().toString().trim();
            String password = binding.editTextPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                User user = userDao.getUserByNome(username);

                requireActivity().runOnUiThread(() -> {
                    if (user == null) {

                        Toast.makeText(getContext(), "Usuário não cadastrado.", Toast.LENGTH_SHORT).show();
                    } else {

                        if (PasswordUtils.verifyPassword(password, user.getPasswordHash(), user.getSalt())) {
                            //Toast.makeText(getContext(), "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                            NavHostFragment.findNavController(LoginFragment.this)
                                    .navigate(R.id.action_loginFragment_to_taskListFragment);
                        } else {
                            Toast.makeText(getContext(), "Senha incorreta.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            });
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}