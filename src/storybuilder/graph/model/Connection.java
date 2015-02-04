package storybuilder.graph.model;

public abstract class Connection implements Comparable<Connection>
{

    private final Node origin;

    private final Node destination;

    public Connection(final Node origin, final Node destination)
    {
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

}
