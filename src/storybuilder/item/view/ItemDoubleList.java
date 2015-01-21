package storybuilder.item.view;

import java.util.List;
import storybuilder.main.Cache;
import storybuilder.main.view.DoubleList;

/**
 *
 * @author Francesco Bertolino
 */
public class ItemDoubleList extends DoubleList
{

    public ItemDoubleList(final List<String> rightItems)
    {
        super(rightItems);
    }

    @Override
    protected List<String> loadLeftItems()
    {
        return Cache.getInstance().getStory().getItemIds();
    }

}
