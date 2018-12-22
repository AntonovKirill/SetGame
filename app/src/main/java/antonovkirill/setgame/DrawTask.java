package antonovkirill.setgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Build;
import antonovkirill.setgame.CardsView.ForDraw;
import antonovkirill.setgame.MainActivity.Card;

import android.support.annotation.RequiresApi;

public class DrawTask extends AsyncTask<ForDraw, Void, Void> {

    public Card[][] cards;
    public int[][] status;
    public float[][] xs;
    public float[][] ys;
    public float border;
    public float width;
    public float height;
    public float xSize;
    public float ySize;
    public Canvas canvas;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void drawCard(int i, int j) {
        Card card = cards[i][j];
        float x = xs[i][j];
        float y = ys[i][j];
        int count = card.count;
        int color = card.color;
        int shape = card.shape;
        int fill = card.fill;

        Paint paint = new Paint();

        paint.setStrokeWidth(10);

        if (color == 1) {
            paint.setColor(Color.BLUE);
        }
        else if (color == 2) {
            paint.setColor(Color.GREEN);
        }
        else {
            paint.setColor(Color.RED);
        }

        if (fill == 1) {
            paint.setStyle(Paint.Style.FILL);
        }
        else {
            paint.setStyle(Paint.Style.STROKE);
        }

        float xRectSize = xSize / 2f;
        float yRectSize = ySize / 5f;

        if (shape == 1) { // oval
            for (int k = 0; k < count; ++k) {
                float xc = x + xSize / 2;
                float yc = y + (k + 1) * ySize / (count + 1);
                canvas.drawOval(
                        (xc - xRectSize / 2) * width,
                        (yc - yRectSize / 2) * height,
                        (xc + xRectSize / 2) * width,
                        (yc + yRectSize / 2) * height,
                        paint
                );

                if (fill == 2) {
                    canvas.drawLine(
                            xc * width,
                            (yc - yRectSize / 2) * height,
                            xc * width,
                            (yc + yRectSize / 2) * height,
                            paint
                    );
                    float shift = xRectSize / 4;
                    canvas.drawLine(
                            (xc - shift) * width,
                            (yc - yRectSize / 2 * (float) Math.sqrt(1f - shift * shift / xRectSize / xRectSize * 4)) * height,
                            (xc - shift) * width,
                            (yc + yRectSize / 2 * (float) Math.sqrt(1f - shift * shift / xRectSize / xRectSize * 4)) * height,
                            paint
                    );
                    shift = -xRectSize / 4;
                    canvas.drawLine(
                            (xc - shift) * width,
                            (yc - yRectSize / 2 * (float) Math.sqrt(1f - shift * shift / xRectSize / xRectSize * 4)) * height,
                            (xc - shift) * width,
                            (yc + yRectSize / 2 * (float) Math.sqrt(1f - shift * shift / xRectSize / xRectSize * 4)) * height,
                            paint
                    );
                }
            }
        }
        else if (shape == 2) { // diamond
            for (int k = 0; k < count; ++k) {
                float xc = x + xSize / 2;
                float yc = y + (k + 1) * ySize / (count + 1);
                float[] pts = {
                        (xc - xRectSize / 2) * width, yc * height,
                        xc * width, (yc - yRectSize / 2) * height,
                        xc * width, (yc - yRectSize / 2) * height,
                        (xc + xRectSize / 2) * width, yc * height,
                        (xc + xRectSize / 2) * width, yc * height,
                        xc * width, (yc + yRectSize / 2) * height,
                        xc * width, (yc + yRectSize / 2) * height,
                        (xc - xRectSize / 2) * width, yc * height
                };
                canvas.drawLines(pts, paint);

                if (fill == 2) {
                    canvas.drawLine(
                            xc * width,
                            (yc - yRectSize / 2) * height,
                            xc * width,
                            (yc + yRectSize / 2) * height,
                            paint
                    );
                    canvas.drawLine(
                            (xc - xRectSize / 4) * width,
                            (yc - yRectSize / 4) * height,
                            (xc - xRectSize / 4) * width,
                            (yc + yRectSize / 4) * height,
                            paint
                    );
                    canvas.drawLine(
                            (xc + xRectSize / 4) * width,
                            (yc - yRectSize / 4) * height,
                            (xc + xRectSize / 4) * width,
                            (yc + yRectSize / 4) * height,
                            paint
                    );
                }
            }
        }
        else { // squiggles
            for (int k = 0; k < count; ++k) {
                float xc = x + xSize / 2;
                float yc = y + (k + 1) * ySize / (count + 1);

                Path p = new Path();
                p.reset();
                p.moveTo(
                        (xc - xRectSize / 2) * width,
                        (yc + yRectSize / 4) * height
                );
                p.cubicTo(
                        (xc - xRectSize / 2) * width,
                        (yc - yRectSize) * height,
                        (xc + xRectSize / 2)* width,
                        (yc + yRectSize / 2) * height,
                        (xc + xRectSize / 2) * width,
                        (yc - yRectSize / 4) * height
                );

                canvas.drawPath(p, paint);

                p.cubicTo(
                        (xc + xRectSize / 2) * width,
                        (yc + yRectSize) * height,
                        (xc - xRectSize / 2)* width,
                        (yc - yRectSize / 2) * height,
                        (xc - xRectSize / 2) * width,
                        (yc + yRectSize / 4) * height
                );

                canvas.drawPath(p, paint);

                if (fill == 3) {
                    canvas.drawLine(
                            xc * width,
                            (yc - yRectSize * 0.2f) * height,
                            xc * width,
                            (yc + yRectSize * 0.2f) * height,
                            paint
                    );
                    canvas.drawLine(
                            (xc - xRectSize / 4)* width,
                            (yc - yRectSize * 0.3f) * height,
                            (xc - xRectSize / 4)* width,
                            (yc + yRectSize * 0.05f) * height,
                            paint
                    );
                    canvas.drawLine(
                            (xc + xRectSize / 4)* width,
                            (yc + yRectSize * 0.3f) * height,
                            (xc + xRectSize / 4)* width,
                            (yc - yRectSize * 0.05f) * height,
                            paint
                    );

                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected Void doInBackground(ForDraw... forDraws) {
        ForDraw fd = forDraws[0];

        cards = fd.cards;
        status = fd.status;
        xs = fd.xs;
        ys = fd.ys;
        border = fd.border;
        width = fd.width;
        height = fd.height;
        xSize = fd.xSize;
        ySize = fd.ySize;
        canvas = fd.canvas;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 4; ++j) {
                drawCard(i, j);
            }
        }

        return null;
    }
}
