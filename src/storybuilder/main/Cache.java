package storybuilder.main;

import storybuilder.preferences.model.Preferences;
import storybuilder.story.model.NullStory;
import storybuilder.story.model.Story;
import storybuilder.validation.ErrorManager;
import storybuilder.validation.SBException;

/**
 *
 * @author Francesco Bertolino
 */
public class Cache
{

    private final static Cache instance = new Cache();

    private Story story;

    private final Story nullStory = new NullStory();

    private Preferences preferences;

    private Cache()
    {
        loadPreferences();
    }

    public final static Cache getInstance()
    {
        return instance;
    }

    public Story getStory()
    {
        return story != null ? story : nullStory;
    }

    public void setStory(Story story)
    {
        this.story = story;
    }

    public Preferences getPreferences()
    {
        return preferences;
    }

    private void loadPreferences()
    {
        try {
            preferences = new Preferences();
        } catch (SBException ex) {
            ErrorManager.showErrorMessage(ex.getFailCause());
        }
    }

}
