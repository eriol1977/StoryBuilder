package storybuilder.command.view;

import java.util.List;
import storybuilder.main.Cache;
import storybuilder.main.view.DoubleList;

/**
 *
 * @author Francesco Bertolino
 */
public class CommandDoubleList extends DoubleList
{

    public CommandDoubleList(final List<String> rightItems)
    {
        super(rightItems);
    }

    @Override
    protected List<String> loadLeftItems()
    {
        return Cache.getInstance().getStory().getCommandIds();
    }

}
