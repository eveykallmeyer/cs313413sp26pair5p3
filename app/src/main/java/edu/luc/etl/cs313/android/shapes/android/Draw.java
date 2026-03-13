package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    // TODO entirely your job (except onCircle)

    private final Canvas canvas;

    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas;
        this.paint = paint;
        this.paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        final int oldColor = paint.getColor();
        paint.setColor(c.getColor());
        c.getShape().accept(this);
        paint.setColor(oldColor);
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        final Style oldStyle = paint.getStyle();
        paint.setStyle(Style.FILL_AND_STROKE);
        f.getShape().accept(this);
        paint.setStyle(oldStyle);
        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        for (final Shape s : g.getShapes()) {
            s.accept(this);
        }
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        final int x = l.getX();
        final int y = l.getY();
        canvas.translate(x, y);
        l.getShape().accept(this);
        canvas.translate(-x, -y);
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onOutline(Outline o) {
        final Style oldStyle = paint.getStyle();
        paint.setStyle(Style.STROKE);
        o.getShape().accept(this);
        paint.setStyle(oldStyle);
        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {
        final int n = s.getPoints().size();
        final float[] pts = new float[n * 4];
        for (int i = 0; i < n; i++) {
            final Point p1 = s.getPoints().get(i);
            final Point p2 = s.getPoints().get((i + 1) % n);
            final int base = i * 4;
            pts[base]     = p1.getX();
            pts[base + 1] = p1.getY();
            pts[base + 2] = p2.getX();
            pts[base + 3] = p2.getY();
        }
        canvas.drawLines(pts, paint);
        return null;
    }
}
