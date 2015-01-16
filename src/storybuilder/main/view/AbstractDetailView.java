package storybuilder.main.view;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.model.StoryElement;
import storybuilder.validation.ErrorManager;
import storybuilder.validation.SBException;

/**
 *
 * @author Francesco Bertolino
 */
public abstract class AbstractDetailView extends AbstractView
{

    protected final AbstractTableView tableView;

    protected final IStoryElement element;

    protected final TextField nameField;

    protected final boolean isNewElement;
    
    protected Button saveButton;

    public AbstractDetailView()
    {
        this.tableView = null;
        this.isNewElement = false;
        this.element = null;
        this.nameField = null;
    }

    public AbstractDetailView(final boolean isNewElement, final IStoryElement element, final AbstractTableView tableView)
    {
        this.isNewElement = isNewElement;
        this.element = element;
        this.tableView = tableView;

        if (isNewElement) {
            addTitle("New");
            nameField = addLabeledTextInput("Code");
        } else {
            addTitle(element.getNameWithoutPrefix());
            nameField = null;
        }

        try {
            setFields();
        } catch (SBException ex) {
            ErrorManager.showErrorMessage(ex.getFailCause());
        }

        saveButton = addButton("Save");
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

    protected abstract void setFields() throws SBException;

    protected abstract void setElementValues();

    protected abstract void disableFields();

    public Button getSaveButton()
    {
        return saveButton;
    }
}
