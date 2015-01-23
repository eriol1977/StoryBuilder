package storybuilder.item.view;

import java.util.List;
import storybuilder.item.model.Item;
import storybuilder.main.Cache;
import storybuilder.main.view.DoubleList;

/**
 *
 * @author Francesco Bertolino
 */
public class ItemDoubleList extends DoubleList
{

    public ItemDoubleList(final List<Item> rightItems)
    {
        super(rightItems);
    }

    @Override
    protected List<Item> loadLeftItems()
    {
        return Cache.getInstance().getStory().getItems();
    }

    @Override
    protected void createNewElement()
    {
        new NewItemDialog(getRightItems()).show();
    }

}
