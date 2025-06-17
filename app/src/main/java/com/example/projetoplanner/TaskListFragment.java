package com.example.projetoplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager; // Importação adicionada

import com.example.projetoplanner.databinding.FragmentTaskListBinding;
import com.example.projetoplanner.dao.TaskDao;
import com.example.projetoplanner.database.PlannerDatabase;
import com.example.projetoplanner.entities.Task;

import java.util.List;
import java.util.concurrent.Executors;

public class TaskListFragment extends Fragment {

    private FragmentTaskListBinding binding;
    private TaskDao taskDao;
    private TaskAdapter taskAdapter;
    // Removidas as declarações de Button e ListView como campos, pois são acessadas via binding e agora é um RecyclerView.

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentTaskListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Esconder o FAB da MainActivity quando este fragmento estiver visível
        if (getActivity() instanceof MainActivity) { //
            ((MainActivity) getActivity()).binding.fab.setVisibility(View.GONE); //
        }

        PlannerDatabase db = PlannerDatabase.getDatabase(requireContext()); //
        taskDao = db.taskDao(); //

        // Inicializa o adapter e o associa ao RecyclerView
        taskAdapter = new TaskAdapter(); //
        binding.listTask.setLayoutManager(new LinearLayoutManager(getContext())); //
        binding.listTask.setAdapter(taskAdapter); //

        // Observa as mudanças nas tarefas e atualiza a lista
        taskDao.getAllTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() { //
            @Override
            public void onChanged(List<Task> tasks) { //
                taskAdapter.submitList(tasks); //
                // Não exibir Toast se a lista estiver vazia, conforme pedido anterior.
                // Aqui você pode adicionar uma lógica para exibir uma "Empty State View" se desejar.
            }
        });

        // Configura os listeners dos botões e itens da lista
        setupListeners(); //
    }

    private void setupListeners() {
        // Listener para o FloatingActionButton de adicionar nova tarefa
        // Se preferir o btnAddTask, use binding.btnAddTask.setOnClickListener
        binding.fabAddTask.setOnClickListener(v -> adicionarTask()); //

        // Listener para o botão de edição em cada item da lista
        taskAdapter.setOnEditButtonClickListener(task -> { //
            // Navega para EditTaskFragment para editar a tarefa existente, passando o ID da tarefa.
            Bundle bundle = new Bundle(); //
            bundle.putInt("taskId", task.getId()); //
            NavHostFragment.findNavController(TaskListFragment.this).navigate(R.id.action_taskListFragment_to_editTaskFragment, bundle); //
        });

        // Listener para a mudança de estado do CheckBox dentro de cada item da lista
        taskAdapter.setOnCheckboxChangeListener((task, isChecked) -> { //
            task.setStatus(isChecked); //
            Executors.newSingleThreadExecutor().execute(() -> { //
                taskDao.updateTask(task); //
                requireActivity().runOnUiThread(() -> { //
                    Toast.makeText(getContext(), "Status da tarefa atualizado!", Toast.LENGTH_SHORT).show(); //
                });
            });
        });

        // Removido o onItemClickListener do item completo para evitar confusão com o botão de edição
        // Se precisar de um clique no item para outra ação, reative e ajuste.
    }

    // Método para navegar para a tela de adicionar nova tarefa
    public void adicionarTask() {
        // Navega para EditTaskFragment sem nenhum argumento, indicando que é uma nova tarefa.
        NavHostFragment.findNavController(TaskListFragment.this).navigate(R.id.action_taskListFragment_to_editTaskFragment); //
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView(); //
        binding = null; //
    }
}