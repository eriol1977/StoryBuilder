package storybuilder.event.view;

import java.util.List;
import storybuilder.event.model.Event;
import storybuilder.main.Cache;
import storybuilder.main.view.DoubleList;

/**
 *
 * @author Francesco Bertolino
 */
public class EventDoubleList extends DoubleList
{

    public EventDoubleList(final List<Event> rightItems)
    {
        super(rightItems);
    }

    @Override
    protected List<Event> loadLeftItems()
    {
        return Cache.getInstance().getStory().getEvents();
    }

}
