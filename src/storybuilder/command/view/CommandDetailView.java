package storybuilder.command.view;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import storybuilder.command.model.Command;
import storybuilder.main.AbstractView;

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
            addTitle(command.getName());
            nameField = null;
        }

        final TextField keywordField = addLabeledTextInput("Keyword");
        keywordField.setText(command.getKeyword());
        final TextField descriptionField = addLabeledTextInput("Description");
        descriptionField.setText(command.getDescription());
        final Button saveButton = addButton("Save");
        saveButton.setOnAction((ActionEvent e) -> {
            command.setKeyword(keywordField.getText());
            command.setDescription(descriptionField.getText());
            if (isNewCommand) {
                command.setName(nameField.getText());
                commandsView.addCommand(command);
            } else {
                commandsView.updateCommand(command);
            }
        });
    }

}
