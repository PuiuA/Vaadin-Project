package com.example.application.service;

import com.example.application.model.Task;
import com.example.application.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    public List<Task> findAll() {
        return repo.findAll();
    }

    public Optional<Task> findById(Long id) {
        return repo.findById(id);
    }

    public Task save(Task task) {
        if (task.getCreationDate() == null) {
            task.setCreationDate(LocalDate.now());
        }
        if (task.getCompleted() == null) {
            task.setCompleted(false);
        }
        return repo.save(task);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
