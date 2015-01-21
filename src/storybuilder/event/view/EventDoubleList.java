package storybuilder.event.view;

import java.util.List;
import storybuilder.main.Cache;
import storybuilder.main.view.DoubleList;

/**
 *
 * @author Francesco Bertolino
 */
public class EventDoubleList extends DoubleList
{

    public EventDoubleList(final List<String> rightItems)
    {
        super(rightItems);
    }

    @Override
    protected List<String> loadLeftItems()
    {
        return Cache.getInstance().getStory().getEventIds();
    }

}
