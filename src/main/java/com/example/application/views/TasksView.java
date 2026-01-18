package com.example.application.views;

import com.example.application.model.Task;
import com.example.application.service.TaskService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route("tasks")
public class TasksView extends VerticalLayout {

    private final TaskService taskService;

    private final Grid<Task> grid = new Grid<>(Task.class, false);
    private final Span emptyLabel = new Span("Nu exista taskuri");

    public TasksView(TaskService taskService) {
        this.taskService = taskService;
        addClassName("centered-content");

        H2 title = new H2("TODO app");
        Button createBtn = new Button("Create task", e -> getUI().ifPresent(ui -> ui.navigate("tasks/new")));
        HorizontalLayout header = new HorizontalLayout(title, createBtn);
        header.setAlignItems(Alignment.CENTER);
        header.setWidthFull();
        header.expand(title);
        configureGrid();

        add(header, emptyLabel, grid);
        refresh();
    }

    private void configureGrid() {
        grid.addColumn(Task::getTitle).setHeader("Title").setAutoWidth(true).setFlexGrow(1);
        grid.addColumn(Task::getDescription).setHeader("Description").setAutoWidth(true).setFlexGrow(2);
        grid.addColumn(t -> t.getCreationDate() == null ? "" : t.getCreationDate().toString())
                .setHeader("Created").setAutoWidth(true);

        grid.addColumn(new ComponentRenderer<>(task -> {
            com.vaadin.flow.component.checkbox.Checkbox cb = new com.vaadin.flow.component.checkbox.Checkbox();
            cb.setValue(Boolean.TRUE.equals(task.getCompleted()));
            cb.addValueChangeListener(e -> {
                task.setCompleted(e.getValue());
                taskService.save(task);
            });
            return cb;
        })).setHeader("Done").setAutoWidth(true);

        grid.addColumn(new ComponentRenderer<>(task -> {
            Button edit = new Button("Edit", e -> getUI().ifPresent(ui -> ui.navigate("tasks/" + task.getId() + "/edit")));
            Button del = new Button("Delete", e -> confirmDelete(task));
            return new HorizontalLayout(edit, del);
        })).setHeader("Actions").setAutoWidth(true);
    }

    private void confirmDelete(Task task) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Stergi task-ul?");
        dialog.setText("Task: " + task.getTitle());
        dialog.setCancelable(true);
        dialog.setConfirmText("Sterge");
        dialog.addConfirmListener(e -> {
            taskService.delete(task.getId());
            refresh();
        });
        dialog.open();
    }

    private void refresh() {
        List<Task> tasks = taskService.findAll();
        grid.setItems(tasks);

        boolean empty = tasks.isEmpty();
        emptyLabel.setVisible(empty);
        grid.setVisible(!empty);
    }
}
