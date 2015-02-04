package storybuilder.graph.view.connection;

import javafx.scene.paint.Color;
import storybuilder.graph.model.connection.LinkConnection;

/**
 *
 * @author Francesco Bertolino
 */
public class LinkConnectionView extends ConnectionView
{

    public LinkConnectionView(final LinkConnection connection, final double x1, final double y1,
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
    }

    private boolean isDirectLink()
    {
        return ((LinkConnection) getConnection()).getLink().isDirectLink();
    }

}
