package storybuilder.graph.model.connection;

import storybuilder.graph.model.Node;

public abstract class Connection implements Comparable<Connection>
{

    private final ConnectionKind kind;

    private final Node origin;

    private final Node destination;

    public Connection(final ConnectionKind kind, final Node origin, final Node destination)
    {
        this.kind = kind;
        this.origin = origin;
        this.destination = destination;
    }

    public abstract String getDescription();

    public Node getOrigin()
    {
        return origin;
    }

    public Node getDestination()
    {
        return destination;
    }

    @Override
    public int compareTo(final Connection another)
    {
        final int compareOrigins = origin.compareTo(another.getOrigin());
        if (compareOrigins != 0) {
            return compareOrigins;
        }
        return destination.compareTo(another.getDestination());
    }

    public ConnectionKind getKind()
    {
        return kind;
    }

}
