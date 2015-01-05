package storybuilder.main.view;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.Label;
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

    private final Menu menuStory;

    private final Menu menuCommands;

    private final Menu menuEvents;

    private final Menu menuItems;

    private final Menu menuSections;

    SBMenuBar(final MainPane mainPane)
    {
        this.mainPane = mainPane;
        menuStory = buildMenuStory();
        menuCommands = buildMenuButton("Commands", KeyCode.O, "storybuilder.command.view.CommandsView");
        menuEvents = buildMenuButton("Events", KeyCode.E, "storybuilder.event.view.EventsView");
        menuItems = buildMenuButton("Items", KeyCode.I, "storybuilder.item.view.ItemsView");
        menuSections = buildMenuButton("Sections", KeyCode.S, "storybuilder.section.view.SectionsView");
        getMenus().addAll(menuStory, menuCommands, menuEvents, menuItems, menuSections);
        enableMenus(false);
    }

    private Menu buildMenuStory()
    {
        final Menu menu = new Menu("Story");
        final MenuItem newStory = buildMenuItem("New", KeyCode.N, "storybuilder.story.view.NewStoryView");
        final MenuItem open = buildMenuItem("Open", KeyCode.O, "storybuilder.story.view.OpenStoryView");
        final MenuItem delete = buildMenuItem("Delete", KeyCode.D, "storybuilder.story.view.DeleteStoryView");
        final MenuItem prefs = buildMenuItem("Preferences", KeyCode.P, "storybuilder.preferences.view.PreferencesView");
        menu.getItems().addAll(newStory, open, delete, new SeparatorMenuItem(), prefs);
        return menu;
    }

    private Menu buildMenuButton(final String label, final KeyCode accelerator, final String clazz)
    {
        final Menu menu = new Menu();
        menu.setAccelerator(new KeyCodeCombination(accelerator, KeyCombination.CONTROL_DOWN));
        final Label menuLabel = new Label(label); // to make menu header clickable
        menuLabel.setOnMouseClicked((Event t) -> {
            try {
                mainPane.setContent((AbstractView) Class.forName(clazz).newInstance());
            } catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        menu.setGraphic(menuLabel);
        return menu;
    }

    private MenuItem buildMenuItem(final String label, final KeyCode accelerator, final String clazz)
    {
        final MenuItem menuItem = new MenuItem(label);
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

    final void enableMenus(boolean enable)
    {
        menuCommands.setDisable(!enable);
        menuEvents.setDisable(!enable);
        menuItems.setDisable(!enable);
        menuSections.setDisable(!enable);
    }

}
