package storybuilder.graph.model;

import storybuilder.section.model.MinigameInstance;

/**
 *
 * @author Francesco Bertolino
 */
public class MinigameGraphData {

    private final MinigameInstance game;
    
    private final boolean winning;

    public MinigameGraphData(MinigameInstance game, boolean winning)
    {
        this.game = game;
        this.winning = winning;
    }

    public MinigameInstance getGame()
    {
        return game;
    }

    public boolean isWinning()
    {
        return winning;
    }
    
}
