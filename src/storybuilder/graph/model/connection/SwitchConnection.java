package storybuilder.graph.model.connection;

import storybuilder.graph.model.struct.LinkSwitchGraphData;
import storybuilder.graph.model.Node;

/**
 *
 * @author Francesco Bertolino
 */
public class SwitchConnection extends Connection
{

    private final LinkSwitchGraphData data;

    public SwitchConnection(final Node origin, final Node destination, final LinkSwitchGraphData data)
    {
        super(ConnectionKind.SWITCH, origin, destination);
        this.data = data;
    }

    @Override
    public String getDescription()
    {
        return data.getText();
    }

    public LinkSwitchGraphData getData()
    {
        return data;
    }

}
