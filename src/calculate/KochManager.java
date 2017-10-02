package calculate;

import jsf31kochfractalfx.JSF31KochFractalFX;

import timeutil.TimeStamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class KochManager {
    private JSF31KochFractalFX application;
    private ArrayList<Edge> edges = new ArrayList<>();
    private TimeStamp stamp;

    public KochManager(JSF31KochFractalFX application) {
        this.application = application;
    }

    public void changeLevel(int nxt) {
        edges.clear();
        stamp = new TimeStamp();

        stamp.setBegin("Start calculation");

        EdgeGenerator leftEdgeGenerator = new EdgeGenerator(nxt, EdgeType.Left);

        EdgeGenerator rightEdgeGenerator = new EdgeGenerator(nxt, EdgeType.Right);

        EdgeGenerator bottomEdgeGenerator = new EdgeGenerator(nxt, EdgeType.Bottom);

        ExecutorService pool = Executors.newFixedThreadPool(3);
        Future<List<Edge>> leftFuture =  pool.submit(leftEdgeGenerator);
        Future<List<Edge>> rightFuture =  pool.submit(rightEdgeGenerator);
        Future<List<Edge>> bottomFuture = pool.submit(bottomEdgeGenerator);

        try {
            edges.addAll(leftFuture.get());
            edges.addAll(rightFuture.get());
            edges.addAll(bottomFuture.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        this.stamp.setEnd("calculation complete");
        application.setTextCalc(this.stamp.toString());
        application.setTextNrEdges("" + edges.size());

        drawEdges();
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

}
