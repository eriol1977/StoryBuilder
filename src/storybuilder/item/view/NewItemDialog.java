package storybuilder.item.view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import storybuilder.main.view.SBDialog;

/**
 *
 * @author Francesco Bertolino
 */
public class NewItemDialog extends SBDialog
{

    private final ObservableList<String> itemList;

    public NewItemDialog(ObservableList<String> itemList)
    {
        this.itemList = itemList;

        final Button save = new Button("Save");
        save.setOnAction((ActionEvent event) -> {
            itemList.add("UNF!");
            close();
        });
        add(save);
    }
}
