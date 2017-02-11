import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RandomSeq;
import edu.princeton.cs.algs4.Stack;

public class Board {
    private int[][] blocks;
    private int blockSize;
    private List<Block> blocksOutOfPlace;
    private Stack<Board> neighboringBoardsStack;
    private Block zeroBlock;
    
    public Board(int[][] blocksP) {
        this.blocks = Arrays.copyOf(blocksP, blocksP.length);
        this.blockSize = blocks.length;
        this.blocksOutOfPlace = getBlocksOutOfPlace(blocks);
        // blocksOutOfPlace method has to run first, because it determines the position of 0
        // it also implies whether the board is the final board
        // this.neighboringBoardsStack = fillNeighboringBoardsStack(blocks);
        
    }
    
    public static void main(String[] args) {
        int demoLength = 3;
        int[][] blocksP = new int[demoLength][demoLength];
        int value = 1;
        for (int i = 0; i < blocksP.length; i++) {
            for (int j = 0; j < blocksP.length; j++) {
                blocksP[i][j] = value;
                value++;
            }
        }
        blocksP[demoLength-1][demoLength-1] = 1;
        blocksP[0][0] = 0;
        
        
        Board board = new Board(blocksP);
        board.neighbors();
        
        Board twin =  board.twin();
        Board twin2 =  board.twin();
        Board twin3 =  board.twin();
        
        System.out.println(board.toString());
        assert(board.hamming() == 1);
        // 1 is at 2,2 to move it into 0,0 you need 4 moves
        assert(board.manhattan() == 4);
        // 
        blocksP[0][0] = 4;
        blocksP[1][0] = 0;
        board = new Board(blocksP);
        assert(board.manhattan() == 5);
        board.neighbors();
        System.out.println(board.toString());
        System.out.println("a kurva anyad");

    }
    
    
    private Stack<Board> fillNeighboringBoardsStack(int[][] blocks) {
        // get possible indices around the zero block
        Stack<Board> neighboringBoardsStack = new Stack<>();
        // swap the values of the zeroBlock and newZeroBlock and instantiate the new board
        Block newZeroBlock;
        if (zeroBlock.x - 1 >= 0) {
            // make it into a private method
            newZeroBlock = new Block(zeroBlock.x - 1, zeroBlock.y);
            Board neighboringBoard = createNeighboringBoard(newZeroBlock, blocks);
            neighboringBoardsStack.push(neighboringBoard);
        }
        if (zeroBlock.y - 1 >= 0) {
            newZeroBlock = new Block(zeroBlock.x, zeroBlock.y - 1);
            Board neighboringBoard = createNeighboringBoard(newZeroBlock, blocks);
            neighboringBoardsStack.push(neighboringBoard);
        }
        if (zeroBlock.x + 1 <= blockSize - 1) {
            newZeroBlock = new Block(zeroBlock.x + 1, zeroBlock.y);
            Board neighboringBoard = createNeighboringBoard(newZeroBlock, blocks);
            neighboringBoardsStack.push(neighboringBoard);
        }
        if (zeroBlock.y + 1 <= blockSize - 1) {
            newZeroBlock = new Block(zeroBlock.x, zeroBlock.y + 1);
            Board neighboringBoard = createNeighboringBoard(newZeroBlock, blocks);
            neighboringBoardsStack.push(neighboringBoard);
        }
        return neighboringBoardsStack;
    }
    
    private Board createNeighboringBoard(Block newZeroBlock, int[][] oldBlocks) {
        int[][] newBlocks = copyTwoDimensionalArray(oldBlocks);
        int tmpValue = newBlocks[newZeroBlock.x][newZeroBlock.y];
        newBlocks[newZeroBlock.x][newZeroBlock.y] = 0;
        newBlocks[zeroBlock.x][zeroBlock.y] = tmpValue;
        return new Board(newBlocks);
    }
    
    private int[][] copyTwoDimensionalArray(int[][] oldArray) {
        int[][] newArray = new int[oldArray.length][oldArray.length];
        for (int i = 0; i < oldArray.length; i++) {
            newArray[i] = Arrays.copyOf(oldArray[i], oldArray[i].length);
        }
        return newArray;
    }
    
    public Iterable<Board> neighbors() {
        this.neighboringBoardsStack = fillNeighboringBoardsStack(this.blocks);
        return neighboringBoardsStack;
    }
    
