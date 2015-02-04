package storybuilder.graph.view.connection;

import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import storybuilder.graph.model.connection.Connection;
import storybuilder.graph.view.NodeView;

/**
 *
 * @author Francesco Bertolino
 */
public abstract class ConnectionView extends Line
{

    private final Connection connection;

    public ConnectionView(final Connection connection, final double x1, final double y1,
            final double x2, final double y2)
    {
        this.connection = connection;

        initShape(x1, y1, x2, y2);
        initTooltip(connection);
        initEventListeners();
    }

    private void initShape(final double x1, final double y1, final double x2, final double y2)
    {
        setStartX(x1);
        setStartY(y1 + NodeView.RADIUS + 2);

        setEndX(x2);
        setEndY(y2 - NodeView.RADIUS);

        setStroke();
    }

    private void initTooltip(final Connection connection1)
    {
        final Tooltip t = new Tooltip(connection1.getDescription());
        t.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
        Tooltip.install(this, t);
    }

    private void initEventListeners()
    {
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
