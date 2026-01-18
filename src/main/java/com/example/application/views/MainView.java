package com.example.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {
    public MainView() {
        addClassName("centered-content");
        Button go = new Button("Go to tasks", e -> getUI().ifPresent(ui -> ui.navigate("tasks")));
        add(go);
    }
}
