import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    
    private boolean isSolvable;
    private Stack<Board> solutionSteps;
    private int minNumberOfMoves;

//    public static void main(String[] args) {
//        int demoLength = 3;
//        int[][] blocksP = new int[demoLength][demoLength];
//        int value = 1;
//        for (int i = 0; i < blocksP.length; i++) {
//            for (int j = 0; j < blocksP.length; j++) {
//                blocksP[i][j] = value;
//                value++;
//            }
//        }
//        blocksP[demoLength-1][demoLength-1] = 1;
//        blocksP[0][0] = 0;
////        int tmpValue = blocksP[demoLength-1][demoLength-1];
////        blocksP[demoLength-1][demoLength-1] = blocksP[demoLength-1][demoLength-1]
//        Solver solver = new Solver(new Board(blocksP));
//
//    }
    
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
    
    public Solver(Board initialBoardP) {
        BoardWrapper finalBoardWrapper = getSolution(initialBoardP);
        this.solutionSteps = fillSolutionSteps(finalBoardWrapper);
        this.isSolvable = finalBoardWrapper.getBoard().isGoal();
        this.minNumberOfMoves = finalBoardWrapper.getNumberOfMoves();
    }
    
    private Stack<Board> fillSolutionSteps(BoardWrapper finalBoardWrapper) {
        if (finalBoardWrapper.getBoard().isGoal()) {
            solutionSteps = new Stack<>();
            solutionSteps.push(finalBoardWrapper.getBoard());
            
            while (finalBoardWrapper.getParentBoard() != null) {
                solutionSteps.push(finalBoardWrapper.getParentBoard().getBoard());
            }
            return solutionSteps;
        }
        else {
            return null;
        }
    }
    
    public Iterable<Board> solution() {
        return solutionSteps;
    }
    
    public int moves() {
        if (!this.isSolvable) {
            return -1;
        }
        else {
            return this.minNumberOfMoves;
        }
        
    }
    
    public boolean isSolvable() {
        return isSolvable;
    }
    
    private BoardWrapper getSolution(Board initialBoardP) {
        MinPQ<BoardWrapper> boardOriginalPQ = new MinPQ<>(new BoardWrapperComparatorManhattan());
        BoardWrapper initialOriginalBoardWrapper = new BoardWrapper(initialBoardP, null);
        initialOriginalBoardWrapper.setNumberOfMoves(0);
        
        MinPQ<BoardWrapper> boardTwinPQ = new MinPQ<>(new BoardWrapperComparatorManhattan());
        Board initialTwinBoard = initialBoardP.twin();
        BoardWrapper initialTwinBoardWrapper = new BoardWrapper(initialTwinBoard, null);
        initialTwinBoardWrapper.setNumberOfMoves(0);
        
        boolean isOriginalBoardSolved = false;
        boolean isTwinBoardSolved = false;
        while (!isOriginalBoardSolved && !isTwinBoardSolved) {
            
            initialOriginalBoardWrapper =  getSolutionStep(initialOriginalBoardWrapper, boardOriginalPQ);
            isOriginalBoardSolved = initialOriginalBoardWrapper.getBoard().isGoal();
            
            initialTwinBoardWrapper = getSolutionStep(initialTwinBoardWrapper, boardTwinPQ);
            isTwinBoardSolved = initialTwinBoardWrapper.getBoard().isGoal();
        }
        return initialOriginalBoardWrapper;
    }
    
    private BoardWrapper getSolutionStep(BoardWrapper currentBoardWrapper, MinPQ<BoardWrapper> boardPQ) {
        List<Board> validNeighborBoards = getValidNeighbors(currentBoardWrapper);
        for (Board board : validNeighborBoards) {
            BoardWrapper boardWrapper = new BoardWrapper(board, currentBoardWrapper);
            boardWrapper.setNumberOfMoves(currentBoardWrapper.getNumberOfMoves() + 1);
            boardPQ.insert(boardWrapper);
        }
        currentBoardWrapper = boardPQ.delMin();
        return currentBoardWrapper;
    }
    
    private List<Board> getValidNeighbors(BoardWrapper currentBoardWrapper) {
        List<Board> validNeighborBoards = new ArrayList<>();
        if (currentBoardWrapper.getParentBoard() == null) {
            for (Board board : currentBoardWrapper.getBoard().neighbors()) {
                validNeighborBoards.add(board);
            }
        }
        else {
            Board parentBoard = currentBoardWrapper.getParentBoard().getBoard();
            validNeighborBoards = new ArrayList<>();
            for (Board board : currentBoardWrapper.getBoard().neighbors()) {
                if (!parentBoard.equals(board)) {
                    validNeighborBoards.add(board);
                }
            }
        }
        return validNeighborBoards;
        
    }
    
    private class BoardWrapper {
        Board board;
        BoardWrapper parentBoard;
        int numberOfMoves;
        
        public BoardWrapper(Board boardP, BoardWrapper parentBoardP) {
            this.board = boardP;
            this.parentBoard = parentBoardP;
        }
        public int getNumberOfMoves() {
            return numberOfMoves;
        }
        public void setNumberOfMoves(int numberOfMoves) {
            this.numberOfMoves = numberOfMoves;
        }
        public BoardWrapper getParentBoard() {
            return parentBoard;
        }
        public void setParentBoard(BoardWrapper parentBoard) {
            this.parentBoard = parentBoard;
        }
        public Board getBoard() {
            return board;
        }
        
        public int hamming() {
            return board.hamming();
        }
        public int manhattan() {
            return board.manhattan();
        }
        
        @Override
        public String toString() {
            return board.toString();
        }

    }
    
    private class BoardWrapperComparatorHamming implements Comparator<BoardWrapper> {

        @Override
        public int compare(BoardWrapper bw1, BoardWrapper bw2) {
            return (bw1.hamming() + bw1.getNumberOfMoves()) - (bw2.hamming() + bw2.getNumberOfMoves());
        }
    }
    
    private class BoardWrapperComparatorManhattan implements Comparator<BoardWrapper> {
        
        @Override
        public int compare(BoardWrapper bw1, BoardWrapper bw2) {
            return (bw1.manhattan() + bw1.getNumberOfMoves()) - (bw2.manhattan() + bw2.getNumberOfMoves());
        }
    }
}
