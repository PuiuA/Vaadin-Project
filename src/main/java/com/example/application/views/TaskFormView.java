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

import java.util.Optional;

@Route("tasks/new")
public class TaskFormView extends VerticalLayout implements BeforeEnterObserver {

    private final TaskService taskService;

    private final Binder<Task> binder = new Binder<>(Task.class);

    private final TextField title = new TextField("Title");
    private final TextArea description = new TextArea("Description");
    private final Checkbox completed = new Checkbox("Completed");

    private final Button save = new Button("Save");
    private final Button cancel = new Button("Cancel");

    private Task currentTask;

    public TaskFormView(TaskService taskService) {
        this.taskService = taskService;

        addClassName("centered-content");

        H2 heading = new H2("Create task");

        title.setRequired(true);
        title.setWidthFull();

        description.setWidthFull();
        description.setMinHeight("120px");

        // binding
        binder.forField(title)
                .asRequired("Title este obligatoriu")
                .bind(Task::getTitle, Task::setTitle);

        binder.forField(description)
                .bind(Task::getDescription, Task::setDescription);

        binder.forField(completed)
                .bind(t -> Boolean.TRUE.equals(t.getCompleted()), Task::setCompleted);

        save.addClickListener(e -> onSave());
        cancel.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("tasks")));

        HorizontalLayout actions = new HorizontalLayout(save, cancel);

        add(heading, title, description, completed, actions);

        // default: create mode
        setCreateMode();
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {}

    private void setCreateMode() {
        currentTask = new Task();
        binder.setBean(currentTask);
    }

    private void onSave() {
        if (binder.validate().isOk()) {
            taskService.save(currentTask);
            Notification.show("Salvat!");
            getUI().ifPresent(ui -> ui.navigate("tasks"));
        }
    }
}
