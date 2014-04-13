import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Андрей on 24.03.14.
 */
public class PFunc implements Drawable
{

    /** Image for drawing */
    BufferedImage image = null;

    /** Scale coefficient */
    private int curZoom = 20;

    /** Offset axis Y */
    private int kU = 0;

    /** Offset axis X */
    private int kL = 0;

    /** Center axis X */
    private int x0 = 0;

    /** Center axis Y */
    private int y0 = 0;

    /** For small optimization */
    private int curX = 0;

    /** For small optimization */
    private int curY = 0;

    /** Width of panel */
    private int width = 0;

    /** Height of panel */
    private int height = 0;

    /** For correct drawing */
    private double dstep = 1. / curZoom;

    /** Delta t */
    private double dT = 0;

    /** Default value of curZoom. Need for correct reset */
    private int defCoeff = 20;

    /**
     * All of the following parameters needed to calculate the values ​​of the function
     */
    private double tx1 = 0;
    private double tx2 = 0;
    private double ty1 = 0;
    private double ty2 = 0;
    private double xmin = 0;
    private double ymin = 0;
    private double xmax = 0;
    private double ymax = 0;

    /**
     * Constructor
     * @param x0 center of axis X
     * @param y0 center of axis Y
     * @param width is width of panel
     * @param height is height of panel
     */
    public PFunc(int x0, int y0, int width, int height)
    {
        this.x0 = x0;
        this.y0 = y0;
        this.width = width;
        this.height = height;
    }

    /**
     * Paint the figure.
     * @see Drawable
     */
    @Override
    public BufferedImage paintFigure()
    {
        dstep = 1. / curZoom;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        x0 += kL;
        y0 += kU;
        System.out.println(x0 + " " + y0);
        if(y0 < height && y0 > 0)
        {
            for(int i = 0; i < width; i++)
            {
                image.setRGB(i, y0, Color.red.getRGB());
            }
        }
        if(x0 < width && x0 > 0)
        {
            for(int j = 0; j < height; j++)
            {
                image.setRGB(x0, j, Color.red.getRGB());
            }
        }
        for(int i = x0 + curZoom; i <= width; i += curZoom)
        {
            drawCoordinateStroke(i, y0 - curZoom / 5, i, y0 + curZoom / 5);
        }
        for(int i = x0 - curZoom; i >= 0; i -= curZoom)
        {
            drawCoordinateStroke(i, y0 - curZoom / 5, i, y0 + curZoom / 5);
        }
        for(int i = y0 + curZoom; i <= height; i += curZoom)
        {
            drawCoordinateStroke(x0 - curZoom / 5, i, x0 + curZoom / 5, i);
        }
        for(int i = y0 - curZoom; i >= 0; i -= curZoom)
        {
            drawCoordinateStroke(x0 - curZoom / 5, i, x0 + curZoom / 5, i);
        }
        if(((y0 - height) < (-17 * curZoom / 2)) && ((width - x0 > curZoom) && (-x0 < 4 * curZoom / 3)))
        {
            ymin = -(height - y0) * dstep;
            ty1 = (ymin + Math.sqrt((ymin + 4) * (ymin + 4) - 20)) / 2;
            ty2 = (ymin - Math.sqrt((ymin + 4) * (ymin + 4) - 20)) / 2;
            dT = dstep / Math.max(Math.abs(funcDY(ty1)), Math.abs(funcDY(ty2)));
            curX = fX(ty2);
            curY = fY(ty2);
            for(double i = ty2; i <= ty1; i += dT)
            {
                int x = fX(i);
                int y = fY(i);
                if((curX != x || curY != y) && ((y > y0 - height) && (y < y0) && (x > -x0) && (x < width - x0)))
                {
                    image.setRGB(x0 + x, y0 - y, Color.blue.getRGB());
                    curX = x;
                    curY = y;
                }
            }
        }


        //t = (-2, -1)
        if((width - x0 > (4 * curZoom / 3)) && ((2 * curZoom) < y0))
        {
            xmax = (width - x0) * dstep;
            ymax = y0 * dstep;
            tx2 = -1.* Math.sqrt(xmax / (xmax - 1));
            ty2 = (ymax - Math.sqrt((ymax + 4) * (ymax + 4) - 20)) / 2;
            dT = dstep / Math.max(Math.abs(funcDX(tx2)), Math.abs(funcDY(ty2)));
            curX = fX(ty2);
            curY = fY(ty2);
            for(double i = ty2; i <= tx2; i += dT)
            {
                int x = fX(i);
                int y = fY(i);
                if((curX != x || curY != y) && ((y > y0 - height) && (y < y0) && (x > -x0) && (x < width - x0)))
                {
                    image.setRGB(x0 + x, y0 - y, Color.blue.getRGB());
                    curX = x;
                    curY = y;
                }
            }
        }

        //t = (-1, 1)
        if((-x0 < 0) && (((y0 > curZoom / 2) && (y0 - height) < 2 * curZoom)))
        {
            xmin = -x0 * dstep;
            tx1 = Math.sqrt(xmin / (xmin - 1));
            tx2 = -1 * Math.sqrt(xmin / (xmin - 1));
            dT = dstep / Math.abs(funcDX(tx1));
            curX = fX(tx2);
            curY = fY(tx2);
            for(double i = tx2; i <= tx1; i += dT)
            {
                int x = fX(i);
                int y = fY(i);
                if((curX != x || curY != y) && ((y > y0 - height) && (y < y0) && (x > -x0) && (x < width - x0)))
                {
                    image.setRGB(x0 + x, y0 - y, Color.blue.getRGB());
                    curX = x;
                    curY = y;
                }
            }
        }

        //t = (1, infinity)
        if(((width - x0) > curZoom) && (y0 > (2 * curZoom / 3)))
        {
            xmax = (width - x0) * dstep;
            ymax = y0 * dstep;
            tx1 = Math.sqrt(xmax / (xmax - 1));
            ty1 = (ymax + Math.sqrt((ymax + 4) * (ymax + 4) - 20)) / 2;
            dT = dstep / Math.max(Math.abs(funcDX(tx1)), Math.abs(funcDY(ty1)));
            curX = fX(tx1);
            curY = fY(tx1);
            for(double i = tx1; i <= ty1; i += dT)
            {
                int x = fX(i);
                int y = fY(i);
                if((curX != x || curY != y) && ((y > y0 - height) && (y < y0) && (x > -x0) && (x < width - x0)))
                {
                    image.setRGB(x0 + x, y0 - y, Color.blue.getRGB());
                    curX = x;
                    curY = y;
                }
            }
        }
        kL = 0;
        kU = 0;
        return image;
    }

