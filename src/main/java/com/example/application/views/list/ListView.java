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

import java.time.LocalDate;

@PageTitle("List view")
@Route("list")
@Menu(order = 1, icon = LineAwesomeIconUrl.TH_LIST_SOLID)
@RolesAllowed("USER")
public class ListView extends VerticalLayout {

    private final PersonService personService;
    private final MeasurementService measurementService;

    private final Grid<Person> personGrid = new Grid<>(Person.class, false);
    private final Grid<Measurement> measurementGrid = new Grid<>(Measurement.class, false);
    private final Dialog formDialog = new Dialog();
    private final Binder<Person> binder = new Binder<>(Person.class);

    public ListView(PersonService personService, MeasurementService measurementService) {
        this.personService = personService;
        this.measurementService = measurementService;

        configureGrids();
        configureFormDialog();
        setupLayout();
        updatePersonList();
    }

    private void configureGrids() {
        // Person Grid
        personGrid.addColumn(Person::getFirstName).setHeader("First Name").setAutoWidth(true);
        personGrid.addColumn(Person::getLastName).setHeader("Last Name").setAutoWidth(true);
        personGrid.addColumn(Person::getEmail).setHeader("Email").setAutoWidth(true);
        personGrid.addColumn(Person::getBirthDate).setHeader("Birth Date").setAutoWidth(true);

        personGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                updateMeasurementList(event.getValue().getId());
            }
        });

        // Measurement Grid
        measurementGrid.addColumn(Measurement::getTimestamp).setHeader("Timestamp").setAutoWidth(true);
        measurementGrid.addColumn(Measurement::getType).setHeader("Type").setAutoWidth(true);
        measurementGrid.addColumn(Measurement::getValue).setHeader("Value").setAutoWidth(true);
    }

    private void configureFormDialog() {
        formDialog.setWidth("400px");

        // Form fields
        TextField firstName = new TextField("First Name");
        TextField lastName = new TextField("Last Name");
        EmailField email = new EmailField("Email");
        DatePicker birthDate = new DatePicker("Birth Date");
        birthDate.setMax(LocalDate.now());

        // Form buttons
        Button saveButton = new Button("Save", e -> savePerson());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel", e -> formDialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        // Form layout
        FormLayout formLayout = new FormLayout();
        formLayout.add(firstName, lastName, email, birthDate);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        formDialog.add(formLayout, buttons);

        // Configure binder
        binder.forField(firstName)
                .asRequired("First name is required")
                .bind(Person::getFirstName, Person::setFirstName);
        binder.forField(lastName)
                .asRequired("Last name is required")
                .bind(Person::getLastName, Person::setLastName);
        binder.forField(email)
                .asRequired("Email is required")
                .withValidator(e -> e.contains("@"), "Must be a valid email")
                .bind(Person::getEmail, Person::setEmail);
        binder.forField(birthDate)
                .asRequired("Birth date is required")
                .bind(Person::getBirthDate, Person::setBirthDate);
    }

    private void setupLayout() {
        // Add button to open form
        Button addButton = new Button("Add Person", e -> showForm(new Person()));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Search field
        TextField searchField = new TextField();
        searchField.setPlaceholder("Filter by last name...");
        searchField.addValueChangeListener(e -> updatePersonList(e.getValue()));

        // Main layout
        HorizontalLayout toolbar = new HorizontalLayout(addButton, searchField);
        VerticalLayout mainLayout = new VerticalLayout(
                toolbar,
                new HorizontalLayout(personGrid, measurementGrid)
        );
        mainLayout.setSizeFull();
        personGrid.setSizeFull();
        measurementGrid.setSizeFull();

        add(mainLayout);
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
            personGrid.setItems(personService.findAll());
        } else {
            personGrid.setItems(personService.findByLastNameContainingIgnoreCase(filterText));
        }
    }

    private void updateMeasurementList(Long personId) {
        measurementGrid.setItems(measurementService.findByPersonId(personId));
    }
}