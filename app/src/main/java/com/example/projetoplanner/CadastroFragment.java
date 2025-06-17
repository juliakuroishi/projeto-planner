package com.example.projetoplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast; // Para mensagens rápidas

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projetoplanner.databinding.FragmentCadastroBinding;
import com.example.projetoplanner.database.PlannerDatabase; // Importe seu banco de dados
import com.example.projetoplanner.entities.User; // Importe sua entidade User
import com.example.projetoplanner.dao.UserDao; // Importe seu DAO de usuário
import com.example.projetoplanner.utils.PasswordUtils; // Importe a classe utilitária de senha

// Para executar operações de DB em thread separada
import java.util.concurrent.Executors;

public class CadastroFragment extends Fragment {

    private FragmentCadastroBinding binding;
    private UserDao userDao; // Instância do DAO

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

        // Inicializa o DAO (Faça isso em uma ViewModel ou repositório em um projeto maior)
        PlannerDatabase db = PlannerDatabase.getDatabase(requireContext());
        userDao = db.userDao();

        // **Correção do tools:context no fragment_cadastro.xml:**
        // Abra fragment_cadastro.xml e mude:
        // tools:context=".LoginFragment"
        // Para:
        // tools:context=".CadastroFragment"

        // **Melhoria dos IDs dos EditTexts em fragment_cadastro.xml:**
        // Considere renomear editTextText para editTextUsername e editTextTextPassword para editTextPassword
        // Se você renomear, ajuste as linhas abaixo de acordo.

        binding.btnCriarConta.setOnClickListener(v -> {
            String username = binding.editTextText.getText().toString().trim();
            String password = binding.editTextTextPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Você também pode adicionar validação para verificar se o nome de usuário já existe
            Executors.newSingleThreadExecutor().execute(() -> {
                User existingUser = userDao.getUserByNome(username);
                if (existingUser != null) {
                    // A UI precisa ser atualizada na thread principal
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Nome de usuário já existe.", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // Gerar salt e hash da senha
                    String salt = PasswordUtils.generateSalt();
                    String passwordHash = PasswordUtils.hashPassword(password, salt);

                    if (passwordHash == null) {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Erro ao gerar hash da senha.", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }

                    // TODO: Implementar a funcionalidade de adicionar foto e obter a URI da foto
                    // Por enquanto, photoUri será nulo ou vazio
                    String photoUri = null; // Ou um valor padrão se não implementado ainda

                    // Criar novo usuário
                    User newUser = new User(username, passwordHash, salt, photoUri);

                    // Inserir usuário no banco de dados
                    long userId = userDao.insertUser(newUser);

                    // ... (restante do código) ...
                    requireActivity().runOnUiThread(() -> {
                        if (userId > 0) {
                            Toast.makeText(getContext(), "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                            // Navegar de volta para a tela de Login
                            NavHostFragment.findNavController(CadastroFragment.this).navigate(R.id.action_cadastroFragment_to_loginFragment); // Note: cadastroFragment e loginFragment (minúsculos)
                        } else {
                            Toast.makeText(getContext(), "Erro ao criar conta.", Toast.LENGTH_SHORT).show();
                        }
                    });
// ... (restante do código) ...

                }
            });
        });

        // Se você tiver um botão "Já tem conta? Entrar" no cadastro, adicione um Listener para ele
        // Exemplo:
        // binding.btnVoltarLogin.setOnClickListener(v -> {
        //     NavHostFragment.findNavController(CadastroFragment.this)
        //             .navigate(R.id.action_cadastroFragment_to_loginFragment);
        // });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}