package calculate;

import javafx.application.Platform;
import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class KochManager {
    private JSF31KochFractalFX application;
    private ArrayList<Edge> edges = new ArrayList<>();
    private TimeStamp stamp;
    private EdgeGenerator leftEdgeGenerator;
    private EdgeGenerator rightEdgeGenerator;
    private EdgeGenerator bottomEdgeGenerator;

    public KochManager(JSF31KochFractalFX application) {
        this.application = application;
    }

    public void changeLevel(int nxt) {
        if (leftEdgeGenerator != null && leftEdgeGenerator.isRunning())
            leftEdgeGenerator.cancel();

        if (rightEdgeGenerator != null && rightEdgeGenerator.isRunning())
            rightEdgeGenerator.cancel();

        if (bottomEdgeGenerator != null && bottomEdgeGenerator.isRunning())
            bottomEdgeGenerator.cancel();

        stamp = new TimeStamp();
        stamp.setBegin("Start calculation");

        leftEdgeGenerator = new EdgeGenerator(nxt, EdgeType.Left);
        rightEdgeGenerator = new EdgeGenerator(nxt, EdgeType.Right);
        bottomEdgeGenerator = new EdgeGenerator(nxt, EdgeType.Bottom);

        application.addLeftEdgeTask(leftEdgeGenerator);
        application.addRightEdgeTask(rightEdgeGenerator);
        application.addBottomEdgeTask(bottomEdgeGenerator);

        leftEdgeGenerator.progressProperty().addListener((e) -> ProgressUpdate(leftEdgeGenerator));
        rightEdgeGenerator.progressProperty().addListener((e) -> ProgressUpdate(rightEdgeGenerator));
        bottomEdgeGenerator.progressProperty().addListener((e) -> ProgressUpdate(bottomEdgeGenerator));

        new Thread(leftEdgeGenerator).start();
        new Thread(rightEdgeGenerator).start();
        new Thread(bottomEdgeGenerator).start();

        new Thread(() -> {
            edges.clear();
            try {
                edges.addAll(leftEdgeGenerator.get());
                edges.addAll(rightEdgeGenerator.get());
                edges.addAll(bottomEdgeGenerator.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            Platform.runLater(() -> {
                this.stamp.setEnd("calculation complete");
                application.setTextCalc(this.stamp.toString());
                application.setTextNrEdges("" + edges.size());

                drawEdges();
            });
        }).start();

    }

    private void ProgressUpdate(EdgeGenerator generator) {
        ArrayList<Edge> value = generator.getValue();
        for (int i = 0, valueSize = value.size(); i < valueSize; i++) {
            Edge edge = value.get(i);
            application.drawEdgeWhite(edge);
        }
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
