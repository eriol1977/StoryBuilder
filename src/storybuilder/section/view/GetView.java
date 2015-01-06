package storybuilder.section.view;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import storybuilder.main.Cache;
import storybuilder.main.view.DoubleList;
import storybuilder.section.model.Get;

/**
 *
 * @author Francesco Bertolino
 */
public class GetView extends VBox
{

    private final DoubleList itemsField;

    private final DoubleList eventsField;

    public GetView(final Get get)
    {
        getChildren().add(new Label("Items to get"));

        itemsField = new DoubleList(Cache.getInstance().getStory().getItemIds(), get != null ? get.getItemIds() : new ArrayList<>());
        getChildren().add(itemsField);

        getChildren().add(new Label("Events to add"));

        eventsField = new DoubleList(Cache.getInstance().getStory().getEventIds(), get != null ? get.getEventIds() : new ArrayList<>());
        getChildren().add(eventsField);
    }

    public List<String> getItemIds()
    {
        return itemsField.getRightItems();
    }

    public List<String> getEventsIds()
    {
        return eventsField.getRightItems();
    }

    public List<String> getIds()
    {
        final List<String> ids = new ArrayList<>(getItemIds());
        ids.addAll(getEventsIds());
        return ids;
    }

    public String[] getIdsArray()
    {
        return getIds().toArray(new String[0]);
    }
}
