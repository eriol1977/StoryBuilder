package storybuilder.main.view;

import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import storybuilder.main.model.StoryElement;

/**
 *
 * @author Francesco Bertolino
 */
public abstract class DoubleList extends HBox
{

    private ListView<StoryElement> leftList;

    private ObservableList<StoryElement> leftListModel;

    private ListView<StoryElement> rightList;

    private ObservableList<StoryElement> rightListModel;

    public DoubleList(final List<? extends StoryElement> rightItems)
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

        final Button refreshButton = new Button("RF");
        refreshButton.setPrefWidth(50);
        refreshButton.setOnAction((ActionEvent event) -> {
            refresh();
        });
        buttonBox.getChildren().add(refreshButton);

        final Button newrefreshButton = new Button("New");
        newrefreshButton.setPrefWidth(50);
        newrefreshButton.setOnAction((ActionEvent event) -> {
            createNewElement();
        });
        buttonBox.getChildren().add(newrefreshButton);

        leftListModel = FXCollections.observableArrayList();
        leftListModel.addAll(loadLeftItems());
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

        leftList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends StoryElement> observable, StoryElement oldValue, StoryElement newValue) -> {
            rightList.getSelectionModel().clearSelection();
            addButton.setDisable(false);
            removeButton.setDisable(true);
        });

        rightList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends StoryElement> ov, StoryElement old_val, StoryElement new_val) -> {
            leftList.getSelectionModel().clearSelection();
            addButton.setDisable(true);
            removeButton.setDisable(false);
        });

        addButton.setOnAction((ActionEvent event) -> {
            final StoryElement element = leftList.getSelectionModel().getSelectedItem();
            leftListModel.remove(element);
            rightListModel.add(element);
            leftList.getSelectionModel().clearSelection();
            addButton.setDisable(true);
        });

        removeButton.setOnAction((ActionEvent event) -> {
            final StoryElement element = rightList.getSelectionModel().getSelectedItem();
            leftListModel.add(element);
            rightListModel.remove(element);
            rightList.getSelectionModel().clearSelection();
            removeButton.setDisable(true);
        });
    }

    public ObservableList<StoryElement> getLeftItems()
    {
        return leftListModel;
    }

    public ObservableList<StoryElement> getRightItems()
    {
        return rightListModel;
    }

    public List<String> getSelectedElementsIds()
    {
        return rightListModel.stream().map(i -> i.getName()).collect(Collectors.toList());
    }

    public void setRightItems(final List<? extends StoryElement> items)
    {
        rightListModel.clear();
        rightListModel.addAll(items);
        leftListModel.removeAll(items);
    }

    public void newRightItem(final StoryElement item)
    {
        rightListModel.add(item);
    }

    protected abstract List<? extends StoryElement> loadLeftItems();

    public void refresh()
    {
        leftListModel.clear();
        leftListModel.addAll(loadLeftItems());
        leftListModel.removeAll(rightListModel);
    }

    protected abstract void createNewElement();
}
