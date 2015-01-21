package storybuilder.section.view.link;

import java.util.ArrayList;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import storybuilder.command.view.CommandDoubleList;
import storybuilder.command.view.NewCommandDialog;
import storybuilder.event.view.EventDoubleList;
import storybuilder.event.view.NewEventDialog;
import storybuilder.item.view.ItemDoubleList;
import storybuilder.item.view.NewItemDialog;
import storybuilder.main.Cache;
import storybuilder.main.view.DoubleList;
import storybuilder.main.view.SBDialog;
import storybuilder.section.model.Section;
import storybuilder.story.model.Story;
import storybuilder.validation.SBException;
import storybuilder.validation.ValidationFailed;

/**
 *
 * @author Francesco Bertolino
 */
public abstract class LinkDialog extends SBDialog
{

    protected final LinksTable linksTable;
    protected final ComboBox newSectionCombo;
    protected final CheckBox newSectionCheckBox;
    protected final DoubleList commands;
    protected final DoubleList items;
    protected final DoubleList noItems;
    protected final DoubleList events;
    protected final DoubleList noEvents;

    public LinkDialog(final String title, final LinksTable linksTable)
    {
        this.linksTable = linksTable;

        setMinHeight(500);
        add(new Label(title));

        final ObservableList<String> options
                = FXCollections.observableArrayList(
                        Cache.getInstance().getStory().getSectionIds(true)
                );
        newSectionCombo = new ComboBox(options);
        newSectionCombo.setPromptText("Next section");
        newSectionCheckBox = new CheckBox("Create new");
        newSectionCheckBox.setSelected(true);
        newSectionCheckBox.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
            if (new_val) {
                newSectionCombo.getSelectionModel().clearSelection();
                newSectionCombo.setDisable(true);
            } else {
                newSectionCombo.setDisable(false);
            }
        });
        newSectionCombo.setOnAction((Event event) -> {
            newSectionCheckBox.setSelected(false);
        });
        final HBox sectionBox = new HBox(10);
        sectionBox.getChildren().add(newSectionCombo);
        sectionBox.getChildren().add(newSectionCheckBox);
        add(sectionBox);

        final Accordion accordion = new Accordion();

        commands = new CommandDoubleList(new ArrayList<>());
        final Button newCommand = new Button("New");
        newCommand.setOnAction((ActionEvent event) -> {
            new NewCommandDialog(commands.getRightItems()).show();
        });
        final TitledPane commandsPane = new TitledPane("Commands", new HBox(10, commands, newCommand));
        accordion.getPanes().add(commandsPane);

        items = new ItemDoubleList(new ArrayList<>());
        final Button newItem = new Button("New");
        newItem.setOnAction((ActionEvent event) -> {
            new NewItemDialog(items.getRightItems()).show();
        });
        final TitledPane itemsPane = new TitledPane("Items", new HBox(10, items, newItem));
        accordion.getPanes().add(itemsPane);

        noItems = new ItemDoubleList(new ArrayList<>());
        final Button newNoItem = new Button("New");
        newNoItem.setOnAction((ActionEvent event) -> {
            new NewItemDialog(noItems.getRightItems()).show();
        });
        final TitledPane noItemsPane = new TitledPane("No-Items", new HBox(10, noItems, newNoItem));
        accordion.getPanes().add(noItemsPane);

        events = new EventDoubleList(new ArrayList<>());
        final Button newEvent = new Button("New");
        newEvent.setOnAction((ActionEvent event) -> {
            new NewEventDialog(events.getRightItems()).show();
        });
        final TitledPane eventsPane = new TitledPane("Events", new HBox(10, events, newEvent));
        accordion.getPanes().add(eventsPane);

        noEvents = new EventDoubleList(new ArrayList<>());
        final Button newNoEvent = new Button("New");
        newNoEvent.setOnAction((ActionEvent event) -> {
            new NewEventDialog(noEvents.getRightItems()).show();
        });
        final TitledPane noEventsPane = new TitledPane("No-Events", new HBox(10, noEvents, newNoEvent));
        accordion.getPanes().add(noEventsPane);
        accordion.setExpandedPane(commandsPane);

        add(accordion);

        final Button button = new Button(title);
        button.setOnAction((ActionEvent event) -> {
            doSomething();
        });
        add(button);
    }

    protected abstract void doSomething();

    protected String getNextSectionId() throws ValidationFailed, SBException
    {
        if (newSectionCheckBox.isSelected()) {
            final Story story = Cache.getInstance().getStory();
            final Section section = story.addNewEmptySection(linksTable.getNewLinkSectionId(), "link in section " + linksTable.getSection().getNameWithoutPrefix());
            return section.getNameWithoutPrefix();
        } else {
            final String sectionNumber = (String) newSectionCombo.getSelectionModel().getSelectedItem();
            if (sectionNumber == null || sectionNumber.isEmpty()) {
                throw new ValidationFailed("Next section is required");
            }
            return sectionNumber;
        }
    }
}
