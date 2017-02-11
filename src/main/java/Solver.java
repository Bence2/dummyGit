import java.util.Comparator;
import java.util.List;

import edu.princeton.cs.algs4.MinPQ;

public class Solver {

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
        Solver solver = new Solver(new Board(blocksP));

    }
    
    public Solver(Board initialBoardP) {
        MinPQ<BoardWrapper> boardPQ = new MinPQ<>(new BoardWrapperComparatorHamming());
        Board currentBoard = initialBoardP;
        while (true) {
            for (Board board : currentBoard.neighbors()) {
                BoardWrapper boardWrapper = new BoardWrapper(board, initialBoardP);
                boardPQ.insert(boardWrapper);
            }
            currentBoard = boardPQ.delMin().getBoard();
            if (currenB) {
                
            }
        }
        
    }
    
    private class BoardWrapper {
        Board board;
        Board parentBoard;
        
        public Board getBoard() {
            return board;
        }
        public BoardWrapper(Board boardP, Board parentBoardP) {
            this.board = boardP;
            this.parentBoard = parentBoardP;
        }
        
        public int hamming() {
            return board.hamming();
        }
        public int manhattan() {
            return board.manhattan();
        }

    }
    
    private class BoardWrapperComparatorHamming implements Comparator<BoardWrapper> {

        @Override
        public int compare(BoardWrapper bw1, BoardWrapper bw2) {
            return bw1.hamming() - bw2.hamming();
        }
    }
    
    private class BoardWrapperComparatorManhattan implements Comparator<BoardWrapper> {
        
        @Override
        public int compare(BoardWrapper bw1, BoardWrapper bw2) {
            return bw1.manhattan() - bw2.manhattan();
        }
    }
}