    /**
     * @see Drawable
     * @param k is scale coefficient
     */
    @Override
    public void setK(int k)
    {
        if(k > 0)
        {
            this.curZoom += k;
        }
        if(k < 0)
        {
            if(this.curZoom + k >= 10)
            {
                this.curZoom += k;
            }
            else
            {
                this.curZoom = 10;
            }
        }
    }

    /**
     * @see Drawable
     * @param k is the offset axis X
     */
    @Override
    public void setKU(int k)
    {
        this.kU = k;
    }

    /**
     * @see Drawable
     * @param k is the offset axis Y
     */
    @Override
    public void setKL(int k)
    {
        this.kL = k;
    }

    /**
     * @see Drawable
     * @param k is new center axis X
     */
    @Override
    public void setX0(int k)
    {
        this.x0 = k;
    }

    /**
     * @see Drawable
     * @param k is new center axis Y
     */
    @Override
    public void setY0(int k)
    {
        this.y0 = k;
    }

    /**
     * @see Drawable
     * @param width is new width
     */
    @Override
    public void setWidth(int width)
    {
        this.width = width;
    }

    /**
     * @see Drawable
     * @param height is new height
     */
    @Override
    public void setHeight(int height)
    {
        this.height = height;
    }

    /**
     * @see Drawable
     */
    @Override
    public void reset()
    {
        curZoom = defCoeff;
    }

    /**
     * Calculates the value of the derivative function X(t) at parameter t
     * @param t is parameter t
     * @return value of the derivative function X(t) at parameter t
     */
    private double funcDX(double t)
    {
        return -2. * t / ((t * t - 1) * (t * t - 1));
    }

    /**
     * Calculates the value of the derivative function Y(t) at parameter t
     * @param t is parameter t
     * @return value of the derivative function Y(t) at parameter t
     */
    private double funcDY(double t)
    {
        return 1 - 5. / ((t + 2) * (t + 2));
    }

    /**
     * Calculates the value of the function X(t) at parameter t
     * @param t is parameter t
     * @return an integer value of the function at the parameter t
     */
    private int fX(double t)
    {
        return (int)((t * t) / (t * t - 1) / dstep);
    }

    /**
     * Calculates the value of the derivative function Y(t) at parameter t
     * @param t is parameter t
     * @return an integer value of the function at the parameter t
     */
    private int fY(double t)
    {
        return (int)((t * t + 1) / (t + 2) / dstep);
    }

    /**
     * Drawing coordinate line
     * @param x0 the first point's x coordinate.
     * @param y0 the first point's y coordinate.
     * @param x1 the second point's x coordinate.
     * @param y1 the second point's y coordinate.
     */
    private void drawCoordinateStroke(int x0, int y0, int x1, int y1)
    {
        for(int x = x0; x <= x1; x++)
        {
            for(int y = y0; y <= y1; y++)
            {
                if(x < width && x > 0 && y < height && y > 0)
                {
                    image.setRGB(x, y, Color.black.getRGB());
                }
            }
        }
    }

    /**
     * @see Drawable
     * @return zoom's coefficient
     */
    public int getCurZoom()
    {
        return curZoom;
    }
}
