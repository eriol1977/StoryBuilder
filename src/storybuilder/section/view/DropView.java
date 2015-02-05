package storybuilder.section.view;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import storybuilder.item.view.ItemDoubleList;
import storybuilder.main.Cache;
import storybuilder.main.view.DoubleList;
import storybuilder.main.view.Observer;
import storybuilder.section.model.Drop;
import storybuilder.story.model.Story;

/**
 *
 * @author Francesco Bertolino
 */
public class DropView extends VBox implements Observer
{

    private final SectionDetailView view;
    
    private final DoubleList itemsField;

    public DropView(final SectionDetailView view, final Drop drop)
    {
        this.view = view;
        
        setSpacing(10);
        setPadding(new Insets(10));

        final Story story = Cache.getInstance().getStory();
        itemsField = new ItemDoubleList(drop != null ? story.getItems(drop.getItemIds()) : new ArrayList<>());
        itemsField.setObserver(this);
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

    @Override
    public void somethingHappened()
    {
        view.save();
    }
}
