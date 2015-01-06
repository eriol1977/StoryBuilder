package storybuilder.validation;

/**
 *
 * @author Francesco Bertolino
 */
public class SBException extends Exception
{

    private final String failCause;

    public SBException(final String failCause)
    {
        this.failCause = failCause;
    }

    public String getFailCause()
    {
        return failCause;
    }
}
