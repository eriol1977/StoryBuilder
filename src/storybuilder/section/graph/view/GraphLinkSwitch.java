package storybuilder.section.graph.view;

import java.util.List;
import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import storybuilder.section.model.Link;

/**
 *
 * @author Francesco Bertolino
 */
public class GraphLinkSwitch extends Line
{

    final static int LINK = 1;

    final static int LINK_SWITCH = 2;

    private final SectionNode origin;

    private final SectionNode destination;

    public GraphLinkSwitch(final SectionNode origin, final SectionNode destination, final int kind)
    {
        this.origin = origin;
        this.destination = destination;

        setStartX(origin.getX());
        setStartY(origin.getY() + SectionNode.RADIUS + 2);

        setEndX(destination.getX());
        setEndY(destination.getY() - SectionNode.RADIUS);

        setStrokeWidth(2);

        if (kind == LINK_SWITCH) {
            getStrokeDashArray().addAll(25d, 10d);
        }

        final String destinationId = destination.getSection().getNameWithoutPrefix();

        final List<Link> links = origin.getSection().getLinks();
        String text = "";
        for (final Link link : links) {
            if (link.getSectionId().equals(destinationId)) {
                text = link.getReadableContent();
                break;
            }
        }

//        // link switch
//        if (text.isEmpty()) {
//            final List<LinkSwitch> linkSwitches = origin.getSection().getLinkSwitches();
//            for (final LinkSwitch linkSwitch : linkSwitches) {
//                if (linkSwitch.getLink() != null && linkSwitch.getLink().getSectionId().equals(destinationId)) {
//                    text = linkSwitch.getReadableContent();
//                    getStrokeDashArray().addAll(25d, 10d);
//                    break;
//                }
//            }
//        }
        Tooltip t = new Tooltip(text);
        t.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
        Tooltip.install(this, t);

        setOnMouseEntered((MouseEvent event) -> {
            setCursor(Cursor.CROSSHAIR);
        });
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
