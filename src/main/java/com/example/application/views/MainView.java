package com.example.application.views;

import com.example.application.service.GreetService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {

  public MainView(GreetService service) {

        Paragraph paragraph = new Paragraph("TODO app");
        paragraph.addClassName("main-view-paragraph");

//       change button color
        Button button = new Button("Sign in");
        button.addClassName("sign-in-button");
        button.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        button.addClickShortcut(Key.ENTER);

        Div div = getDiv(button);

        // Use custom CSS classes to apply styling. This is defined in
        // styles.css.
        addClassName("centered-content");

        add(paragraph, div);
    }

    private static Div getDiv(Button button) {
        TextField usernameTextField = new TextField("Username");
        usernameTextField.addClassName("username-input-field");

        TextField passwordTextField = new TextField("Password");
        passwordTextField.addClassName("password-input-field");

        Div div = new Div(usernameTextField, passwordTextField, button);
        div.addClassName("sign-in-view-div");

        return div;
    }

}
