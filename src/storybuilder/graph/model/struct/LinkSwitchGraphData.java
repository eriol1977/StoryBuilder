package storybuilder.graph.model.struct;

import storybuilder.section.model.Link;
import storybuilder.section.model.Section;

/**
 *
 * @author Francesco Bertolino
 */
public class LinkSwitchGraphData
{

    private final Section activatingSection;

    private final Link link;

    public LinkSwitchGraphData(final Section activatingSection, final Link link)
    {
        this.activatingSection = activatingSection;
        this.link = link;
    }

    public Section getActivatingSection()
    {
        return activatingSection;
    }

    public Link getLink()
    {
        return link;
    }

    public String getText()
    {
        return link.getReadableContent()
                + " [switch activated by section " + activatingSection.getNameWithoutPrefix() + "]";
    }

}
