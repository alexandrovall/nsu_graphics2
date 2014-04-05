import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Андрей on 03.03.14.
 */


public class MyCanvas extends JPanel
{

    /** Drawable object */
    private Drawable object = null;

    /** For drag and drop */
    private Point point = null;

    private Point mousePoint = null;

    /** Center axis X */
    private int x0 = 0;

    /** Center axis Y */
    private int y0 = 0;

    /** Scale coefficient */
    private int k = 0;

    /** For drag&drop */
    private boolean dragAndDrop = true;

    /** For wheel scale */
    private boolean wheelScale = true;


    /**
     * Constructor
     * @param width is width of the canvas
     * @param height is height of the canvas
     */
    public MyCanvas(int width, int height)
    {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.white);
    }

    /** Specify panel size and append listeners */
    public void initSizeAndMouse()
    {
        x0 = this.getWidth() / 2;
        y0 = this.getHeight() / 2;
        setDragAndDrop(dragAndDrop);
        setWheelScale(wheelScale);
    }

    /**
     * Draw the figure
     * @param g is graphics for drawing
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        if(null != object)
        {
            g2d.drawImage(object.paintFigure(), null, null);
        }
    }

    /**
     * Sets size of panel
     * @param size is new size
     */
    public void setSizeOfPanel(Dimension size)
    {
        setPreferredSize(size);
        Map<String, Integer> coord = new HashMap<String, Integer>();
        coord.put("width", this.getWidth());
        coord.put("height", this.getHeight());
        updateObject(coord);
    }

    /**
     * Drawing parametric function
     */
    public void makeParamFunc()
    {
        x0 = this.getWidth() / 2;
        y0 = this.getHeight() / 2;
        object = new PFunc(x0, y0, this.getWidth(), this.getHeight());
        repaint();
    }

    /**
     * Clears panel
     */
    public void makeClear()
    {
        object = null;
        repaint();
    }

    /**
     * Updates the coefficients of the object
     * @param coeff is map which contains the new coefficients
     */
    public void updateObject(Map<String, Integer> coeff)
    {
        if(null != object)
        {
            for(Map.Entry<String, Integer> mapEntry : coeff.entrySet())
            {
                String key = mapEntry.getKey();
                if("k".equals(key))
                {
                    k = mapEntry.getValue();
                    object.setK(k);
                }
                if("kU".equals(key))
                {
                    y0 -= mapEntry.getValue();
                    object.setKU(mapEntry.getValue());
                }
                if("kL".equals(key))
                {
                    x0 -= mapEntry.getValue();
                    object.setKL(mapEntry.getValue());
                }
                if("x0".equals(key))
                {
                    object.setX0(mapEntry.getValue());
                }
                if("y0".equals(key))
                {
                    object.setY0(mapEntry.getValue());
                }
                if("width".equals(key))
                {
                    object.setWidth(mapEntry.getValue());
                }
                if("height".equals(key))
                {
                    object.setHeight(mapEntry.getValue());
                }
            }
            repaint();
        }
    }

    /**
     * Resets the object
     */
    public  void reset()
    {
        if(null != object)
        {
            x0 = this.getWidth() / 2;
            y0 = this.getHeight() /2;
            object.setX0(x0);
            object.setY0(y0);
            object.reset();
            repaint();
        }
    }

    /**
     * Sets option "drag&drop"
     * @param flag is new value for dragAndDrop
     */
    public void setDragAndDrop(boolean flag)
    {
        dragAndDrop = flag;
        if(dragAndDrop)
        {
            if(this.getMouseListeners().length == 0)
            {
                this.addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mousePressed(MouseEvent e)
                    {
                        point = e.getPoint();

                    }
                });
            }
            if(this.getMouseMotionListeners().length == 0)
            {
                this.addMouseMotionListener(new MouseMotionListener() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        Map<String, Integer> dragdrop = new HashMap<String, Integer>();
                        x0 += e.getX() - point.x;
                        y0 += e.getY() - point.y;
                        dragdrop.put("x0", x0);
                        dragdrop.put("y0", y0);
                        updateObject(dragdrop);
                        point = e.getPoint();
                    }

                    @Override
                    public void mouseMoved(MouseEvent e)
                    {
                        mousePoint = e.getPoint();
                    }
                });
            }
        }
        else
        {
            if(this.getMouseListeners().length != 0)
            {
                this.removeMouseListener(this.getMouseListeners()[0]);
            }
            if(this.getMouseMotionListeners().length != 0)
            {
                this.removeMouseMotionListener(this.getMouseMotionListeners()[0]);
            }
        }
    }

    /**
     * Sets the option "wheelScale"
     * @param flag is new value for wheelScale
     */
    public void setWheelScale(boolean flag)
    {
        wheelScale = flag;
        if(wheelScale)
        {
            if(this.getMouseWheelListeners().length == 0)
            {
                this.addMouseWheelListener(new MouseWheelListener()
                {
                    @Override
                    public void mouseWheelMoved(MouseWheelEvent e)
                    {
                        if(e.getWheelRotation() < 0)
                        {
                            if(object != null)
                            {
                                Map<String, Integer> scale = new HashMap<String, Integer>();
                                k = 5;
                                x0 += (getWidth() / 2 - mousePoint.x) / 4;
                                y0 += (getHeight() / 2 - mousePoint.y) / 4;
                                scale.put("x0", x0);
                                scale.put("y0", y0);
                                scale.put("k", k);
                                updateObject(scale);
                            }
                        }
                        else
                        {
                            if(object != null)
                            {
                                Map<String, Integer> scale = new HashMap<String, Integer>();
                                k = -5;
                                x0 += (getWidth() / 2 - mousePoint.x) / 4;
                                y0 += (getHeight() / 2 - mousePoint.y) / 4;
                                scale.put("x0", x0);
                                scale.put("y0", y0);
                                scale.put("k", k);
                                updateObject(scale);
                            }
                        }
                    }
                });
            }
        }
        else
        {
            if(this.getMouseWheelListeners().length != 0)
            {
                this.removeMouseWheelListener(this.getMouseWheelListeners()[0]);
            }
        }
    }

    /**
     * Drawing lemniscate.
     */
    public void makeLemniscate()
    {
        x0 = this.getWidth() / 2;
        y0 = this.getHeight() / 2;
        object = new Lemniscate(x0, y0, this.getWidth(), this.getHeight());
        repaint();
    }
}
