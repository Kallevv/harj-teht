package com.example.application.views.themecolor;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@Route("theme-colors")
@Menu(order = 1, icon = LineAwesomeIconUrl.TH_LIST_SOLID)
@AnonymousAllowed
public class ThemeColorView extends VerticalLayout {

    private final String[] THEME_COLORS = {
            "var(--lumo-primary-text-color)", // Teeman pääväri
            "var(--lumo-error-text-color)",   // Virheväri
            "var(--lumo-success-text-color)", // Onnistumisväri
            "var(--lumo-contrast)"            // Kontrastiväri
    };
    private int colorIndex = 0;

    public ThemeColorView() {
        Button themeButton = new Button("Vaihda teemaväriä");
        Span text = new Span("Amppareiden teemavärit");

        themeButton.addClickListener(event -> {
            text.getStyle().set("color", THEME_COLORS[colorIndex]);
            colorIndex = (colorIndex + 1) % THEME_COLORS.length;
        });

        add(themeButton, text);
    }
}