    public Board twin() {
        // random x and y check if not null if null generate new one
        // swap two blocks - any to block they cant be null or equal to each other
        Random random = new Random();
        
        int randomRowIndex1 = random.nextInt(blockSize);
        int randomColumnIndex1 = random.nextInt(blockSize);
        while (this.blocks[randomRowIndex1][randomColumnIndex1] == 0) {
            randomRowIndex1 = random.nextInt(blockSize);
            randomColumnIndex1 = random.nextInt(blockSize);
        }
        
        int randomRowIndex2 = random.nextInt(blockSize);
        int randomColumnIndex2 = random.nextInt(blockSize);
        while (this.blocks[randomRowIndex2][randomColumnIndex2] == 0 ||
               this.blocks[randomRowIndex2][randomColumnIndex2] == this.blocks[randomRowIndex1][randomColumnIndex1]) {
            randomRowIndex2 = random.nextInt(blockSize);
            randomColumnIndex2 = random.nextInt(blockSize);
        }
        
        int[][] newBlocks = copyTwoDimensionalArray(this.blocks);
        
        int tmp = newBlocks[randomRowIndex1][randomColumnIndex1];
        newBlocks[randomRowIndex1][randomColumnIndex1] = newBlocks[randomRowIndex2][randomColumnIndex2];
        newBlocks[randomRowIndex2][randomColumnIndex2] = tmp;
        return new Board(newBlocks);
    }
    
    /**
     * number of blocks out of place
     * @return
     */
    public int hamming() {
        int numberOfBlocksOutOfPlace = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                int blockValue = blocks[i][j];
                if (blockValue == 0){
//                    if (i != blocks.length -1 || j != blocks.length -1) {
//                        numberOfBlocksOutOfPlace++;
//                    }
                    continue;
                }
                int normalizedBlockValue = blockValue - 1;
                int rowIndex = normalizedBlockValue / blockSize;
                int columnIndex = normalizedBlockValue % blockSize;
                if (rowIndex != i || columnIndex != j) {
                    numberOfBlocksOutOfPlace++;
                }
            }
        }
        return numberOfBlocksOutOfPlace;
    }
    
    /**
     * The sum of the Manhattan distances (sum of the vertical and horizontal distance)
     * from the blocks to their goal positions
     * @return
     */
    public int manhattan() {
        int manhattanDistance = 0;
        for (Block outOfPlaceBlock : blocksOutOfPlace) {
            int blockValue = blocks[outOfPlaceBlock.x][outOfPlaceBlock.y];
            int normalizedBlockValue = blockValue - 1;
            int properRowIndex = normalizedBlockValue / blockSize;
            int properColumnIndex = normalizedBlockValue % blockSize;
            int horizontalDiff = Math.abs(properRowIndex - outOfPlaceBlock.x);
            int verticalDiff = Math.abs(properColumnIndex - outOfPlaceBlock.y);
            manhattanDistance += (verticalDiff + horizontalDiff);
        }
        return manhattanDistance;
    }
    
    private List<Block> getBlocksOutOfPlace(int[][] blocks) {
        List<Block> blocksOutOfPlace = new ArrayList<>();
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                int blockValue = blocks[i][j];
                if (blockValue == 0) {
//                    if (i != blocks.length -1 || j != blocks.length -1) {
//                        Block blockOutOfPlace = new Block(i, j);
//                        blocksOutOfPlace.add(blockOutOfPlace);
//                    }
                    zeroBlock = new Block(i, j);
                    continue;
                }
                else {
                    int normalizedBlockValue = blockValue - 1;
                    int rowIndex = normalizedBlockValue / blockSize;
                    int columnIndex = normalizedBlockValue % blockSize;
                    if (rowIndex != i || columnIndex != j) {
                        Block blockOutOfPlace = new Block(i, j);
                        blocksOutOfPlace.add(blockOutOfPlace);
                    }
                }
            }
        }
        return blocksOutOfPlace;
    }
    
    public int dimension() {
        return this.blockSize;
    }
    
    public boolean isGoal() {
        return blocksOutOfPlace.isEmpty();
    }
    

    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(blocks);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Board other = (Board) obj;
        if (!Arrays.deepEquals(blocks, other.blocks))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(blockSize + "\n");
        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private class Block
    {
        int x;
        int y;
        
        public Block(int xp, int yp) {
            if (xp < 0 || yp < 0) {
                throw new IllegalArgumentException();
            }
            this.x = xp;
            this.y = yp;
        }
    }
    /*
     * constructorbol megallapitani, h mely blockok vannak outofplace, 
     * pl getOutOfPlaceBlocks private method, private nested class block, x,y coordinate-s
     */
}
