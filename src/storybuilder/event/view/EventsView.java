package storybuilder.event.view;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TableColumn;
import storybuilder.event.model.Event;
import storybuilder.main.model.IStoryElement;
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
    protected void showDetailView(final boolean isNewElement, final IStoryElement element)
    {
        if (layout.getChildren().size() > 1) {
            layout.getChildren().remove(1);
        }
        stashed = new Event((Event) element);
        layout.getChildren().add(new EventDetailView(isNewElement, element, this));
    }

    @Override
    protected List<TableColumn> getColumns()
    {
        final List<TableColumn> columns = new ArrayList<>(1);
        columns.add(getColumn("Code", "nameWithoutPrefix", 150));
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
        data.addAll(cache.getStory().getEvents());
    }

}
