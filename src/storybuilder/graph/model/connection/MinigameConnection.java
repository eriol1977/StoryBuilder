package storybuilder.graph.model.connection;

import storybuilder.graph.model.struct.MinigameGraphData;
import storybuilder.graph.model.Node;

/**
 *
 * @author Francesco Bertolino
 */
public class MinigameConnection extends Connection
{

    private final MinigameGraphData data;

    public MinigameConnection(final Node origin, final Node destination, final MinigameGraphData data)
    {
        super(ConnectionKind.MINIGAME, origin, destination);
        this.data = data;
    }

    @Override
    public String getDescription()
    {
        return data.getText();
    }

    public MinigameGraphData getData()
    {
        return data;
    }

}
