package com.minoq.game2048;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by yichengfeng on 14-11-2.
 */
public class GameManager {
    private int score = 0;
    private Grid grid;

    public static void main(String[] ars) {
        GameManager gameManager = new GameManager();
        gameManager.setUp();
        gameManager.start();
    }

    private void start() {
        this.getGrid().setPlayerTurn(true);
        System.out.println(getGrid().toString());

        while (!this.terminate()) {
            if (getGrid().isPlayerTurn()) {
                System.out.println("please enter l/r/u/d.");
                int direction = getInput();
                System.out.println("direction:" + direction);
                MoveResult moveResult = getGrid().move(direction);
                this.score += moveResult.getScore();
                System.out.println("score:" + score);
                System.out.println(getGrid().toString());
            } else {
                getGrid().computerMove();
                System.out.println("computer move.");
                System.out.println(getGrid().toString());
            }
        }
    }

    private int getInput() {
        BufferedReader stdin =new BufferedReader(new InputStreamReader(System.in));
        try {
            String instring = stdin.readLine();
            if ("l".equalsIgnoreCase(instring)) {
                return Grid.LEFT_DIRECTION;
            }
            else if ("r".equalsIgnoreCase(instring)) {
                return Grid.RIGHT_DIRECTION;
            }
            else if ("u".equalsIgnoreCase(instring)) {
                return Grid.UP_DIRECTION;
            }
            else if ("d".equalsIgnoreCase(instring)) {
                return Grid.DOWN_DIRECTION;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private boolean terminate() {
        return false;
    }

    private void setUp() {
        Grid grid1 = new Grid();
        grid1.addStartTile();
        this.setGrid(grid1);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }
}
