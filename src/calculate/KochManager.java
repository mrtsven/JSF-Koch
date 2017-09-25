package calculate;

import jsf31kochfractalfx.JSF31KochFractalFX;

import timeutil.TimeStamp;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class KochManager {
    private JSF31KochFractalFX application;
    private ArrayList<Edge> edges = new ArrayList<>();
    TimeStamp stamp;
    private int count = 0;
    public KochManager(JSF31KochFractalFX application) {
        this.application = application;
    }

    public void changeLevel(int nxt) {
        edges.clear();
        stamp = new TimeStamp();

        count = 0;
        stamp.setBegin("Start calculation");

        EdgeGenerator leftEdgeGenerator = new EdgeGenerator(nxt, EdgeType.Left, this);
        new Thread(leftEdgeGenerator).start();

        EdgeGenerator rightEdgeGenerator = new EdgeGenerator(nxt, EdgeType.Right, this);
        new Thread(rightEdgeGenerator).start();

        EdgeGenerator bottomEdgeGenerator = new EdgeGenerator(nxt, EdgeType.Bottom, this);
        new Thread(bottomEdgeGenerator).start();

    }

    public void drawEdges() {
        TimeStamp stamp = new TimeStamp();
        stamp.setBegin("Start draw");
        application.clearKochPanel();
        for(Edge edge : edges)
            application.drawEdge(edge);

        stamp.setEnd("drawing complete");
        application.setTextDraw(stamp.toString());


        this.stamp.setEnd("calculation complete");

        application.setTextCalc(this.stamp.toString());

        application.setTextNrEdges(""+edges.size());
    }

    public int getCount() {
        return count;
    }

    public synchronized void addEdge(Edge edge) {
        edges.add(edge);
    }

    public synchronized void edgeDone() {
        this.count++;

        if (count == 3) {

            application.requestDrawEdges();
        }
    }
}
