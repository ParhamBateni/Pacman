package Sample.Model;

import Sample.View.GamePage;

import java.util.TimerTask;

public class DeadGhostTimer extends TimerTask {

    private final GamePage gamePage;

    public DeadGhostTimer(GamePage gamePage) {
        this.gamePage = gamePage;

    }

    @Override
    public void run() {
        gamePage.deActivateBomb();
    }
}
