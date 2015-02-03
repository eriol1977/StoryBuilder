package storybuilder.section.graph.view;

import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author Francesco Bertolino
 */
public abstract class GraphLink extends Line
{

    private final SectionNode origin;

    private final SectionNode destination;

    public GraphLink(final SectionNode origin, final SectionNode destination)
    {
        this.origin = origin;
        this.destination = destination;

        setStartX(origin.getX());
        setStartY(origin.getY() + SectionNode.RADIUS + 2);

        setEndX(destination.getX());
        setEndY(destination.getY() - SectionNode.RADIUS);

        setStrokeWidth(2);

        Tooltip t = new Tooltip(getText());
        t.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
        Tooltip.install(this, t);

        setOnMouseEntered((MouseEvent event) -> {
            setCursor(Cursor.CROSSHAIR);
        });
    }

    protected abstract String getText();

    public SectionNode getOrigin()
    {
        return origin;
    }

    public SectionNode getDestination()
    {
        return destination;
    }

}
