package com.example.projetoplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton; // Importação adicionada para ImageButton
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoplanner.entities.Task;
import com.example.projetoplanner.databinding.ItemTaskBinding;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskViewHolder> {

    // Mantido, mas o foco da edição será no ImageButton
    public interface OnItemClickListener {
        void onItemClick(Task task);
    }

    public interface OnCheckboxChangeListener {
        void onCheckboxChange(Task task, boolean isChecked);
    }

    public interface OnEditButtonClickListener { // Nova interface para o botão de edição
        void onEditButtonClick(Task task);
    }

    private OnItemClickListener listener;
    private OnCheckboxChangeListener checkboxListener;
    private OnEditButtonClickListener editButtonListener; // Novo campo para o listener do botão de edição

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnCheckboxChangeListener(OnCheckboxChangeListener checkboxListener) {
        this.checkboxListener = checkboxListener;
    }

    public void setOnEditButtonClickListener(OnEditButtonClickListener editButtonListener) { // Setter para o novo listener
        this.editButtonListener = editButtonListener;
    }

    public TaskAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTaskBinding binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = getItem(position);
        holder.bind(currentTask);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewDescription;
        private final CheckBox checkBoxStatus;
        private final ImageButton imageButtonEdit; // Referência ao ImageButton

        public TaskViewHolder(@NonNull ItemTaskBinding binding) {
            super(binding.getRoot());
            textViewDescription = binding.textViewTaskDescription;
            checkBoxStatus = binding.checkBoxTaskStatus;
            imageButtonEdit = binding.imageButtonEdit; // Inicialize o ImageButton

            // Opcional: Se você quiser que o item inteiro seja clicável para alguma outra ação (não edição)
            // Cuidado para não conflitar com o clique do ImageButtonEdit
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });

            // Listener para o CheckBox de status
            checkBoxStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = getAdapterPosition();
                if (checkboxListener != null && position != RecyclerView.NO_POSITION) {
                    checkboxListener.onCheckboxChange(getItem(position), isChecked);
                }
            });

            // Listener para o ImageButton de edição
            imageButtonEdit.setOnClickListener(v -> {
                int position = getAdapterPosition(); //
                if (editButtonListener != null && position != RecyclerView.NO_POSITION) { //
                    editButtonListener.onEditButtonClick(getItem(position)); //
                }
            });
        }

        public void bind(Task task) {
            textViewDescription.setText(task.getDescricao()); //

            // Importante: Defina null antes de setar o checked para evitar acionar o listener durante a reciclagem
            checkBoxStatus.setOnCheckedChangeListener(null); //
            checkBoxStatus.setChecked(task.getStatus()); //
            // Depois de setar o checked, redefina o listener
            checkBoxStatus.setOnCheckedChangeListener((buttonView, isChecked) -> { //
                int position = getAdapterPosition(); //
                if (checkboxListener != null && position != RecyclerView.NO_POSITION) { //
                    checkboxListener.onCheckboxChange(getItem(position), isChecked); //
                }
            });
            // O listener do imageButtonEdit é definido uma vez no construtor, não precisa de rebind aqui
        }
    }

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getDescricao().equals(newItem.getDescricao()) &&
                    oldItem.getStatus().equals(newItem.getStatus());
        }
    };
}