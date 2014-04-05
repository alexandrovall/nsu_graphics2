import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: Андрей
 * Date: 04.04.14
 * Time: 11:42
 * To change this template use File | Settings | File Templates.
 */
public class Lemniscate implements Drawable
{

    /** Image for drawing */
    BufferedImage image = null;

    /** Scale coefficient */
    private int coeff = 20;

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
    private double dstep = 1. / coeff;

    /** Delta t */
    private double dT = 0;

    /** Default value of coeff. Need for correct reset */
    private int defCoeff = 20;


    private int a = 1;

    /**
     * Constructor
     * @param x0 center of axis X
     * @param y0 center of axis Y
     * @param width is width of panel
     * @param height is height of panel
     */
    public Lemniscate(int x0, int y0, int width, int height)
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
        dstep = 1. / coeff;
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
        for(int i = x0 + coeff; i <= width; i += coeff)
        {
            drawCoordinateStroke(i, y0 - coeff / 5, i, y0 + coeff / 5);
        }
        for(int i = x0 - coeff; i >= 0; i -= coeff)
        {
            drawCoordinateStroke(i, y0 - coeff / 5, i, y0 + coeff / 5);
        }
        for(int i = y0 + coeff; i <= height; i += coeff)
        {
            drawCoordinateStroke(x0 - coeff / 5, i, x0 + coeff / 5, i);
        }
        for(int i = y0 - coeff; i >= 0; i -= coeff)
        {
            drawCoordinateStroke(x0 - coeff / 5, i, x0 + coeff / 5, i);
        }
        dT = dstep / (1. * height);
        double x1 = -1.0 * Math.sqrt(2);
        double x2 = Math.sqrt(2);
        double curX = x1;
        double curY = fX(x1);
        for(double i = x1 + dT; i <= x2; i += dT)
        {
            double x = i;
            double y = fX(i);
            if((curX != i || curY != y) && ((int)(x / dstep) > -x0) && ((int)(x / dstep) < width - x0))
            {
                if((height > y0 - y) && (y < y0))
                {
                    image.setRGB(x0 + (int)(x / dstep), y0 - (int)y, Color.blue.getRGB());
                }
                if((height > y0 + y) && (-y < y0))
                {
                    image.setRGB(x0 + (int)(x / dstep), y0 + (int)y, Color.blue.getRGB());
                }
                curX = x;
                curY = y;
            }
        }
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
            this.coeff += k;
        }
        if(k < 0)
        {
            if(this.coeff + k >= 10)
            {
                this.coeff += k;
            }
            else
            {
                this.coeff = 10;
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
        coeff = defCoeff;
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
     * Calculates the value of the function y(x) at point x
     * @param x is parameter x
     * @return value of the function y(x) at point x
     */
    private double fX(double x)
    {
        return (Math.sqrt(Math.sqrt(a*a*a*a + 4 * x * x * a * a) - x * x - a * a) / dstep);
    }
}
