package storybuilder.graph.model.connection;

import storybuilder.graph.model.Node;
import storybuilder.section.model.Link;

/**
 *
 * @author Francesco Bertolino
 */
public class LinkConnection extends Connection
{

    private final Link link;

    public LinkConnection(final Node origin, final Node destination, final Link link)
    {
        super(ConnectionKind.LINK, origin, destination);
        this.link = link;
    }

    @Override
    public String getDescription()
    {
        return link.getReadableContent();
    }

    public Link getLink()
    {
        return link;
    }
}
