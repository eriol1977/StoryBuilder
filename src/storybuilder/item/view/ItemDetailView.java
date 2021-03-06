package storybuilder.item.view;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import storybuilder.item.model.Item;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractDetailView;
import storybuilder.main.view.AbstractTableView;
import storybuilder.section.view.SectionDetailView;
import storybuilder.validation.SBException;

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
    protected void setFields() throws SBException
    {
        final Item item = (Item) element;
        itemNameField = addLabeledTextInput("Name");
        itemNameField.setText(item.getItemName());
        itemFullNameField = addLabeledTextInput("Full Name");
        itemFullNameField.setText(item.getItemFullName());
        textField = addLabeledTextInput("Section text", 600);
        textField.setText(item.getSectionText());
        if (!isNewElement && !item.getSectionId().isEmpty()) {
            final Button button = addButton("Jump to section " + item.getSectionId());
            button.setOnAction((ActionEvent event) -> {
                mwc.switchToSection(item.getSectionId(), SectionDetailView.EXPAND_PARAGRAPHS);
            });
            button.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, new Insets(2))));
        }
    }

    @Override
    public void setElementValues()
    {
        final Item item = (Item) element;
        item.setItemName(itemNameField.getText());
        item.setItemFullName(itemFullNameField.getText());
        item.setTemporarySectionText(textField.getText());
    }

    @Override
    protected void disableFields()
    {
        itemNameField.setDisable(true);
        itemFullNameField.setDisable(true);
        textField.setDisable(true);
    }

}
