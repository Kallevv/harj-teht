package com.example.application.views.list;

import com.example.application.entity.Measurement;
import com.example.application.entity.Person;
import com.example.application.service.MeasurementService;
import com.example.application.service.PersonService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

import java.time.LocalDate;

@PageTitle("Person List")
@Route("list")
@Menu(order = 2, icon = LineAwesomeIconUrl.TH_LIST_SOLID)
@RolesAllowed("USER")
public class ListView extends Main implements HasComponents, HasStyle {

    private final PersonService personService;
    private final Grid<Person> personGrid = new Grid<>(Person.class, false);
    private final TextField searchField = new TextField();
    private final Dialog formDialog = new Dialog();
    private final Binder<Person> binder = new Binder<>(Person.class);

    public ListView(PersonService personService) {
        this.personService = personService;

        setSizeFull();

        addClassNames("person-list-view");
        addClassNames(MaxWidth.SCREEN_LARGE, Margin.Horizontal.AUTO, Padding.Bottom.LARGE, Padding.Horizontal.LARGE);

        constructUI();
        configureGrid();
        configureFormDialog();

        updatePersonList();
    }

    private void constructUI() {
        // Header section
        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames(AlignItems.CENTER, JustifyContent.BETWEEN);

        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("Registered People");
        header.addClassNames(Margin.Bottom.NONE, Margin.Top.XLARGE, FontSize.XXXLARGE);
        Paragraph description = new Paragraph("List of users and their information");
        description.addClassNames(Margin.Bottom.XLARGE, Margin.Top.NONE, TextColor.SECONDARY);
        headerContainer.add(header, description);

        Button addButton = new Button("Add Person", e -> showForm(new Person()));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        container.add(headerContainer, addButton);

        // Search
        searchField.setPlaceholder("Filter by last name...");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setClearButtonVisible(true);
        searchField.setWidth("300px");
        searchField.addValueChangeListener(e -> updatePersonList(e.getValue()));

        VerticalLayout layout = new VerticalLayout(container, searchField, personGrid);
        layout.setPadding(false);
        layout.setSpacing(true);
        layout.setSizeFull();

        add(layout);
    }

    private void configureGrid() {
        personGrid.addClassNames(Margin.Top.MEDIUM);
        personGrid.setSizeFull();

        personGrid.addColumn(Person::getFirstName).setHeader("First Name").setAutoWidth(true);
        personGrid.addColumn(Person::getLastName).setHeader("Last Name").setAutoWidth(true);
        personGrid.addColumn(Person::getEmail).setHeader("Email").setAutoWidth(true);
        personGrid.addColumn(Person::getBirthDate).setHeader("Birth Date").setAutoWidth(true);
    }

    private void configureFormDialog() {
        formDialog.setWidth("400px");

        TextField firstName = new TextField("First Name");
        TextField lastName = new TextField("Last Name");
        EmailField email = new EmailField("Email");
        DatePicker birthDate = new DatePicker("Birth Date");
        birthDate.setMax(LocalDate.now());

        FormLayout formLayout = new FormLayout(firstName, lastName, email, birthDate);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        Button saveButton = new Button("Save", e -> savePerson());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel", e -> formDialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        formDialog.add(formLayout, buttons);
        add(formDialog);

        binder.forField(firstName).asRequired("First name is required").bind(Person::getFirstName, Person::setFirstName);
        binder.forField(lastName).asRequired("Last name is required").bind(Person::getLastName, Person::setLastName);
        binder.forField(email).asRequired("Email is required").withValidator(e -> e.contains("@"), "Must be valid email")
                .bind(Person::getEmail, Person::setEmail);
        binder.forField(birthDate).asRequired("Birth date is required").bind(Person::getBirthDate, Person::setBirthDate);
    }

    private void showForm(Person person) {
        binder.setBean(person);
        formDialog.open();
    }

    private void savePerson() {
        if (binder.validate().isOk()) {
            Person person = binder.getBean();
            personService.save(person);
            updatePersonList();
            formDialog.close();
            Notification.show("Person saved successfully", 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void updatePersonList() {
        personGrid.setItems(personService.findAll());
    }

    private void updatePersonList(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            updatePersonList();
        } else {
            personGrid.setItems(personService.findByLastNameContainingIgnoreCase(filterText));
        }
    }
}