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
        dT = dstep / (1. * height);
        double x1 = -1.0 * Math.sqrt(2);
        double x2 = Math.sqrt(2);
        int curX = 0;
        int curY = 0;
        for(double i = 0; i >= x1; i -= dT)
        {
            int iX = (int)(i / dstep);
            int iY = (int)fX(i);
            if((curX != iX || curY != iY))
            {
                while(Math.abs(curX - iX) != 0)
                {
                    curX--;
                    drawPixel(curX, curY);
                    drawPixel(curX, -curY);
                }
                drawPixel(iX, iY);
                drawPixel(iX, -iY);
                curX = iX;
                curY = iY;
            }
        }
        curX = 0;
        curY = 0;
        for(double i = 0; i <= x2; i += dT)
        {
            int iX = (int)(i / dstep);
            int iY = (int)fX(i);
            if((curX != iX || curY != iY))
            {
                while(Math.abs(curX - iX) != 0)
                {
                    curX++;
                    drawPixel(curX, curY);
                    drawPixel(curX, -curY);
                }
                drawPixel(iX, iY);
                drawPixel(iX, -iY);
                curX = iX;
                curY = iY;
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
        this.curZoom += k;
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

    /**
     * Function for drawing pixels
     * @param x is coordinate X
     * @param y is coordinate Y
     */
    private void drawPixel(int x, int y)
    {
        if((x > -x0) && (x < width - x0))
        {
            if((height > y0 - y) && (y < y0))
            {
                image.setRGB(x0 + x, y0 - y, Color.blue.getRGB());
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