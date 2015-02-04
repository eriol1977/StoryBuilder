package storybuilder.graph.view.connection;

import javafx.scene.paint.Color;
import storybuilder.graph.model.connection.SwitchConnection;

/**
 *
 * @author Francesco Bertolino
 */
public class SwitchConnectionView extends ConnectionView
{

    public SwitchConnectionView(final SwitchConnection connection, final double x1, final double y1,
            final double x2, final double y2)
    {
        super(connection, x1, y1, x2, y2);
    }

    @Override
    protected void setStroke()
    {
        if (isDirectLink()) {
            setStroke(Color.GREEN);
        }
        setStrokeWidth(2);
        getStrokeDashArray().addAll(25d, 10d);
    }

    private boolean isDirectLink()
    {
        return ((SwitchConnection) getConnection()).getData().getLink().isDirectLink();
    }
}
