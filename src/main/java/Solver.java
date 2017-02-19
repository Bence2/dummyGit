import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
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
//        int tmpValue = blocksP[demoLength-1][demoLength-1];
//        blocksP[demoLength-1][demoLength-1] = blocksP[demoLength-1][demoLength-1]
        Solver solver = new Solver(new Board(blocksP));

    }
    
    public Solver(Board initialBoardP) {
        getSolution(initialBoardP);
        
    }
    
    private void getSolution(Board initialBoardP) {
        MinPQ<BoardWrapper> boardOriginalPQ = new MinPQ<>(new BoardWrapperComparatorHamming());
        BoardWrapper initialOriginalBoardWrapper = new BoardWrapper(initialBoardP, null);
        initialOriginalBoardWrapper.setNumberOfMoves(0);
        
        MinPQ<BoardWrapper> boardTwinPQ = new MinPQ<>(new BoardWrapperComparatorHamming());
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
        System.out.println("sikeer");
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
