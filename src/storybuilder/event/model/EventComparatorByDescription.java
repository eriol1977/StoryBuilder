package storybuilder.event.model;

import java.util.Comparator;

/**
 *
 * @author Francesco Bertolino
 */
public class EventComparatorByDescription implements Comparator<Event>
{

    @Override
    public int compare(Event e1, Event e2)
    {
        return e1.getDescription().compareTo(e2.getDescription());
    }

}
