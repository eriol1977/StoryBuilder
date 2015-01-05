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

    protected final TextField nameField;

    public AbstractDetailView()
    {
        this.tableView = null;
        this.element = null;
        this.nameField = null;
    }
    
    public AbstractDetailView(final boolean isNewElement, final IStoryElement element, final AbstractTableView tableView)
    {
        this.element = element;
        this.tableView = tableView;

        if (isNewElement) {
            addTitle("New");
            nameField = addLabeledTextInput("Code");
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
