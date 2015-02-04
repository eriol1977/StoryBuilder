package storybuilder.graph.model;

/**
 *
 * @author Francesco Bertolino
 */
public class SwitchConnection extends Connection
{

    private final LinkSwitchGraphData data;

    public SwitchConnection(final Node origin, final Node destination, final LinkSwitchGraphData data)
    {
        super(origin, destination);
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
