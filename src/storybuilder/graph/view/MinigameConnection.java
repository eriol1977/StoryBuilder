package storybuilder.graph.view;

import storybuilder.graph.model.MinigameGraphData;

/**
 *
 * @author Francesco Bertolino
 */
public class MinigameConnection extends Connection
{

    private final MinigameGraphData data;

    public MinigameConnection(final Node origin, final Node destination, final MinigameGraphData data)
    {
        super(origin, destination);
        this.data = data;
    }

    @Override
    protected String getText()
    {
        return data.getText();
    }

    @Override
    protected void setStroke()
    {
        setStrokeWidth(2);
        // TODO
    }

}
