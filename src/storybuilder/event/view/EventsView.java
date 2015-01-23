package storybuilder.event.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.control.TableColumn;
import storybuilder.event.model.Event;
import storybuilder.event.model.EventComparatorByDescription;
import storybuilder.main.model.IStoryElement;
import storybuilder.main.view.AbstractDetailView;
import storybuilder.main.view.AbstractTableView;
import storybuilder.validation.SBException;

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
    protected AbstractDetailView showDetailView(final boolean isNewElement, final IStoryElement element)
    {
        if (layout.getChildren().size() > 1) {
            layout.getChildren().remove(1);
        }
        stashed = new Event((Event) element);
        final EventDetailView eventDetailView = new EventDetailView(isNewElement, element, this);
        layout.getChildren().add(eventDetailView);
        return eventDetailView;
    }

    @Override
    protected List<TableColumn> getColumns()
    {
        final List<TableColumn> columns = new ArrayList<>(1);
        columns.add(getColumn("Desc.", "description", 160));
        return columns;
    }

    @Override
    protected void addElementToStory(final IStoryElement element) throws SBException
    {
        cache.getStory().addEvent((Event) element);
    }

    @Override
    protected void updateElementInStory(final IStoryElement element) throws SBException
    {
        cache.getStory().updateEvent((Event) element);
    }

    @Override
    protected void deleteElementFromStory(final IStoryElement element) throws SBException
    {
        cache.getStory().removeEvent((Event) element);
    }

    @Override
    protected void loadData()
    {
        final List<Event> events = cache.getStory().getEvents();
        Collections.sort(events, new EventComparatorByDescription());
        data.addAll(events);
    }

}
