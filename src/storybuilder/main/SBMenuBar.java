package storybuilder.main;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 *
 * @author Francesco Bertolino
 */
public class SBMenuBar extends MenuBar
{

    private final MainPane mainPane;

    SBMenuBar(final MainPane mainPane)
    {
        this.mainPane = mainPane;
        Menu menuStory = buildMenuStory();
        getMenus().addAll(menuStory);
    }

    private Menu buildMenuStory()
    {
        Menu menuStory = new Menu("Story");
        MenuItem newStory = buildMenuItem("New", KeyCode.N, "storybuilder.story.view.NewStoryView");
        MenuItem open = buildMenuItem("Open", KeyCode.O, "storybuilder.story.view.OpenStoryView");
        MenuItem save = buildMenuItem("Save", KeyCode.S, "storybuilder.story.view.SaveStoryView");
        MenuItem prefs = buildMenuItem("Preferences", KeyCode.P, "storybuilder.story.view.PreferencesView");
        menuStory.getItems().addAll(newStory, open, save, new SeparatorMenuItem(), prefs);
        return menuStory;
    }

    private MenuItem buildMenuItem(final String label, final KeyCode accelerator, final String clazz)
    {
        MenuItem menuItem = new MenuItem(label);
        menuItem.setAccelerator(new KeyCodeCombination(accelerator, KeyCombination.CONTROL_DOWN));
        menuItem.setOnAction((ActionEvent t) -> {
            try {
                mainPane.setContent((AbstractView) Class.forName(clazz).newInstance());
            } catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return menuItem;
    }

}
