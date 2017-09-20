package calculate;

import jsf31kochfractalfx.JSF31KochFractalFX;

import java.util.Observable;
import java.util.Observer;

public class KochManager implements Observer {
    KochFractal koch = new KochFractal();
    private JSF31KochFractalFX application;

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
        koch.generateLeftEdge();
        koch.generateBottomEdge();
        koch.generateRightEdge();
    }
    @Override
    public void update(Observable o, Object arg) {
        application.drawEdge((Edge)arg);
    }
}
