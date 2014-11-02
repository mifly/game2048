package com.minoq.game2048.test;

import com.minoq.game2048.Grid;
import com.minoq.game2048.Tile;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by yichengfeng on 14-11-2.
 */
public class TestGrid {

    @Test
    public void testInitAndIndex()
    {
        Grid grid = new Grid();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                Tile tile = grid.indexs(x,y);
                Assert.assertTrue(tile.getValue() == 0);
            }
        }
    }

    @Test
    public void testGrid1() {
        int[][] numbers = new int[4][4];

        numbers[3] = new int[]{32,0,0,0};
        numbers[2] = new int[]{2,4,2,0};
        numbers[1] = new int[]{2,4,0,0};
        numbers[0] = new int[]{2,0,0,0};

        Grid grid = new Grid(numbers);
        System.out.println(grid.toString());
        grid.move(Grid.LEFT_DIRECTION);
        System.out.println(grid.toString());
        grid.computerMove();
        System.out.println(grid.toString());

    }
}
