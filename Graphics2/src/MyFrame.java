import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MyFrame extends JFrame
{
    /** Title of the ftrame */
    private static final String TITLE = "Lab №0";

    /** Title of the info */
    private static final String TITLEINFO = "Info";

    /** Message with information about the developer */
    private static final String INFO = "Made by Andrey Alexandrov";

    /** Width in pixels of my canvas */
    private static final int WIDTH = 600;

    /** Height in pixels of my canvas */
    private static final int HEIGHT = 400;

    /** Minimal width in pixels of my canvas */
    private static final int MINWIDTH = 600;

    /** Minimal height in pixels of my canvas */
    private static final int MINHEIGHT = 400;

    /** Menubar with buttons */
    private JMenuBar myMenuBar = null;

    /** Toolbar with buttons */
    private JToolBar myToolBar = null;

    /** Button "Help" */
    private JButton helpButton = null;

    /** Button "Exit" */
    private JButton exitButton = null;

    /** Button to zoom in */
    private JButton plusButton = null;

    /** Button to zoom out */
    private JButton minusButton = null;

    /** Button to move left */
    private JButton leftButton = null;

    /** Button to move right */
    private JButton rightButton = null;

    /** Button to move up */
    private JButton upButton = null;

    /** Button to move down */
    private JButton downButton = null;

    /** Button to reset */
    private JButton resetButton = null;

    /** Button "Draw parametric function" */
    private JButton paramFuncButton = null;

    /** Button "Draw lemniscate" */
    private JButton lemniscateButton = null;

    /** Button "Clear screen" */
    private JButton clearButton = null;

    /** Button "Settings" */

    private JButton settingsButton = null;

    /** Menu #1 */
    private JMenu file = null;

    /** Menu for drawing */
    private JMenu draw = null;

    /** Menu with supproting info */
    private JMenu help = null;

    /** Menu with settings */
    private JMenu settings = null;

    /** Submenu in the "File" */
    private JMenuItem create = null;

    /** Submenu in the "File"  */
    private JMenuItem open = null;

    /** Submenu in the "File"  */
    private JMenuItem save = null;

    /** Submenu in the "File" */
    private JMenuItem exit = null;

    /** Submenu in the "Draw". Drawing parametric function */
    private JMenuItem drawPFunc = null;

    /** Submenu in the "Draw". Drawing lemniscate */
    private JMenuItem drawLemniscate = null;

    /** Submenu in the "Draw". Clear screen */
    private JMenuItem cls = null;

    /** Submenu in the "Help". Contains info about me */
    private JMenuItem about = null;

    /** Submenu in the "Settings". Sets scale and step */
    private JMenuItem scalseAndStep = null;

    /** Class for drawing */
    private MyCanvas myCanvas = null;

    /** Scale coefficient */
    private static int k = 20;

    /** offset Y */
    private static int kU = 1;

    /** offset X */
    private static int kL = 1;

    /** For drag&drop */
    private boolean dragAndDrop = true;

    /** For zooming with mouse the wheel */
    private boolean wheelScale = true;

    /** For transmission coefficients */
    Map<String, Integer> coeff = null;

    /** Create a frame */
    public MyFrame()
    {
        coeff = new HashMap<String, Integer>();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension minFrameSize = new Dimension(MINWIDTH, MINHEIGHT);
        this.setTitle(TITLE);
        this.setMinimumSize(minFrameSize);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        initDrawPanel();
        myCanvas.initSizeAndMouse();
        initMenuBar();
        initToolBar();
        this.setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2 - this.getHeight() / 2);
        this.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                Dimension newSize = e.getComponent().getSize();
                Dimension oldSize = myCanvas.getPreferredSize();
                if(newSize.width != oldSize.width || newSize.height != oldSize.height)
                {
                    myCanvas.setSizeOfPanel(e.getComponent().getSize());
                    myCanvas.repaint();
                }
            }
        });
        this.setVisible(true);
    }

    /** Initiate panel */
    private void initDrawPanel()
    {
        myCanvas = new MyCanvas(WIDTH, HEIGHT);
        this.add(myCanvas);
    }

    /** Initiate menubar */
    private void initMenuBar()
    {
        myMenuBar = new JMenuBar();
        this.setJMenuBar(myMenuBar);
        file = new JMenu("File");
        draw = new JMenu("Draw");
        help = new JMenu("Help");
        settings = new JMenu("Setting");
        create = new JMenuItem("Create");
        open = new JMenuItem("Open");
        save = new JMenuItem("Save");
        exit = new JMenuItem("Exit");
        about = new JMenuItem("About");
        scalseAndStep = new JMenuItem("Scale & Step");
        about.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                info();
            }
        });
        exit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
        drawPFunc = new JMenuItem("Draw PFunc");
        drawPFunc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawParamFunc();
            }
        });
        drawLemniscate = new JMenuItem("Draw Lemnisate");
        drawLemniscate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawLemniscate();
            }
        });
        cls = new JMenuItem("Clear");
        cls.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                myCanvas.makeClear();
            }
        });
        scalseAndStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                showSettings();
            }
        });
        file.add(create);
        file.add(open);
        file.add(save);
        file.add(exit);
        draw.add(drawPFunc);
        draw.add(drawLemniscate);
        draw.add(cls);
        help.add(about);
        settings.add(scalseAndStep);
        myMenuBar.add(file);
        myMenuBar.add(draw);
        myMenuBar.add(help);
        myMenuBar.add(settings);
    }

    /** Initiate toolbar */
    private void initToolBar()
    {
        myToolBar = new JToolBar();
        plusButton = new JButton();
        minusButton = new JButton();
        paramFuncButton = new JButton();
        clearButton = new JButton();
        helpButton = new JButton();
        exitButton = new JButton();
        leftButton = new JButton();
        rightButton = new JButton();
        upButton = new JButton();
        downButton = new JButton();
        resetButton = new JButton();
        settingsButton = new JButton();
        leftButton = new JButton();
        lemniscateButton = new JButton();
        leftButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //kL = 1;
                coeff.put("kL", kL);
                myCanvas.updateObject(coeff);
                coeff.clear();
            }
        });
        rightButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //kL = -1;
                coeff.put("kL", -kL);
                myCanvas.updateObject(coeff);
                coeff.clear();
            }
        });
        upButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //kU = 1;
                coeff.put("kU", kU);
                myCanvas.updateObject(coeff);
                coeff.clear();
            }
        });
        downButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //kU = -1;
                coeff.put("kU", -kU);
                myCanvas.updateObject(coeff);
                coeff.clear();
            }
        });
        plusButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                updateZoom(k);
            }
        });
        minusButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                updateZoom(-k);
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myCanvas.reset();
            }
        });
        paramFuncButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawParamFunc();
            }
        });
        lemniscateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawLemniscate();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                myCanvas.makeClear();
            }
        });
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSettings();
            }
        });
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                info();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
        try
        {
            Image exitImg = ImageIO.read(getClass().getResource("exit.png"));
            Image helpImg = ImageIO.read(getClass().getResource("help.png"));
            Image clearImg = ImageIO.read(getClass().getResource("clear.png"));
            Image plusImg = ImageIO.read(getClass().getResource("plus.png"));
            Image minusImg = ImageIO.read(getClass().getResource("minus.png"));
            Image leftImg = ImageIO.read(getClass().getResource("left.png"));
            Image rightImg = ImageIO.read(getClass().getResource("right.png"));
            Image upImg = ImageIO.read(getClass().getResource("up.png"));
            Image downImg = ImageIO.read(getClass().getResource("down.png"));
            Image paramFuncImg = ImageIO.read(getClass().getResource("parametric.png"));
            Image resetImg = ImageIO.read(getClass().getResource("reset.png"));
            Image settingsImg = ImageIO.read(getClass().getResource("settings.png"));
            Image lemniscateImg = ImageIO.read(getClass().getResourceAsStream("lemniscate.png"));
            leftButton.setIcon(new ImageIcon(leftImg));
            rightButton.setIcon(new ImageIcon(rightImg));
            upButton.setIcon(new ImageIcon(upImg));
            downButton.setIcon(new ImageIcon(downImg));
            plusButton.setIcon(new ImageIcon(plusImg));
            minusButton.setIcon(new ImageIcon(minusImg));
            exitButton.setIcon(new ImageIcon(exitImg));
            helpButton.setIcon(new ImageIcon(helpImg));
            paramFuncButton.setIcon(new ImageIcon(paramFuncImg));
            clearButton.setIcon(new ImageIcon(clearImg));
            resetButton.setIcon(new ImageIcon(resetImg));
            settingsButton.setIcon(new ImageIcon(settingsImg));
            lemniscateButton.setIcon(new ImageIcon(lemniscateImg));
        } catch(IOException ioe)
        {
            System.exit(1);
        }
        myToolBar.add(plusButton);
        myToolBar.add(minusButton);
        myToolBar.add(leftButton);
        myToolBar.add(rightButton);
        myToolBar.add(upButton);
        myToolBar.add(downButton);
        myToolBar.add(paramFuncButton);
        myToolBar.add(lemniscateButton);
        myToolBar.add(resetButton);
        myToolBar.add(clearButton);
        myToolBar.add(settingsButton);
        myToolBar.add(helpButton);
        myToolBar.add(exitButton);
        myToolBar.setFloatable(false);
        this.add(myToolBar, BorderLayout.NORTH);
    }

    /** Get info about developer */
    public void info()
    {
        JOptionPane.showMessageDialog(this, INFO, TITLEINFO, JOptionPane.INFORMATION_MESSAGE);
    }

    /** Draw parametric function */
    public void drawParamFunc()
    {
        myCanvas.makeParamFunc();
    }

    /** Draw lemniscate */
    public void drawLemniscate()
    {
        myCanvas.makeLemniscate();
    }

    /** Show the panel with the settings */
    public void showSettings()
    {
        JPanel p = new JPanel(new GridLayout(3, 2));
        JLabel step = new JLabel("Step");
        JLabel scale = new JLabel("Scale");
        SpinnerNumberModel scaleModel = new SpinnerNumberModel(k, 5, 35, 1);
        SpinnerNumberModel stepModel = new SpinnerNumberModel(kU, 1, 20, 1);
        JSpinner scaleSpinner = new JSpinner(scaleModel);
        JSpinner stepSpinner = new JSpinner(stepModel);
        JCheckBox isDragDrop = new JCheckBox("Drag&Drop");
        JCheckBox isWheelScale = new JCheckBox("Mouse zooming");
        isDragDrop.setSelected(dragAndDrop);
        isWheelScale.setSelected(wheelScale);
        p.add(step);
        p.add(stepSpinner);
        p.add(scale);
        p.add(scaleSpinner);
        p.add(isDragDrop);
        p.add(isWheelScale);
        int choose = JOptionPane.showConfirmDialog(this, p, "Settings", JOptionPane.OK_CANCEL_OPTION);
        if(choose == JOptionPane.OK_OPTION)
        {
            k = (Integer)scaleSpinner.getValue();
            kU = (Integer)stepSpinner.getValue();
            kL = (Integer)stepSpinner.getValue();
            dragAndDrop = isDragDrop.isSelected();
            wheelScale = isWheelScale.isSelected();
            myCanvas.setDragAndDrop(dragAndDrop);
            myCanvas.setWheelScale(wheelScale);
        }
    }

    /**
     * Update's zoom
     * @param k is scale coefficient
     */
    public void updateZoom(int k)
    {
        int width = myCanvas.getWidth();
        int height = myCanvas.getHeight();
        int x0 = myCanvas.getX0();
        int y0 = myCanvas.getY0();
        int minZoom = myCanvas.getMinZoomCoeff();
        int curZoom = myCanvas.getCurrZoom();
        if(curZoom + k >= minZoom)
        {
            coeff.put("k", k);
            if(width / 2 - x0 < 0)
            {
                coeff.put("x0", x0 + (x0 - width / 2) * k / curZoom);
            }
            if(width / 2 - x0 > 0)
            {
                coeff.put("x0", x0 - (width / 2 - x0) * k / curZoom);
            }
            if(height / 2 - y0 < 0)
            {
                coeff.put("y0", y0 + (y0 - height / 2) * k / curZoom);
            }
            if(height / 2 - y0 > 0)
            {
                coeff.put("y0", y0 - (height / 2 - y0) * k / curZoom);
            }
            myCanvas.updateObject(coeff);
            coeff.clear();
        }
    }
}