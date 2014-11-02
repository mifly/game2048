package com.minoq.game2048;

/**
 * Created by yichengfeng on 14-11-2.
 */
public class MoveResult {
    private boolean moved;
    private int score;
    private boolean won;

    public MoveResult(boolean moved, int score, boolean won) {
        this.moved = moved;
        this.score = score;
        this.won = won;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }
}
