package storybuilder.section.view.linkswitch;

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
import storybuilder.event.view.EventDoubleList;
import storybuilder.item.view.ItemDoubleList;
import storybuilder.main.Cache;
import storybuilder.main.view.DoubleList;
import storybuilder.main.view.SBDialog;
import storybuilder.section.model.Link;
import storybuilder.story.model.Story;
import storybuilder.validation.SBException;

/**
 *
 * @author Francesco Bertolino
 */
public class LinkSwitchAddLinkDialog extends SBDialog
{

    public LinkSwitchAddLinkDialog(final AddLinkSwitchDialog view)
    {
        setMinHeight(500);
        setTitle("New link");

        final ObservableList<String> options
                = FXCollections.observableArrayList(
                        Cache.getInstance().getStory().getSectionIds(true)
                );
        final ComboBox newSectionCombo = new ComboBox(options);
        newSectionCombo.setPromptText("Next section");
        final CheckBox newSectionCheckBox = new CheckBox("Create new");
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
        final DoubleList commands = new CommandDoubleList(new ArrayList<>());
        final TitledPane commandsPane = new TitledPane("Commands", commands);
        accordion.getPanes().add(commandsPane);

        final DoubleList items = new ItemDoubleList(new ArrayList<>());
        final TitledPane itemsPane = new TitledPane("Items", items);
        accordion.getPanes().add(itemsPane);

        final DoubleList noItems = new ItemDoubleList(new ArrayList<>());
        final TitledPane noItemsPane = new TitledPane("No-Items", noItems);
        accordion.getPanes().add(noItemsPane);

        final DoubleList events = new EventDoubleList(new ArrayList<>());
        final TitledPane eventsPane = new TitledPane("Events", events);
        accordion.getPanes().add(eventsPane);

        final DoubleList noEvents = new EventDoubleList(new ArrayList<>());
        final TitledPane noEventsPane = new TitledPane("No-Events", noEvents);
        accordion.getPanes().add(noEventsPane);

        accordion.setExpandedPane(commandsPane);
        add(accordion);

        final Button button = new Button("Create");
        button.setOnAction((ActionEvent event) -> {
            final String sectionNumber;
            if (newSectionCheckBox.isSelected()) {
                try {
                    final Story story = Cache.getInstance().getStory();
                    sectionNumber = String.valueOf(story.getNewSectionId());
                    view.setNewSectionNumber(sectionNumber);
                } catch (SBException ex) {
                    view.setResult(ex.getFailCause());
                    close();
                    return;
                }
            } else {
                sectionNumber = (String) newSectionCombo.getSelectionModel().getSelectedItem();
            }
            // id doesn't matter, this Link serves only as a mean to write the LinkSwitch content
            final Link link = new Link("", sectionNumber,
                    commands.getSelectedElementsIds(), items.getSelectedElementsIds(),
                    noItems.getSelectedElementsIds(), events.getSelectedElementsIds(),
                    noEvents.getSelectedElementsIds(), false);
            view.addCreateLinkSwitch(link);
            close();
        });
        add(button);
    }

}
