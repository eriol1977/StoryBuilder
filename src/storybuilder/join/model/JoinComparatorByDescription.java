package storybuilder.join.model;

import java.util.Comparator;

/**
 *
 * @author Francesco Bertolino
 */
public class JoinComparatorByDescription implements Comparator<Join>
{

    @Override
    public int compare(Join j1, Join j2)
    {
        return j1.getDescription().compareTo(j2.getDescription());
    }

}
