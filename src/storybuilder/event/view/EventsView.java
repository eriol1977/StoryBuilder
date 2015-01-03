package storybuilder.event.view;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TableColumn;
import storybuilder.event.model.Event;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractTableView;

/**
 *
 * @author Francesco Bertolino
 */
public class EventsView extends AbstractTableView
{

    public EventsView()
    {
        addTitle("Events");
    }

    @Override
    protected IStoryElement getNewElement()
    {
        return new Event("", "", false);
    }

    @Override
    protected void showDetailView(final IStoryElement element)
    {
        if (layout.getChildren().size() > 1) {
            layout.getChildren().remove(1);
        }
        stashed = new Event((Event) element);
        layout.getChildren().add(new EventDetailView(element, this));
    }

    @Override
    protected List<TableColumn> getColumns()
    {
        final List<TableColumn> columns = new ArrayList<>(1);
        columns.add(getColumn("Name", "nameWithoutPrefix", 150));
        return columns;
    }

    @Override
    protected boolean addElementToStory(final IStoryElement element)
    {
        return cache.getStory().addEvent((Event) element);
    }

    @Override
    protected boolean updateElementInStory(final IStoryElement element)
    {
        return cache.getStory().updateEvent((Event) element);
    }

    @Override
    protected boolean deleteElementFromStory(final IStoryElement element)
    {
        return cache.getStory().removeEvent((Event) element);
    }

    @Override
    protected void loadData()
    {
        data.addAll(cache.getStory().getEvents());
    }

}
