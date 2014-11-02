package com.minoq.game2048;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yichengfeng on 14-11-2.
 */
public class Tile {
    private int x;
    private int y;
    private int value;
    private Tile[] mergeFrom;
    private Map<String,Integer> previousPosition;

    public Tile(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Tile[] getMergeFrom() {
        return mergeFrom;
    }

    public void setMergeFrom(Tile[] mergeFrom) {
        this.mergeFrom = mergeFrom;
    }

    public void savePosition() {
        previousPosition = new HashMap<>(2);
        previousPosition.put("x", Integer.valueOf(this.getX()));
        previousPosition.put("y", Integer.valueOf(this.getY()));
    }

    public void updatePosition(Tile to) {
        setX(to.getX());
        setY(to.getY());
    }
}
