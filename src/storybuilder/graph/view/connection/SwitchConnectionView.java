package storybuilder.graph.view.connection;

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
        setStrokeWidth(2);
        getStrokeDashArray().addAll(25d, 10d);
    }

}
