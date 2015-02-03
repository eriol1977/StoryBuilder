package storybuilder.graph.view;

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
        super(origin, destination);
        this.link = link;
    }

    @Override
    protected String getText()
    {
        return link.getReadableContent();
    }

    @Override
    protected void setStroke()
    {
        setStrokeWidth(2);
    }

}
