package storybuilder.main.view;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.model.StoryElement;

/**
 *
 * @author Francesco Bertolino
 */
public abstract class AbstractDetailView extends AbstractView
{

    protected final AbstractTableView tableView;

    protected final IStoryElement element;

    public AbstractDetailView(final IStoryElement element, final AbstractTableView tableView)
    {
        this.element = element;
        this.tableView = tableView;
        final boolean isNewElement = element.getName().isEmpty();

        final TextField nameField;
        if (isNewElement) {
            addTitle("New");
            nameField = addLabeledTextInput("Name");
        } else {
            addTitle(element.getNameWithoutPrefix());
            nameField = null;
        }

        setFields();

        final Button saveButton = addButton("Save");
        saveButton.setOnAction((ActionEvent e) -> {
            setElementValues();
            if (isNewElement) {
                ((StoryElement) (element)).setNameWithoutPrefix(nameField.getText());
                tableView.addElement(element);
            } else {
                tableView.updateElement(element);
            }
        });

        if (element.isDefault()) {
            disableFields();
            saveButton.setDisable(true);
        }
    }

    protected abstract void setFields();

    protected abstract void setElementValues();

    protected abstract void disableFields();

}
