package storybuilder.item.model;

import java.util.Comparator;

/**
 *
 * @author Francesco Bertolino
 */
public class ItemComparatorByItemName implements Comparator<Item>
{

    @Override
    public int compare(Item i1, Item i2)
    {
        return i1.getItemName().compareTo(i2.getItemName());
    }

}
