package game;

import javafx.application.Application;
import model.Direction;
import model.LabyrinthModel;
import model.Position;
import puzzle.solver.BreadthFirstSearch;

public class Main {
    public static void main(String[] args) {
        Application.launch(LabyrinthApp.class, args);
    }
}
