package calculate;

import java.util.Observable;
import java.util.Observer;

public class EdgeGenerator implements Runnable, Observer {

    private KochFractal fractal;
    private EdgeType type;
    private KochManager manager;

    public EdgeGenerator(int level, EdgeType type, KochManager manager) {
        this.fractal = new KochFractal(level);
        this.fractal.addObserver(this);
        this.type = type;
        this.manager = manager;
    }

    @Override
    public void run() {

        switch (type) {
            case Left:
                fractal.generateLeftEdge();
                break;
            case Right:
                fractal.generateRightEdge();
                break;
            case Bottom:
                fractal.generateBottomEdge();
        }
        manager.edgeDone();
    }

    @Override
    public void update(Observable o, Object arg) {
        manager.addEdge((Edge)arg);

    }


}
