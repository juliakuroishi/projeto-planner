package com.example.projetoplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable; // Import para @Nullable
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

// Importar a classe de binding correta para fragment_edit_task.xml
import com.example.projetoplanner.databinding.FragmentEditTaskBinding; // Verifique o nome exato da classe gerada

public class EditTaskFragment extends Fragment {

    private FragmentEditTaskBinding binding; // Variável para o View Binding

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Infla o layout para este fragmento usando View Binding
        binding = FragmentEditTaskBinding.inflate(inflater, container, false);
        return binding.getRoot(); // Retorna a View raiz do layout
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // AQUI VOCÊ ADICIONARÁ A LÓGICA DO SEU EDITTASKFRAGMENT
        // (Ex: obter argumentos, preencher campos, configurar listeners de botões de salvar/cancelar)

        // Exemplo: Se você tiver um botão de salvar no seu layout com id 'button_save_task'
        /*
        binding.buttonSaveTask.setOnClickListener(v -> {
            // Lógica para salvar ou atualizar a tarefa
            // NavHostFragment.findNavController(EditTaskFragment.this)
            //         .navigate(R.id.action_editTaskFragment_to_taskListFragment); // Voltar para a lista
        });
        */

        // Lógica para obter o argumento taskId, se houver
        // if (getArguments() != null) {
        //     int taskId = EditTaskFragmentArgs.fromBundle(getArguments()).getTaskId();
        //     if (taskId != -1) {
        //         // É modo de edição, carregue a tarefa
        //     } else {
        //         // É modo de adição de nova tarefa
        //     }
        // }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpa a referência do binding para evitar vazamentos de memória
        binding = null;
    }
}