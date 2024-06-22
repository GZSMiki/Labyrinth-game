package solver;

import model.Direction;
import model.LabyrinthModel;
import puzzle.solver.BreadthFirstSearch;

public class Solve {
    public static void main(String[] args) {
        BreadthFirstSearch<Direction> bfs = new BreadthFirstSearch<>();
        bfs.solveAndPrintSolution(new LabyrinthModel());
    }
}
