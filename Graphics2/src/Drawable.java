import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Андрей on 21.03.14.
 */
public interface Drawable
{
    /**
     * Draw the figure
     */
    public BufferedImage paintFigure();

    /**
     * Sets the scale coefficient
     * @param k is scale coefficient
     */
    public void setK(int k);

    /**
     * Sets the offset axis X
     * @param k is the offset axis X
     */
    public void setKU(int k);

    /**
     * Sets the offset axis Y
     * @param k is the offset axis Y
     */
    public void setKL(int k);

    /**
     * Sets the center axis X
     * @param k is new center axis X
     */
    public void setX0(int k);

    /**
     * Sets the center axis Y
     * @param k is new center axis Y
     */
    public void setY0(int k);

    /**
     * Sets the width of panel
     * @param width is new width
     */
    public void setWidth(int width);

    /**
     * Sets the height of panel
     * @param height is new height
     */
    public void setHeight(int height);

    /**
     * Reset image
     */
    public void reset();

    /**
     * Gets zoom's coefficient
     */
    public int getCurZoom();
}
