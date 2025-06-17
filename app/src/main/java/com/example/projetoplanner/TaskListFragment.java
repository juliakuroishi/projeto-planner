package com.example.projetoplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class TaskListFragment extends Fragment { // Precisa estender Fragment

    // Opcional: Você pode adicionar um binding aqui se tiver um layout XML específico para ele
    // private FragmentTaskListBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Infla o layout para este fragmento
        // Substitua R.layout.fragment_task_list pelo nome correto do seu arquivo XML de layout para TaskList
        return inflater.inflate(R.layout.fragment_task_list, container, false);
        // Se você usar View Binding, seria assim:
        // binding = FragmentTaskListBinding.inflate(inflater, container, false);
        // return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Aqui você adicionaria a lógica para exibir as tarefas, botões, etc.
    }

    // Se usar View Binding, adicione o onDestroyView
    // @Override
    // public void onDestroyView() {
    //     super.onDestroyView();
    //     binding = null;
    // }
}