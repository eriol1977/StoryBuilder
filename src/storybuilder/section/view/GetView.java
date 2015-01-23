package storybuilder.section.view;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import storybuilder.event.view.EventDoubleList;
import storybuilder.item.view.ItemDoubleList;
import storybuilder.main.Cache;
import storybuilder.main.view.DoubleList;
import storybuilder.section.model.Get;
import storybuilder.section.model.Section;
import storybuilder.story.model.Story;

/**
 *
 * @author Francesco Bertolino
 */
public class GetView extends HBox
{

    private final DoubleList itemsField;

    private final DoubleList eventsField;

    public GetView(final Section section)
    {
        final Get get = section.getGet();

        setSpacing(10);
        setPadding(new Insets(10));

        getChildren().add(new Label("Items:"));

        final Story story = Cache.getInstance().getStory();

        itemsField = new ItemDoubleList(get != null ? story.getItems(get.getItemIds()) : new ArrayList<>());
        getChildren().add(itemsField);

        final Separator separator = new Separator(Orientation.VERTICAL);
        separator.setPadding(new Insets(0, 30, 0, 30));
        getChildren().add(separator);

        getChildren().add(new Label("Events:"));

        eventsField = new EventDoubleList(get != null ? story.getEvents(get.getEventIds()) : new ArrayList<>());
        getChildren().add(eventsField);
    }

    public List<String> getItemIds()
    {
        return itemsField.getSelectedElementsIds();
    }

    public List<String> getEventsIds()
    {
        return eventsField.getSelectedElementsIds();
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
