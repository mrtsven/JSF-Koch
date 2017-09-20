package calculate;

import jsf31kochfractalfx.JSF31KochFractalFX;

import timeutil.TimeStamp;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class KochManager implements Observer {
    KochFractal koch = new KochFractal();
    private JSF31KochFractalFX application;
    private ArrayList<Edge> edges = new ArrayList<>();
    public KochManager(JSF31KochFractalFX application) {
        this.application = application;
        koch.addObserver(this);
    }
    public void changeLevel(int nxt) {
        koch.setLevel(nxt);
        drawEdges();
    }
    public void drawEdges() {
        application.clearKochPanel();

        TimeStamp stamp = new TimeStamp();

        stamp.setBegin("Start calculation");
        koch.generateLeftEdge();
        koch.generateBottomEdge();
        koch.generateRightEdge();
        for(Edge edge : edges)
            application.drawEdge(edge);
        stamp.setEnd("calculation complete");
        application.setTextCalc(stamp.toString());
        application.setTextNrEdges(""+edges.size());

    }
    @Override
    public void update(Observable o, Object arg) {
        edges.add((Edge)arg);
    }
}
