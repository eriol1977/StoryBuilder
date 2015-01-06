package storybuilder.join.view;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import storybuilder.join.model.Join;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractDetailView;
import storybuilder.main.view.AbstractTableView;
import storybuilder.main.view.DoubleList;
import storybuilder.validation.SBException;

/**
 *
 * @author Francesco Bertolino
 */
public class JoinDetailView extends AbstractDetailView
{

    private TextField descriptionField;

    private DoubleList itemsField;

    private TextField textField;

    public JoinDetailView(final boolean isNewElement, final IStoryElement element, final AbstractTableView tableView)
    {
        super(isNewElement, element, tableView);
    }

    @Override
    protected void setFields() throws SBException
    {
        final Join join = (Join) element;
        descriptionField = addLabeledTextInput("Description");
        descriptionField.setText(join.getDescription());

        addLabel("Items");

        itemsField = new DoubleList(cache.getStory().getItemIds(), join.getItemIds());
        add(itemsField);

        textField = addLabeledTextInput("Section text", 600);
        textField.setText(join.getSectionText());
        if (!isNewElement && !join.getSectionId().isEmpty()) {
            final Button button = addButton("Go to description section (" + join.getSectionId() + ")");
            button.setOnAction((ActionEvent event) -> {
                mwc.switchToSection(join.getSectionId());
            });
        }
    }

    @Override
    protected void setElementValues()
    {
        final Join join = (Join) element;
        join.setDescription(descriptionField.getText());
        join.setItemIds(itemsField.getRightItems());
        join.setTemporarySectionText(textField.getText());
    }

    @Override
    protected void disableFields()
    {
        descriptionField.setDisable(true);
        itemsField.setDisable(true);
        textField.setDisable(true);
    }

}
