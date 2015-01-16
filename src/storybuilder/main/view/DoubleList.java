package storybuilder.main.view;

import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Francesco Bertolino
 */
public class DoubleList extends HBox
{

    private ListView<String> leftList;

    private ObservableList<String> leftListModel;

    private ListView<String> rightList;

    private ObservableList<String> rightListModel;

    public DoubleList(final List<String> leftItems, final List<String> rightItems)
    {
        setSpacing(10);

        final VBox buttonBox = new VBox(10);
        final Button addButton = new Button(">>");
        final Button removeButton = new Button("<<");
        addButton.setPrefWidth(50);
        removeButton.setPrefWidth(50);
        addButton.setDisable(true);
        removeButton.setDisable(true);
        buttonBox.getChildren().add(addButton);
        buttonBox.getChildren().add(removeButton);

        leftListModel = FXCollections.observableArrayList();
        leftListModel.addAll(leftItems);
        leftListModel.removeAll(rightItems);
        leftList = new ListView<>();
        leftList.setMaxWidth(150);
        leftList.setItems(leftListModel);
        getChildren().add(leftList);

        getChildren().add(buttonBox);

        rightListModel = FXCollections.observableArrayList();
        rightListModel.addAll(rightItems);
        rightList = new ListView<>();
        rightList.setMaxWidth(150);
        rightList.setItems(rightListModel);
        getChildren().add(rightList);

        leftList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            rightList.getSelectionModel().clearSelection();
            addButton.setDisable(false);
            removeButton.setDisable(true);
        });

        rightList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            leftList.getSelectionModel().clearSelection();
            addButton.setDisable(true);
            removeButton.setDisable(false);
        });

        addButton.setOnAction((ActionEvent event) -> {
            final String itemId = leftList.getSelectionModel().getSelectedItem();
            leftListModel.remove(itemId);
            rightListModel.add(itemId);
            leftList.getSelectionModel().clearSelection();
            addButton.setDisable(true);
        });

        removeButton.setOnAction((ActionEvent event) -> {
            final String itemId = rightList.getSelectionModel().getSelectedItem();
            leftListModel.add(itemId);
            rightListModel.remove(itemId);
            rightList.getSelectionModel().clearSelection();
            removeButton.setDisable(true);
        });
    }

    public ObservableList<String> getLeftItems()
    {
        return leftListModel;
    }

    public ObservableList<String> getRightItems()
    {
        return rightListModel;
    }
    
    public void setRightItems(final List<String> items)
    {
        rightListModel.clear();
        rightListModel.addAll(items);
        leftListModel.removeAll(items);
    }
    
    public void newRightItem(final String item) {
        rightListModel.add(item);
    }
}
