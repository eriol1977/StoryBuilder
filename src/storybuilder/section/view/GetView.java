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
import storybuilder.main.view.Observer;
import storybuilder.section.model.Get;
import storybuilder.section.model.Section;
import storybuilder.story.model.Story;

/**
 *
 * @author Francesco Bertolino
 */
public class GetView extends HBox implements Observer
{
    private final SectionDetailView view;
    
    private final DoubleList itemsField;

    private final DoubleList eventsField;

    public GetView(final SectionDetailView view, final Section section)
    {
        this.view = view;
        final Get get = section.getGet();

        setSpacing(10);
        setPadding(new Insets(10));

        getChildren().add(new Label("Items:"));

        final Story story = Cache.getInstance().getStory();

        itemsField = new ItemDoubleList(get != null ? story.getItems(get.getItemIds()) : new ArrayList<>());
        itemsField.setObserver(this);
        getChildren().add(itemsField);

        final Separator separator = new Separator(Orientation.VERTICAL);
        separator.setPadding(new Insets(0, 30, 0, 30));
        getChildren().add(separator);

        getChildren().add(new Label("Events:"));

        eventsField = new EventDoubleList(get != null ? story.getEvents(get.getEventIds()) : new ArrayList<>());
        eventsField.setObserver(this);
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

    @Override
    public void somethingHappened()
    {
        view.save();
    }
}
