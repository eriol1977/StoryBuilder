package storybuilder.graph.view;

import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import storybuilder.graph.model.Connection;

/**
 *
 * @author Francesco Bertolino
 */
public abstract class ConnectionView extends Line
{

    private final Connection connection;

    public ConnectionView(final Connection connection)
    {
        this.connection = connection;

//        setStartX(connection.getOrigin().getX());
//        setStartY(connection.getOrigin().getY() + NodeView.RADIUS + 2);
//
//        setEndX(connection.getDestination().getX());
//        setEndY(connection.getDestination().getY() - NodeView.RADIUS);

        setStroke();

        Tooltip t = new Tooltip(connection.getDescription());
        t.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
        Tooltip.install(this, t);

        setOnMouseEntered((MouseEvent event) -> {
            setCursor(Cursor.CROSSHAIR);
        });
    }

    protected abstract void setStroke();

    public Connection getConnection()
    {
        return connection;
    }

}
