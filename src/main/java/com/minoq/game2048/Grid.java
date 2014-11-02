package com.minoq.game2048;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yichengfeng on 14-11-2.
 */
public class Grid {
    public static final int UP_DIRECTION = 0;
    public static final int RIGHT_DIRECTION = 1;
    public static final int DOWN_DIRECTION = 2;
    public static final int LEFT_DIRECTION = 3;
    private Tile[][] cells;
    private boolean playerTurn;
    private Map<Integer, Vector> vectorMap;
    private int startTileNumber = 2;


    public Grid() {
        initCells();
        initVector();
    }

    public Grid(int[][] numbers) {
        initVector();
        cells = new Tile[4][4];
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                Tile tile = new Tile(x, y, numbers[x][y]);
                cells[x][y] = tile;
            }
        }
    }

    private void initVector() {
        vectorMap = new HashMap<Integer, Vector>(4);
        vectorMap.put(Integer.valueOf(UP_DIRECTION), new Vector(0, 1)); // up  0, 1
        vectorMap.put(Integer.valueOf(RIGHT_DIRECTION), new Vector(1, 0)); // right
        vectorMap.put(Integer.valueOf(DOWN_DIRECTION), new Vector(0, -1)); // down 0 -1
        vectorMap.put(Integer.valueOf(LEFT_DIRECTION), new Vector(-1, 0)); // left
    }

    private void initCells() {
        cells = new Tile[4][4];
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                Tile tile = new Tile(x, y, 0);
                cells[x][y] = tile;
            }
        }
    }

    public Tile indexs(int x, int y) {
        return isWithin(x, y) ? cells[x][y] : null;
    }

    public int smoothness() {
        int smoothness = 0;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (cellOccupied(indexs(x, y))) {
                    Tile tile = indexs(x, y);
                    double value1 = Math.log(tile.getValue()) / Math.log(2);
                    for (int direction = RIGHT_DIRECTION;
                         direction <= DOWN_DIRECTION; direction++) {
                        Vector vector = this.vectorMap.get(Integer.valueOf(direction));
                        Map<String, Tile> farthest = this.getFarthest(this.indexs(x, y), vector);
                        Tile nextCell = farthest.get("next");
                        if (nextCell != null && this.cellOccupied(nextCell)) {
                            double targetValue = Math.log(nextCell.getValue()) / Math.log(2);
                            smoothness -= Math.abs(value1 - targetValue);
                        }

                    }
                }
            }
        }
        return smoothness;
    }

    public double monotonicity() {
        double[] totals = new double[4];
        // up/down
        for (int x = 0; x < 4; x++) {
            int current = 0;
            int next = current + 1;
            while (next < 4) {
                while (next < 4 && this.isEmpty(this.indexs(x, next))) {
                    next++;
                }
                if (next >= 4)
                    next--;
                Tile currentCell =
                    (this.cellOccupied(this.indexs(x, current))) ? this.indexs(x, current) : null;
                Tile nextCell =
                    (this.cellOccupied(this.indexs(x, next))) ? this.indexs(x, next) : null;
                double currentValue =
                    currentCell != null ? (Math.log(currentCell.getValue()) / Math.log(2)) : 0;
                double nextValue =
                    nextCell != null ? (Math.log(nextCell.getValue()) / Math.log(2)) : 0;
                if (currentValue > nextValue) {
                    totals[0] += nextValue - currentValue;
                } else if (nextValue > currentValue) {
                    totals[1] += current - nextValue;
                }
                current = next;
                next++;
            }
        }

        for (int y = 0; y < 4; y++) {
            int current = 0;
            int next = current + 1;
            while (next < 4) {
                while (next < 4 && this.isEmpty(this.indexs(next, y))) {
                    next++;
                }
                if (next >= 4)
                    next--;
                Tile currentCell =
                    (this.cellOccupied(this.indexs(current, y))) ? this.indexs(current, y) : null;
                Tile nextCell =
                    (this.cellOccupied(this.indexs(next, y))) ? this.indexs(next, y) : null;
                double currentValue =
                    currentCell != null ? (Math.log(currentCell.getValue()) / Math.log(2)) : 0;
                double nextValue =
                    nextCell != null ? (Math.log(nextCell.getValue()) / Math.log(2)) : 0;
                if (currentValue > nextValue) {
                    totals[2] += nextValue - currentValue;
                } else if (nextValue > currentValue) {
                    totals[3] += current - nextValue;
                }
                current = next;
                next++;
            }
        }
        return 0;
    }

    private Map<String, Tile> getFarthest(Tile cell, Vector vector) {
        Tile previous;
        int nextX = 0;
        int nextY = 0;
        do {
            previous = cell;
            nextX = previous.getX() + vector.getX();
            nextY = previous.getY() + vector.getY();
            cell = this.indexs(nextX, nextY);
        }
        while (this.isWithin(nextX, nextY) && this.isEmpty(nextX, nextY));
        Tile next = indexs(nextX, nextY);
        Map<String, Tile> result = new HashMap<>(2);
        result.put("farthest", previous);
        result.put("next", next);
        return result;
    }

    public MoveResult move(int direction) {
        Vector vector = this.vectorMap.get(Integer.valueOf(direction));
        int[] traversalsX = getTraversals(vector.getX() == 1);
        int[] traversalsY = getTraversals(vector.getY() == 1);
        int score = 0;
        boolean won = false;
        boolean moved = false;

        prepareTiles();

        for (int i = 0; i < traversalsX.length; i++) {
            int x = traversalsX[i];
            for (int j = 0; j < traversalsY.length; j++) {
                int y = traversalsX[j];
                Tile tile = indexs(x, y);
                if (tile != null && tile.getValue() > 0) {
                    Map<String, Tile> farthest = this.getFarthest(tile, vector);
                    Tile next = farthest.get("next");
                    if (next != null && next.getValue() == tile.getValue()
                        && next.getMergeFrom() == null) {
                        Tile mergedTile = new Tile(next.getX(), next.getY(), tile.getValue() * 2);
                        mergedTile.setMergeFrom(new Tile[] {tile, next});

                        this.insertTile(mergedTile);
                        this.removeTile(tile);
                        score += mergedTile.getValue();

                    } else {
                        this.moveTile(tile, farthest.get("farthest"));
                    }

                    if (!this.isTheSamePosition(tile, x, y)) {
                        playerTurn = false;
                        moved = true;
                    }
                }
            }
        }
        return new MoveResult(moved, score, won);
    }

    public void computerMove() {
        addRandomTile();
        this.playerTurn = true;
    }

    private void addRandomTile() {
        List<Tile> emptyCells = getAllEmptyCells();
        if (emptyCells.size() > 0) {
            int value = Math.random() < 0.9 ? 2 : 4;
            Tile tile = randomCells(emptyCells);
            tile.setValue(value);
        }
    }

    private Tile randomCells(List<Tile> emptyCells) {
        if (emptyCells.size() > 0) {
            int randomIndex = (int) Math.floor(Math.random() * emptyCells.size());
            return emptyCells.get(randomIndex);
        }
        return null;
    }

    public void addStartTile() {
        for (int i = 0; i < startTileNumber; i++) {
            addRandomTile();
        }
    }

    private boolean hasEmptyCell() {

        return getAllEmptyCells().size() > 0;
    }

    private List<Tile> getAllEmptyCells() {
        List<Tile> emptyTiles = new ArrayList<Tile>();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                Tile tile = indexs(x, y);
                if (tile == null || (tile != null && tile.getValue() == 0)) {
                    emptyTiles.add(tile);
                }
            }
        }

        return emptyTiles;
    }

    private boolean isTheSamePosition(Tile tile, int x, int y) {
        Tile other = indexs(x, y);
        return tile == other;
    }

    private void moveTile(Tile from, Tile to) {
        this.cells[from.getX()][from.getY()] = new Tile(from.getX(), from.getY(), 0);
        this.cells[to.getX()][to.getY()] = from;
        from.updatePosition(to);
    }

    private void removeTile(Tile tile) {
        this.cells[tile.getX()][tile.getY()] = new Tile(tile.getX(), tile.getY(), 0);
    }

    private void insertTile(Tile tile) {
        this.cells[tile.getX()][tile.getY()] = tile;
    }

    private void prepareTiles() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                Tile tile = indexs(x, y);
                if (tile != null) {
                    tile.setMergeFrom(null);
                    tile.savePosition();
                }
            }
        }
    }

    private int[] getTraversals(boolean reverse) {
        int[] traversals = new int[4];
        if (reverse) {
            traversals[0] = 3;
            traversals[1] = 2;
            traversals[2] = 1;
            traversals[3] = 0;
        } else {
            traversals[0] = 0;
            traversals[1] = 1;
            traversals[2] = 2;
            traversals[3] = 3;
        }
        return traversals;
    }

    private boolean isWithin(Tile cell) {
        int x = cell.getX();
        int y = cell.getY();
        return isWithin(x, y);
    }

    private boolean isWithin(int x, int y) {
        return x >= 0 && x < 4 && y >= 0 && y < 4;
    }

    private boolean isEmpty(Tile tile) {
        return !cellOccupied(tile);
    }

    private boolean isEmpty(int x, int y) {
        Tile tile = indexs(x, y);
        return isEmpty(tile);
    }

    private boolean cellOccupied(Tile tile) {
        return tile != null && tile.getValue() > 0;
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 3; y >= 0; y--) {
            for (int x = 0; x < 4; x++) {
                Tile tile = cells[x][y];
                sb.append("[");
                sb.append(tile == null ? "0" : tile.getValue());
                sb.append("]").append("\t");
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }
}
