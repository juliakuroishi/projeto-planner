package com.example.projetoplanner;

import android.app.AlertDialog; // Importação adicionada para AlertDialog
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projetoplanner.databinding.FragmentEditTaskBinding;
import com.example.projetoplanner.database.PlannerDatabase;
import com.example.projetoplanner.dao.TaskDao;
import com.example.projetoplanner.entities.Task;

import java.util.concurrent.Executors;

public class EditTaskFragment extends Fragment {

    private FragmentEditTaskBinding binding;
    private TaskDao taskDao;
    private int taskId = -1;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentEditTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PlannerDatabase db = PlannerDatabase.getDatabase(requireContext());
        taskDao = db.taskDao();

        // 1. Checar se estamos editando uma tarefa existente
        if (getArguments() != null) {
            taskId = getArguments().getInt("taskId", -1);
        }

        if (taskId != -1) {
            binding.textViewEditTaskTitle.setText("Editar Tarefa");

            binding.buttonDeleteTask.setVisibility(View.VISIBLE);
            Executors.newSingleThreadExecutor().execute(() -> {
                Task taskToEdit = taskDao.getTaskByIdSync(taskId);
                requireActivity().runOnUiThread(() -> {
                    if (taskToEdit != null) {
                        binding.editTextTaskDescription.setText(taskToEdit.getDescricao()); //
                        binding.checkBoxTaskCompleted.setChecked(taskToEdit.getStatus()); //
                    } else {
                        Toast.makeText(getContext(), "Tarefa não encontrada.", Toast.LENGTH_SHORT).show(); //
                        NavHostFragment.findNavController(EditTaskFragment.this).popBackStack();
                    }
                });
            });
        } else {

            binding.textViewEditTaskTitle.setText("Nova Tarefa"); //
            binding.buttonDeleteTask.setVisibility(View.GONE); //
        }

        // 2. Botão Salvar
        binding.buttonSaveTask.setOnClickListener(v -> {
            String description = binding.editTextTaskDescription.getText().toString().trim(); //
            boolean isCompleted = binding.checkBoxTaskCompleted.isChecked(); //

            if (description.isEmpty()) {
                Toast.makeText(getContext(), "A descrição da tarefa não pode estar vazia.", Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                if (taskId == -1) {
                    Task newTask = new Task(description, isCompleted);
                    long newId = taskDao.insertTask(newTask);
                    requireActivity().runOnUiThread(() -> {
                        if (newId > 0) {
                            Toast.makeText(getContext(), "Tarefa adicionada!", Toast.LENGTH_SHORT).show();
                            NavHostFragment.findNavController(EditTaskFragment.this).popBackStack();
                        } else {
                            Toast.makeText(getContext(), "Erro ao adicionar tarefa.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Atualizar tarefa existente
                    Task taskToUpdate = new Task(description, isCompleted); //
                    taskToUpdate.id = taskId; // Definir o ID para a atualização
                    int updatedRows = taskDao.updateTask(taskToUpdate); //
                    requireActivity().runOnUiThread(() -> {
                        if (updatedRows > 0) {
                            Toast.makeText(getContext(), "Tarefa atualizada!", Toast.LENGTH_SHORT).show();
                            NavHostFragment.findNavController(EditTaskFragment.this).popBackStack();
                        } else {
                            Toast.makeText(getContext(), "Erro ao atualizar tarefa.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        });

        // 3. Botão Cancelar
        binding.buttonCancel.setOnClickListener(v -> {
            NavHostFragment.findNavController(EditTaskFragment.this).popBackStack();
        });

        // 4. Botão Excluir (apenas visível ao editar)
        binding.buttonDeleteTask.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Excluir Tarefa")
                    .setMessage("Tem certeza que deseja excluir esta tarefa?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            Task taskToDelete = new Task("", false);
                            taskToDelete.id = taskId;
                            int deletedRows = taskDao.deleteTask(taskToDelete);
                            requireActivity().runOnUiThread(() -> {
                                if (deletedRows > 0) {
                                    Toast.makeText(getContext(), "Tarefa excluída!", Toast.LENGTH_SHORT).show();
                                    NavHostFragment.findNavController(EditTaskFragment.this).popBackStack();
                                } else {
                                    Toast.makeText(getContext(), "Erro ao excluir tarefa.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}