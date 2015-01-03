package storybuilder.event.view;

import javafx.scene.control.TextField;
import storybuilder.event.model.Event;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractDetailView;
import storybuilder.main.view.AbstractTableView;

/**
 *
 * @author Francesco Bertolino
 */
public class EventDetailView extends AbstractDetailView
{

    private TextField descriptionField;

    public EventDetailView(final IStoryElement element, final AbstractTableView tableView)
    {
        super(element, tableView);
    }

    @Override
    protected void setFields()
    {
        final Event event = (Event) element;
        descriptionField = addLabeledTextInput("Description", 200);
        descriptionField.setText(event.getDescription());
    }

    @Override
    protected void setElementValues()
    {
        final Event event = (Event) element;
        event.setDescription(descriptionField.getText());
    }

    @Override
    protected void disableFields()
    {
        descriptionField.setDisable(true);
    }

}
