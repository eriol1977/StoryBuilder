package storybuilder.command.view;

import javafx.scene.control.TextField;
import storybuilder.command.model.Command;
import storybuilder.main.view.AbstractDetailView;

/**
 *
 * @author Francesco Bertolino
 */
public class CommandDetailView extends AbstractDetailView
{

    private TextField keywordField;

    private TextField descriptionField;

    public CommandDetailView(final Command command, final CommandsView commandsView)
    {
        super(command, commandsView);
    }

    @Override
    protected void setFields()
    {
        final Command command = (Command) element;
        keywordField = addLabeledTextInput("Keyword");
        keywordField.setText(command.getKeyword());
        descriptionField = addLabeledTextInput("Description", 200);
        descriptionField.setText(command.getDescription());
    }

    @Override
    protected void setElementValues()
    {
        final Command command = (Command) element;
        command.setKeyword(keywordField.getText());
        command.setDescription(descriptionField.getText());
    }

    @Override
    protected void disableFields()
    {
        keywordField.setDisable(true);
        descriptionField.setDisable(true);
    }

}
