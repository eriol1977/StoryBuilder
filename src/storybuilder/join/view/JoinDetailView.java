package storybuilder.join.view;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import storybuilder.join.model.Join;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractDetailView;
import storybuilder.main.view.AbstractTableView;

/**
 *
 * @author Francesco Bertolino
 */
public class JoinDetailView extends AbstractDetailView
{

    private TextField descriptionField;

    private ListView<String> listAllItemIds;

    private ObservableList<String> allItemIds;

    private ListView<String> listSelectedItemIds;

    private ObservableList<String> selectedItemIds;

    private TextField textField;

    public JoinDetailView(final boolean isNewElement, final IStoryElement element, final AbstractTableView tableView)
    {
        super(isNewElement, element, tableView);
    }

    @Override
    protected void setFields()
    {
        final Join join = (Join) element;
        descriptionField = addLabeledTextInput("Description");
        descriptionField.setText(join.getDescription());

        addLabel("Items");

        final HBox listsBox = new HBox(10);

        final VBox buttonBox = new VBox(10);
        final Button addButton = new Button(">>");
        final Button removeButton = new Button("<<");
        addButton.setPrefWidth(50);
        removeButton.setPrefWidth(50);
        addButton.setDisable(true);
        removeButton.setDisable(true);
        buttonBox.getChildren().add(addButton);
        buttonBox.getChildren().add(removeButton);

        allItemIds = FXCollections.observableArrayList();
        allItemIds.addAll(cache.getStory().getItemIds());
        allItemIds.removeAll(join.getItemIds());
        listAllItemIds = new ListView<>();
        listAllItemIds.setMaxWidth(150);
        listAllItemIds.setItems(allItemIds);
        listsBox.getChildren().add(listAllItemIds);

        listsBox.getChildren().add(buttonBox);

        selectedItemIds = FXCollections.observableArrayList();
        selectedItemIds.addAll(join.getItemIds());
        listSelectedItemIds = new ListView<>();
        listSelectedItemIds.setMaxWidth(150);
        listSelectedItemIds.setItems(selectedItemIds);
        listsBox.getChildren().add(listSelectedItemIds);

        listAllItemIds.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            listSelectedItemIds.getSelectionModel().clearSelection();
            addButton.setDisable(false);
            removeButton.setDisable(true);
        });

        listSelectedItemIds.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            listAllItemIds.getSelectionModel().clearSelection();
            addButton.setDisable(true);
            removeButton.setDisable(false);
        });

        addButton.setOnAction((ActionEvent event) -> {
            final String itemId = listAllItemIds.getSelectionModel().getSelectedItem();
            allItemIds.remove(itemId);
            selectedItemIds.add(itemId);
            listAllItemIds.getSelectionModel().clearSelection();
            addButton.setDisable(true);
        });

        removeButton.setOnAction((ActionEvent event) -> {
            final String itemId = listSelectedItemIds.getSelectionModel().getSelectedItem();
            allItemIds.add(itemId);
            selectedItemIds.remove(itemId);
            listSelectedItemIds.getSelectionModel().clearSelection();
            removeButton.setDisable(true);
        });

        add(listsBox);

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
        join.setTemporarySectionText(textField.getText());
    }

    @Override
    protected void disableFields()
    {
        descriptionField.setDisable(true);
        textField.setDisable(true);
    }

}
