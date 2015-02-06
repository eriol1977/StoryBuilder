package storybuilder.event.view;

import javafx.collections.ObservableList;
import storybuilder.event.model.Event;
import storybuilder.main.Cache;
import storybuilder.main.model.StoryElement;
import storybuilder.main.view.NewElementDialog;
import storybuilder.validation.SBException;

/**
 *
 * @author Francesco Bertolino
 */
public class NewEventDialog extends NewElementDialog
{

    public NewEventDialog(final ObservableList<StoryElement> itemList)
    {
        super(itemList, new EventDetailView(true, new Event("", "", false), null));
        setMinHeight(270);
        setTitle("New event");
    }

    @Override
    protected void saveElement(StoryElement element) throws SBException
    {
        Cache.getInstance().getStory().addEvent((Event) element);
    }

}
