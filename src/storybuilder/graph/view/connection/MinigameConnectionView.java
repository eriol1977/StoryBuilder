package storybuilder.graph.view.connection;

import javafx.scene.paint.Color;
import storybuilder.graph.model.connection.MinigameConnection;

/**
 *
 * @author Francesco Bertolino
 */
public class MinigameConnectionView extends ConnectionView
{

    public MinigameConnectionView(final MinigameConnection connection, final double x1, final double y1,
            final double x2, final double y2)
    {
        super(connection, x1, y1, x2, y2);
    }

    @Override
    protected void setStroke()
    {
        if (gameWon()) {
            setStroke(Color.GREEN);
        } else {
            setStroke(Color.RED);
        }
        setStrokeWidth(2);
        getStrokeDashArray().addAll(2d, 10d);
    }

    private boolean gameWon()
    {
        return ((MinigameConnection) getConnection()).getData().isWinning();
    }

}
