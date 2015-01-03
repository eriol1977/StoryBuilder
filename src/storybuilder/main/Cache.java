package storybuilder.main;

import storybuilder.command.model.Command;
import storybuilder.preferences.model.Preferences;
import storybuilder.story.model.NullStory;
import storybuilder.story.model.Story;

/**
 *
 * @author Francesco Bertolino
 */
public class Cache
{
    
    private final static Cache instance = new Cache();
    
    private Story story;
    
    private final Story nullStory = new NullStory();
    
    private final Preferences preferences = new Preferences();
    
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
        // TODO caricare da xml di configurazione
    }
    
    public void addCommand(final Command command)
    {
        getStory().addCommand(command);
    }
    
    public void removeCommand(final Command command)
    {
        getStory().removeCommand(command);
    }
    
    public void updateCommand(final Command command)
    {
        getStory().updateCommand(command);
    }
}
