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
import storybuilder.command.view.NewCommandDialog;
import storybuilder.event.view.EventDoubleList;
import storybuilder.event.view.NewEventDialog;
import storybuilder.item.view.ItemDoubleList;
import storybuilder.item.view.NewItemDialog;
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
        add(new Label("Create a new link"));

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
        final Button newCommand = new Button("New");
        newCommand.setOnAction((ActionEvent event) -> {
            new NewCommandDialog(commands.getRightItems()).show();
        });
        final TitledPane commandsPane = new TitledPane("Commands", new HBox(10, commands, newCommand));
        accordion.getPanes().add(commandsPane);
        
        final DoubleList items = new ItemDoubleList(new ArrayList<>());
        final Button newItem = new Button("New");
        newItem.setOnAction((ActionEvent event) -> {
            new NewItemDialog(items.getRightItems()).show();
        });
        final TitledPane itemsPane = new TitledPane("Items", new HBox(10, items, newItem));
        accordion.getPanes().add(itemsPane);
        
        final DoubleList noItems = new ItemDoubleList(new ArrayList<>());
        final Button newNoItem = new Button("New");
        newNoItem.setOnAction((ActionEvent event) -> {
            new NewItemDialog(noItems.getRightItems()).show();
        });
        final TitledPane noItemsPane = new TitledPane("No-Items", new HBox(10, noItems, newNoItem));
        accordion.getPanes().add(noItemsPane);
        
        final DoubleList events = new EventDoubleList(new ArrayList<>());
        final Button newEvent = new Button("New");
        newEvent.setOnAction((ActionEvent event) -> {
            new NewEventDialog(events.getRightItems()).show();
        });
        final TitledPane eventsPane = new TitledPane("Events", new HBox(10, events, newEvent));
        accordion.getPanes().add(eventsPane);
        
        final DoubleList noEvents = new EventDoubleList(new ArrayList<>());
        final Button newNoEvent = new Button("New");
        newNoEvent.setOnAction((ActionEvent event) -> {
            new NewEventDialog(noEvents.getRightItems()).show();
        });
        final TitledPane noEventsPane = new TitledPane("No-Events", new HBox(10, noEvents, newNoEvent));
        accordion.getPanes().add(noEventsPane);
        
        accordion.setExpandedPane(commandsPane);
        add(accordion);

        final Button button = new Button("Create");
        button.setOnAction((ActionEvent event) -> {
            final String sectionNumber;
            if (newSectionCheckBox.isSelected()) {
                try {
                    final Story story = Cache.getInstance().getStory();
                    sectionNumber = String.valueOf(story.getLastSectionId() + 1);
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
                    commands.getRightItems(), items.getRightItems(), noItems.getRightItems(),
                    events.getRightItems(), noEvents.getRightItems(), false);
            view.addCreateLinkSwitch(link);
            close();
        });
        add(button);
    }

}
