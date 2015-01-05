package storybuilder.item.view;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import storybuilder.item.model.Item;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractDetailView;
import storybuilder.main.view.AbstractTableView;

/**
 *
 * @author Francesco Bertolino
 */
public class ItemDetailView extends AbstractDetailView
{
    
    private TextField itemNameField;
    
    private TextField itemFullNameField;
    
    private TextField textField;
    
    public ItemDetailView(final boolean isNewElement, final IStoryElement element, final AbstractTableView tableView)
    {
        super(isNewElement, element, tableView);
    }
    
    @Override
    protected void setFields()
    {
        final Item item = (Item) element;
        itemNameField = addLabeledTextInput("Name");
        itemNameField.setText(item.getItemName());
        itemFullNameField = addLabeledTextInput("Full Name");
        itemFullNameField.setText(item.getItemFullName());
        textField = addLabeledTextInput("Description", 600);
        textField.setText(item.getDescription());
        if (!isNewElement && !item.getSectionId().isEmpty()) {
            final Button button = addButton("Go to description section (" + item.getSectionId() + ")");
            button.setOnAction((ActionEvent event) -> {
                mwc.switchToSection(item.getSectionId());
            });
        }
    }
    
    @Override
    protected void setElementValues()
    {
        final Item item = (Item) element;
        item.setItemName(itemNameField.getText());
        item.setItemFullName(itemFullNameField.getText());
        item.setTemporaryDescription(textField.getText());
    }
    
    @Override
    protected void disableFields()
    {
        itemNameField.setDisable(true);
        itemFullNameField.setDisable(true);
        textField.setDisable(true);
    }
    
}
