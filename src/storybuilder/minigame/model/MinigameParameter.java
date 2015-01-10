package storybuilder.minigame.model;

import java.util.Objects;

/**
 *
 * @author Francesco Bertolino
 */
public class MinigameParameter
{

    private final String definition;

    private final String obs;

    private final String placeHolder;

    public MinigameParameter(final String definition, final String obs, final String placeHolder)
    {
        this.definition = definition;
        this.obs = obs;
        this.placeHolder = placeHolder;
    }

    public String getDefinition()
    {
        return definition;
    }

    public String getObs()
    {
        return obs;
    }

    public String getPlaceHolder()
    {
        return placeHolder;
    }

    @Override
    public String toString()
    {
        return definition + " (" + obs + " - ex: '" + placeHolder + "')";
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.definition);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MinigameParameter other = (MinigameParameter) obj;
        return Objects.equals(this.definition, other.definition);
    }

}
