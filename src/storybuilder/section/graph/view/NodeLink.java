package storybuilder.section.graph.view;

import java.util.List;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Line;
import storybuilder.section.model.Link;

/**
 *
 * @author Francesco Bertolino
 */
public class NodeLink extends Line
{

    private final SectionNode origin;

    private final SectionNode destination;

    public NodeLink(SectionNode origin, SectionNode destination)
    {
        this.origin = origin;
        this.destination = destination;

        setStartX(origin.getX());
        setStartY(origin.getY() + SectionNode.RADIUS + 2);

        setEndX(destination.getX());
        setEndY(destination.getY() - SectionNode.RADIUS);

        setStrokeWidth(2);

        final List<Link> links = origin.getSection().getLinks();
        String text = "";
        for (final Link link : links) {
            if (link.getSectionId().equals(destination.getSection().getNameWithoutPrefix())) {
                text = link.getReadableContent();
                break;
            }
        }
        Tooltip t = new Tooltip(text);
        Tooltip.install(this, t);
    }

    public SectionNode getOrigin()
    {
        return origin;
    }

    public SectionNode getDestination()
    {
        return destination;
    }

}
