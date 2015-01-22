package storybuilder.section.view;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import storybuilder.item.view.ItemDoubleList;
import storybuilder.main.Cache;
import storybuilder.main.view.DoubleList;
import storybuilder.section.model.Drop;
import storybuilder.story.model.Story;

/**
 *
 * @author Francesco Bertolino
 */
public class DropView extends VBox
{

    private final DoubleList itemsField;

    public DropView(final Drop drop)
    {
        setSpacing(10);
        setPadding(new Insets(10));

        final Story story = Cache.getInstance().getStory();
        itemsField = new ItemDoubleList(drop != null ? story.getItems(drop.getItemIds()) : new ArrayList<>());
        getChildren().add(itemsField);
    }

    public List<String> getItemIds()
    {
        return itemsField.getSelectedElementsIds();
    }

    public List<String> getIds()
    {
        final List<String> ids = new ArrayList<>(getItemIds());
        return ids;
    }

    public String[] getIdsArray()
    {
        return getIds().toArray(new String[0]);
    }
}
