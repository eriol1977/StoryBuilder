package storybuilder.command.model;

import java.util.Comparator;

/**
 *
 * @author Francesco Bertolino
 */
public class CommandComparatorByDescription implements Comparator<Command>
{

    @Override
    public int compare(Command c1, Command c2)
    {
        return c1.getDescription().compareTo(c2.getDescription());
    }

}
