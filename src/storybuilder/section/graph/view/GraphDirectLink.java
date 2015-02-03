package storybuilder.section.graph.view;

import java.util.List;
import storybuilder.section.model.Link;

/**
 *
 * @author Francesco Bertolino
 */
public class GraphDirectLink extends GraphLink
{

    public GraphDirectLink(final SectionNode origin, final SectionNode destination)
    {
        super(origin, destination);
    }

    @Override
    protected String getText()
    {
        final List<Link> links = getOrigin().getSection().getLinks();
        String text = "";
        for (final Link link : links) {
            if (link.getSectionId().equals(getDestination().getSection().getNameWithoutPrefix())) {
                text = link.getReadableContent();
                break;
            }
        }
        return text;
    }

}
