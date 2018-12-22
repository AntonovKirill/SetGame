package antonovkirill.setgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import antonovkirill.setgame.MainActivity.Card;

public class CardsView extends View {

    public class ForDraw {
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
    }

    public Card[][] cards = new Card[3][4];
    public int[][] status = new int[3][4];
    public float[][] xs = new float[3][4];
    public float[][] ys = new float[3][4];
    public float border;
    public float width;
    public float height;
    public float xSize;
    public float ySize;

    public CardsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        border = 0.1f;
        xSize = (1 - border) / 4;
        ySize = (1 - border) / 3;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 4; ++j) {
                cards[i][j] = new Card();
                status[i][j] = 0;

                xs[i][j] = j / 4f + border / 8f;
                ys[i][j] = i / 3f + border / 6f;
            }
        }

        setFocusable(true);
        setClickable(true);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        if (width == 0)
            width = getWidth();
        if (height == 0)
            height = getHeight();

        canvas.drawColor(Color.rgb(192, 192, 192));

        Paint paint = new Paint();

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (status[i][j] == 1) {
                    paint.setColor(Color.rgb(255, 250, 160));
                }
                else if (status[i][j] == 0) {
                    paint.setColor(Color.WHITE);
                }
                else {
                    paint.setColor(Color.rgb(255, 161, 114));
                }
                float x = xs[i][j];
                float y = ys[i][j];
                canvas.drawRect(x * width, y * height, (x + xSize) * width, (y + ySize) * height, paint);
            }
        }

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.rgb(64, 64, 64));
        paint.setStrokeWidth(3);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 4; ++j) {
                float x = xs[i][j];
                float y = ys[i][j];
                canvas.drawRect(x * width, y * height, (x + xSize) * width, (y + ySize) * height, paint);
            }
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 4; ++j) {
                drawCard(i, j, canvas);
            }
        }

//        ForDraw fd = new ForDraw();
//
//        fd.cards = cards;
//        fd.status = status;
//        fd.xs = xs;
//        fd.ys = ys;
//        fd.border = border;
//        fd.width = width;
//        fd.height = height;
//        fd.xSize = xSize;
//        fd.ySize = ySize;
//        fd.canvas = canvas;
//
//        DrawTask drawTask = new DrawTask();
//        drawTask.execute(fd);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void drawCard(int i, int j, Canvas canvas) {
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
//                canvas.drawLines(pts, paint);
                Path p = new Path();
                p.reset();
                p.moveTo(xc * width, (yc - yRectSize / 2) * height);
                p.lineTo((xc + xRectSize / 2) * width, yc * height);
                p.lineTo(xc * width, (yc + yRectSize / 2) * height);
                p.lineTo((xc - xRectSize / 2) * width, yc * height);
                p.lineTo(xc * width, (yc - yRectSize / 2) * height);

                canvas.drawPath(p, paint);

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

                if (fill == 2) {
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


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return true;
        }

        float x = event.getX() / width;
        float y = event.getY() / height;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (x >= xs[i][j] && x <= xs[i][j] + xSize && y >= ys[i][j] && y <= ys[i][j] + ySize) {
                    if (status[i][j] == 1) {
                        status[i][j] = 0;
                    }
                    else {
                        status[i][j] = 1;
                    }

                    Log.d("card", "count=" + cards[i][j].count +
                            " color=" + cards[i][j].color +
                            " shape=" + cards[i][j].shape +
                            " fill=" + cards[i][j].fill
                    );
                }
            }
        }

        this.performClick();
        invalidate();

        return true;
    }

}
