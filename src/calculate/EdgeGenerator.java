package calculate;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.*;

public class EdgeGenerator extends Task<ArrayList<Edge>> implements Observer {

    private KochFractal fractal;
    private EdgeType type;
    private ArrayList<Edge> edges;

    public EdgeGenerator(int level, EdgeType type) {
        fractal = new KochFractal(level);
        fractal.addObserver(this);
        edges = new ArrayList<>(fractal.getNrOfEdges() / 3);
        this.type = type;
    }

    @Override
    public void update(Observable o, Object arg) {

        edges.add((Edge)arg);

        updateValue(edges);
        updateProgress(edges.size(), fractal.getNrOfEdges() / 3);
    }

    @Override
    protected ArrayList<Edge> call() {
        switch (type) {
            case Left:
                fractal.generateLeftEdge();
                break;
            case Right:
                fractal.generateRightEdge();
                break;
            case Bottom:
                fractal.generateBottomEdge();
                break;
        }
        return edges;
    }
}
