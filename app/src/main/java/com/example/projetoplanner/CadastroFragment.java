package com.example.projetoplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projetoplanner.databinding.FragmentCadastroBinding;
import com.example.projetoplanner.database.PlannerDatabase;
import com.example.projetoplanner.entities.User;
import com.example.projetoplanner.dao.UserDao;
import com.example.projetoplanner.utils.PasswordUtils;


import java.util.concurrent.Executors;

public class CadastroFragment extends Fragment {

    private FragmentCadastroBinding binding;
    private UserDao userDao;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentCadastroBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        PlannerDatabase db = PlannerDatabase.getDatabase(requireContext());
        userDao = db.userDao();


        binding.btnCriarConta.setOnClickListener(v -> {
            String username = binding.editTextText.getText().toString().trim();
            String password = binding.editTextPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                return;
            }


            Executors.newSingleThreadExecutor().execute(() -> {
                User existingUser = userDao.getUserByNome(username);
                if (existingUser != null) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Nome de usuário já existe.", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    String salt = PasswordUtils.generateSalt();
                    String passwordHash = PasswordUtils.hashPassword(password, salt);

                    if (passwordHash == null) {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Erro ao gerar hash da senha.", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }

                    String photoUri = null;


                    User newUser = new User(username, passwordHash, salt, photoUri);

                    // Inserir usuário no banco de dados
                    long userId = userDao.insertUser(newUser);


                    requireActivity().runOnUiThread(() -> {
                        if (userId > 0) {
                            Toast.makeText(getContext(), "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                            NavHostFragment.findNavController(CadastroFragment.this).navigate(R.id.action_cadastroFragment_to_loginFragment);
                        } else {
                            Toast.makeText(getContext(), "Erro ao criar conta.", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            });
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}