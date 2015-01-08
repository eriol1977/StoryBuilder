package storybuilder.section.view.link;

import java.util.ArrayList;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
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
        newSectionCheckBox.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
            if (new_val) {
                newSectionCombo.getSelectionModel().clearSelection();
                newSectionCombo.setDisable(true);
            } else {
                newSectionCombo.setDisable(false);
            }
        });
        final HBox sectionBox = new HBox(10);
        sectionBox.getChildren().add(newSectionCombo);
        sectionBox.getChildren().add(newSectionCheckBox);
        add(sectionBox);

        final Accordion accordion = new Accordion();
        commands = new DoubleList(Cache.getInstance().getStory().getCommandIds(), new ArrayList<>());
        final TitledPane commandsPane = new TitledPane("Commands", commands);
        accordion.getPanes().add(commandsPane);
        items = new DoubleList(Cache.getInstance().getStory().getItemIds(), new ArrayList<>());
        final TitledPane itemsPane = new TitledPane("Items to have", items);
        accordion.getPanes().add(itemsPane);
        noItems = new DoubleList(Cache.getInstance().getStory().getItemIds(), new ArrayList<>());
        final TitledPane noItemsPane = new TitledPane("Items not to have", noItems);
        accordion.getPanes().add(noItemsPane);
        events = new DoubleList(Cache.getInstance().getStory().getEventIds(), new ArrayList<>());
        final TitledPane eventsPane = new TitledPane("Events to have", events);
        accordion.getPanes().add(eventsPane);
        noEvents = new DoubleList(Cache.getInstance().getStory().getEventIds(), new ArrayList<>());
        final TitledPane noEventsPane = new TitledPane("Events not to have", noEvents);
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
            final Section section = story.addNewEmptySection(linksTable.getNewLinkSectionId());
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
