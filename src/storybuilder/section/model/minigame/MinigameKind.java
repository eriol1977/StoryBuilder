package storybuilder.section.model.minigame;

import java.util.List;

/**
 *
 * @author Francesco Bertolino
 */
public class MinigameKind
{

    private final String code;

    private final String title;

    private final String clazz;

    private final List<MinigameParameter> parameters;

    public MinigameKind(String code, String title, String clazz, List<MinigameParameter> parameters)
    {
        this.code = code;
        this.title = title;
        this.clazz = clazz;
        this.parameters = parameters;
    }

    public String getCode()
    {
        return code;
    }

    public String getTitle()
    {
        return title;
    }

    public String getClazz()
    {
        return clazz;
    }

    public List<MinigameParameter> getParameters()
    {
        return parameters;
    }

}
