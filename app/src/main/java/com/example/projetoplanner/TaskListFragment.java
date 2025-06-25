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
import androidx.recyclerview.widget.LinearLayoutManager;

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

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).binding.fab.setVisibility(View.GONE);
        }

        PlannerDatabase db = PlannerDatabase.getDatabase(requireContext());
        taskDao = db.taskDao();


        taskAdapter = new TaskAdapter();
        binding.listTask.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.listTask.setAdapter(taskAdapter);


        taskDao.getAllTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                taskAdapter.submitList(tasks);

            }
        });


        setupListeners(); //
    }

    private void setupListeners() {

        binding.fabAddTask.setOnClickListener(v -> adicionarTask());


        taskAdapter.setOnEditButtonClickListener(task -> {

            Bundle bundle = new Bundle();
            bundle.putInt("taskId", task.getId());
            NavHostFragment.findNavController(TaskListFragment.this).navigate(R.id.action_taskListFragment_to_editTaskFragment, bundle); //
        });


        taskAdapter.setOnCheckboxChangeListener((task, isChecked) -> {
            task.setStatus(isChecked);
            Executors.newSingleThreadExecutor().execute(() -> {
                taskDao.updateTask(task);
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Status da tarefa atualizado! :)", Toast.LENGTH_SHORT).show();
                });
            });
        });


    }


    public void adicionarTask() {

        NavHostFragment.findNavController(TaskListFragment.this).navigate(R.id.action_taskListFragment_to_editTaskFragment); //
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}