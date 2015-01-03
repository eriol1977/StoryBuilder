package storybuilder.command.view;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import storybuilder.command.model.Command;
import storybuilder.main.view.AbstractView;

/**
 *
 * @author Francesco Bertolino
 */
public class CommandDetailView extends AbstractView
{

    private final CommandsView commandsView;

    private final Command command;

    public CommandDetailView(final Command command, final CommandsView commandsView)
    {
        this.command = command;
        this.commandsView = commandsView;
        final boolean isNewCommand = command.getName().isEmpty();

        final TextField nameField;
        if (isNewCommand) {
            addTitle("New command");
            nameField = addLabeledTextInput("Name");
        } else {
            addTitle(command.getNameWithoutPrefix());
            nameField = null;
        }

        final TextField keywordField = addLabeledTextInput("Keyword");
        keywordField.setText(command.getKeyword());
        final TextField descriptionField = addLabeledTextInput("Description", 200);
        descriptionField.setText(command.getDescription());

        final Button saveButton = addButton("Save");
        saveButton.setOnAction((ActionEvent e) -> {
            command.setKeyword(keywordField.getText());
            command.setDescription(descriptionField.getText());
            if (isNewCommand) {
                command.setNameWithoutPrefix(nameField.getText());
                commandsView.addElement(command);
            } else {
                commandsView.updateElement(command);
            }
        });

        if (command.isDefault()) {
            keywordField.setDisable(true);
            descriptionField.setDisable(true);
            saveButton.setDisable(true);
        }
    }

}
