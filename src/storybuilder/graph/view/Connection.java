package storybuilder.graph.view;

import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public abstract class Connection extends Line
{

    private final Node origin;

    private final Node destination;

    public Connection(final Node origin, final Node destination)
    {
        this.origin = origin;
        this.destination = destination;

        setStartX(origin.getX());
        setStartY(origin.getY() + Node.RADIUS + 2);

        setEndX(destination.getX());
        setEndY(destination.getY() - Node.RADIUS);

        setStroke();

        Tooltip t = new Tooltip(getText());
        t.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
        Tooltip.install(this, t);

        setOnMouseEntered((MouseEvent event) -> {
            setCursor(Cursor.CROSSHAIR);
        });
    }

    protected abstract String getText();

    protected abstract void setStroke();

    public Node getOrigin()
    {
        return origin;
    }

    public Node getDestination()
    {
        return destination;
    }

}
