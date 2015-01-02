package storybuilder.story.model;

/**
 *
 * @author Francesco Bertolino
 */
public class Story {

    private final String title;
    
    private final String fileName;

    public Story(String title, String fileName)
    {
        this.title = title;
        this.fileName = fileName;
    }

    public String getTitle()
    {
        return title;
    }

    public String getFileName()
    {
        return fileName;
    }
    
}
