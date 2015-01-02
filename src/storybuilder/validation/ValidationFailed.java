package storybuilder.validation;

/**
 *
 * @author Francesco Bertolino
 */
public class ValidationFailed extends Exception
{

    private final String failCause;

    public ValidationFailed(final String failCause)
    {
        this.failCause = failCause;
    }

    public String getFailCause()
    {
        return failCause;
    }

}
