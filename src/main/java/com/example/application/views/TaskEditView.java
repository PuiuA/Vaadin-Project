package com.example.application.views;

import com.example.application.model.Task;
import com.example.application.service.TaskService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;

@Route("tasks/:id/edit")
public class TaskEditView extends VerticalLayout implements BeforeEnterObserver {

    private final TaskService taskService;

    private final Binder<Task> binder = new Binder<>(Task.class);

    private final TextField title = new TextField("Title");
    private final TextArea description = new TextArea("Description");
    private final Checkbox completed = new Checkbox("Completed");

    private final Button save = new Button("Save");
    private final Button cancel = new Button("Cancel");

    private Task currentTask;

    public TaskEditView(TaskService taskService) {
        this.taskService = taskService;

        addClassName("centered-content");

        H2 heading = new H2("Edit task");

        title.setRequired(true);
        title.setWidthFull();

        description.setWidthFull();
        description.setMinHeight("120px");

        binder.forField(title)
                .asRequired("Title este obligatoriu")
                .bind(Task::getTitle, Task::setTitle);

        binder.forField(description)
                .bind(Task::getDescription, Task::setDescription);

        binder.forField(completed)
                .bind(t -> Boolean.TRUE.equals(t.getCompleted()), Task::setCompleted);

        save.addClickListener(e -> onSave());
        cancel.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("tasks")));

        add(heading, title, description, completed, new HorizontalLayout(save, cancel));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String idStr = event.getRouteParameters().get("id").orElse(null);
        if (idStr == null) {
            event.forwardTo("tasks");
            return;
        }

        Long id = Long.valueOf(idStr);
        currentTask = taskService.findById(id).orElse(null);

        if (currentTask == null) {
            Notification.show("Task nu exista");
            event.forwardTo("tasks");
            return;
        }

        // umplem automat fields
        binder.setBean(currentTask);
    }

    private void onSave() {
        if (binder.validate().isOk()) {
            taskService.save(currentTask);
            Notification.show("Modificat!");
            getUI().ifPresent(ui -> ui.navigate("tasks"));
        }
    }
}
