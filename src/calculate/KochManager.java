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
        TimeStamp stamp = new TimeStamp();

        stamp.setBegin("Start calculation");
        koch.generateLeftEdge();
        koch.generateBottomEdge();
        koch.generateRightEdge();

        stamp.setEnd("calculation complete");
        application.setTextCalc(stamp.toString());

        drawEdges();

        application.setTextNrEdges(""+edges.size());

    }

    public void drawEdges() {
        TimeStamp stamp = new TimeStamp();
        stamp.setBegin("Start draw");
        application.clearKochPanel();
        for(Edge edge : edges)
            application.drawEdge(edge);

        stamp.setEnd("drawing complete");
        application.setTextDraw(stamp.toString());
    }

    @Override
    public void update(Observable o, Object arg) {
        edges.add((Edge)arg);
    }
}
