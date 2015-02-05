package storybuilder.graph.model;

import storybuilder.item.model.Item;
import storybuilder.section.model.Section;

/**
 *
 * @author Francesco Bertolino
 */
public class Node implements Comparable<Node>
{

    private final Section section;

    public Node(Section section)
    {
        this.section = section;
    }

    public Section getSection()
    {
        return section;
    }

    public String getTitle()
    {
        return section.getNameWithoutPrefix();
    }

    public String getDescription()
    {
        final StringBuilder sb = new StringBuilder();
        section.getParagraphs().stream().forEach(p -> sb.append(p.getText()).append("\n"));
        final Item item = getItem();
        if (item == null) {
            sb.delete(sb.length() - 1, sb.length()); // removes last \n
        } else {
            sb.append("[This section describes item '")
                    .append(item.getItemName())
                    .append("' for examination]");
        }
        return sb.toString();
    }

    public boolean isStarting()
    {
        return section.getNameWithoutPrefix().equals("1");
    }

    public boolean isEnding()
    {
        return section.isEnding();
    }

    public Item getItem()
    {
        return section.getItem();
    }

    @Override
    public int compareTo(final Node another)
    {
        return section.compareTo(another.getSection());
    }

}
