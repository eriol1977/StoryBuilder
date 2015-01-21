package storybuilder.command.view;

import java.util.List;
import storybuilder.command.model.Command;
import storybuilder.main.Cache;
import storybuilder.main.view.DoubleList;

/**
 *
 * @author Francesco Bertolino
 */
public class CommandDoubleList extends DoubleList
{

    public CommandDoubleList(final List<Command> rightItems)
    {
        super(rightItems);
    }

    @Override
    protected List<Command> loadLeftItems()
    {
        return Cache.getInstance().getStory().getCommands();
    }

}
