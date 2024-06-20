//提出用(改)
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.*;
import javax.swing.JFrame;
import java.io.File;  // File クラスを追加
import java.io.IOException;  // IOException クラスを追加
import javax.imageio.ImageIO;  // ImageIO クラスを追加
import javax.swing.JFileChooser;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class SimpleDraw extends JFrame implements ActionListener,MouseListener,MouseMotionListener{
   
    JFileChooser fileChooser = new JFileChooser();
    private boolean selecting = false;
    private boolean copying = false;
    private Rectangle selectionBounds = new Rectangle();
    private Rectangle copiedBounds = new Rectangle();
    
    DrawPanel panel;
    int x,y = 0;
    private String command;
    BufferedImage copiedImage;
    BufferedImage openedImage;
    private int clickX;
    private int clickY;
    private int dragX;
    private int dragY;
    private int radius;
    
    private void initMenu() {
	JMenuBar menubar=new JMenuBar();
	JMenu menuFile = new JMenu("File");
	this.addMenuItem(menuFile,"Open...","Open",this);
	this.addMenuItem(menuFile,"Save...","Save",this);
	menubar.add(menuFile);

	JMenu menuPen = new JMenu("Pen");
	this.addMenuItem(menuPen, "Color...", "Color", this);
	menubar.add(menuPen);

	JMenu menuNew = new JMenu("New");
	this.addMenuItem(menuNew,"A4","A4",this);
	this.addMenuItem(menuNew,"B5","B5",this);
	this.addMenuItem(menuNew,"Default","Default",this);
	this.addMenuItem(menuNew,"SelectSize","SelectSize",this);
	menuFile.add(menuNew);
     	
	JMenu menuWidth = new JMenu("Width");
	this.addMenuItem(menuWidth, "width1", "width1", this);
	this.addMenuItem(menuWidth, "width5", "width5", this);
	this.addMenuItem(menuWidth, "width10", "width10", this);
	this.addMenuItem(menuWidth, "width20", "width20", this);
	menuPen.add(menuWidth);

	JMenu menuErase = new JMenu("Erase");
	JMenu menuSize = new JMenu("Size");
	this.addMenuItem(menuSize, "Size1", "Size1", this);
	this.addMenuItem(menuSize, "Size5", "Size5", this);
	this.addMenuItem(menuSize, "Size10", "Size10", this);
	this.addMenuItem(menuSize, "Size20", "Size20", this);
	this.addMenuItem(menuErase, "All", "All", this);
	menuErase.add(menuSize);
	menubar.add(menuErase);

	JMenu menuSelect = new JMenu("Select");
	this.addMenuItem(menuSelect, "Clear Selection", "ClearSelection", this);
        this.addMenuItem(menuSelect, "Select Area", "Select", this);
        this.addMenuItem(menuSelect, "Copy", "Copy", this);
	this.addMenuItem(menuSelect, "Paste", "Paste", this);
	menubar.add(menuSelect);

	JMenu menuDraw = new JMenu("Draw");
	menubar.add(menuDraw);

	JMenu menuCircle= new JMenu("Circle");
	this.addMenuItem(menuCircle, "Fill", "Fill", this);
	this.addMenuItem(menuCircle, "Empty", "Empty", this);
	menuDraw.add(menuCircle);

	JMenu menuRect= new JMenu("Rectangle");
	this.addMenuItem(menuRect, "Fill", "FillR", this);
	this.addMenuItem(menuRect, "Empty", "EmptyR", this);
	menuDraw.add(menuRect);

	this.setJMenuBar(menubar);
    }
    
    private void init() {
	this.setTitle("Draw Tube");
	this.setSize(400, 300);
	initMenu(); 
	panel=new DrawPanel();
	panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
	this.getContentPane().add(panel);
	JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    panel.clearBuffer(panel.getWidth(), panel.getHeight());
		    panel.createBuffer(panel.getWidth(),panel.getHeight());
	
		    panel.fillBuffer(panel.getWidth(),panel.getHeight());
	    
		}
	    });

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
        this.add(clearButton, BorderLayout.SOUTH);
	this.setVisible(true);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
        SimpleDraw frame=new SimpleDraw();
	frame.init();
    }

    private void addMenuItem
	(JMenu targetMenu, String itemName, String actionName,
	 ActionListener listener) {
	JMenuItem menuItem = new JMenuItem(itemName);
	menuItem.setActionCommand(actionName);
	menuItem.addActionListener(listener);
	targetMenu.add(menuItem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	command = e.getActionCommand();
	if ("A4".equals(command)) {
	    panel.clearBuffer(3508,2450);
	    panel.createBuffer(3508,2450);
	    System.out.println("A4 menu selected");
	}else if ("B5".equals(command)) {
	    panel.clearBuffer(3035,2150);
	    panel.createBuffer(3035,2150);
	    System.out.println("B5 menu selected");
	}else if ("Default".equals(command)) {
	    panel.clearBuffer(this.getWidth(),this.getHeight());
	    panel.createBuffer(this.getWidth(),this.getHeight());
	}else if ("SelectSize".equals(command)) {
	    panel.clearBuffer(this.getWidth(),this.getHeight());
	    panel.createBuffer(this.getWidth(),this.getHeight());
	    System.out.println("SelectSize menu selected");
	} else if ("Open".equals(command)) {
	    int returnVal = fileChooser.showOpenDialog(this);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		openedImage = panel.openFile(fileChooser.getSelectedFile());
	    }
	    System.out.println("Open menu selected");
	} else if ("Save".equals(command)) {
	    int returnVal = fileChooser.showSaveDialog(this);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		panel.saveFile(fileChooser.getSelectedFile());
	    }
	     System.out.println("Save menu selected");
	}else if ("Color".equals(command)) {
	    Color newColor = JColorChooser.showDialog(this, "Choose Color", panel.currentColor);
	    if (newColor != null) {
		panel.setPenColor(newColor);
		System.out.println("Color menu selected: " + newColor);
	    }
	}else if ("width1".equals(command)) {
	    panel.setPenWidth(2.0f);
	    System.out.println("w1 menu selected");
	}else if ("width5".equals(command)) {
	    panel.setPenWidth(5.0f);
	    System.out.println("w5 menu selected");
	}else if ("width10".equals(command)) {
	    panel.setPenWidth(10.0f);
	    System.out.println("w10 menu selected");
	}else if ("width20".equals(command)) {
	    panel.setPenWidth(20.0f);
	    System.out.println("w20 menu selected");
	}else if ("Size1".equals(command)) {
	    panel.setPenWidth(2.0f);
	    panel.setPenColor(Color.white);
	    System.out.println("e1 menu selected");
	}else if ("Size5".equals(command)) {
	    panel.setPenWidth(5.0f);
	    panel.setPenColor(Color.white);
	    System.out.println("e5 menu selected");
	}else if ("Size10".equals(command)) {
	    panel.setPenWidth(10.0f);
	    panel.setPenColor(Color.white);
	    System.out.println("e10 menu selected");
	}else if ("Size20".equals(command)) {
	    panel.setPenWidth(20.0f);
	    panel.setPenColor(Color.white);
	    System.out.println("e20 menu selected");
	}else if ("All".equals(command)) {
	    panel.clearBuffer(this.getWidth(),this.getHeight());
	    panel.createBuffer(this.getWidth(),this.getHeight());	
	    panel.fillBuffer(this.getWidth(),this.getHeight());
	    System.out.println("All menu selected");
	}else if ("ClearSelection".equals(command)) {
	    clearSelection();
	    System.out.println("select menu selected");
	}else if ("Copy".equals(command)) {
	    copy();
	    System.out.println("Copy menu selected");
	} else if ("Paste".equals(command)) {
	    System.out.println("Paste menu selected");
	}else if ("Fill".equals(command)) {	   
	    System.out.println("fill menu selected");
	}else if ("FillR".equals(command)) {	   
	    System.out.println("fillR menu selected");
	}else if ("EmptyR".equals(command)) {	   
	    System.out.println("emptyR menu selected");
	}
    }

    private void drawCircle(int centerX, int centerY, int radius) {
        panel.drawCircle(centerX, centerY, radius);
    }

    private void clearSelection() {
        selecting = false;
        selectionBounds.setBounds(0, 0, 0, 0);
        repaint();
    }
    public void startSelection(int x, int y) {
        selectionBounds.setLocation(x, y);
        selecting = true;
    }
    public void updateSelection(int x, int y) {
        if (selecting) {
            selectionBounds.setSize(x - selectionBounds.x, y - selectionBounds.y);
            repaint();
        }
    }
    public void endSelection() {
        selecting = false;
        repaint();
    }
    private void drawSelection(Graphics g) {
	g.setColor(Color.blue);
	g.drawRect(selectionBounds.x+2, selectionBounds.y+50, selectionBounds.width, selectionBounds.height);
	g.drawRect(selectionBounds.x+3, selectionBounds.y+51, selectionBounds.width-2, selectionBounds.height-2);        
    }

    public void copy(){
	copiedBounds.setBounds(selectionBounds.x - 30, selectionBounds.y - 30, selectionBounds.width, selectionBounds.height);
	
	copiedImage = panel.getBufferImage(selectionBounds.x,selectionBounds.y,selectionBounds);
	System.out.println("Selection copied at (" + selectionBounds.x + ", " + selectionBounds.y + ")");
    
	System.out.println(selectionBounds.x);
	copying = true;
	System.out.println("Selection copied.");  
    }

    private void paste(int x,int y) {
        if (copying) {
           
	    panel.pasteImage(copiedImage, x - copiedBounds.width / 2, y - copiedBounds.height / 2);
       
            copying = false;
            System.out.println("Selection pasted.");
        }
    }

    private void pasteFile(int x,int y) {
           
	panel.pasteImage(openedImage,x,y);
       
	System.out.println("File pasted.");
        
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawSelection(g);
       
    }
    
    public void mouseDragged(MouseEvent e) {
	
	    dragX = e.getX();
	    dragY = e.getY();
	    
	    if ("Select".equals(command)) {
		updateSelection(e.getX(), e.getY());
	    }else if(("Fill".equals(command))||("Empty".equals(command))){
		radius = (int) Math.sqrt(Math.pow(dragX - clickX, 2) + Math.pow(dragY - clickY, 2));		
	    }else if ("FillR".equals(command)) {
	    }else if ("EmptyR".equals(command)) {	    	
	    }else{
		panel.drawLine(x,y,dragX,dragY);
	    }
	    repaint();

       	x = e.getX();
		y = e.getY();
        
		System.out.println("mouse dragged!");
    }
    public void mouseMoved(MouseEvent e) {
	//System.out.println("mouse moved!");
    }
    public void mouseClicked(MouseEvent e){
	System.out.println("mouse clicked!");
    }
    @Override
    public void mousePressed(MouseEvent e){
	if ("Select".equals(command)){
            startSelection(e.getX(), e.getY());
    }else if("Paste".equals(command)){
	    paste(e.getX(), e.getY());
	}else if ("Open".equals(command)) {
	    pasteFile(e.getX(),e.getY());
	    System.out.println("Open menu selected");
	}else if ("Fill".equals(command)) {
	    clickX = e.getX();
	    clickY = e.getY();
	    drawCircle(clickX, clickY, radius);
 	}else if ("Empty".equals(command)) {
	    clickX = e.getX();
	    clickY = e.getY();
	    drawCircle(clickX, clickY, radius);
	}else if ("FillR".equals(command)) {
	    clickX = e.getX();
	    clickY = e.getY();
	}else if ("EmptyR".equals(command)) {
	    clickX = e.getX();
	    clickY = e.getY();
	   	}
	x = e.getX();
	y = e.getY();
	System.out.println("mouse pressed!");
    }
    public void mouseReleased(MouseEvent e){
	if ("Select".equals(command)) {
            endSelection();
        }else if ("Fill".equals(command)) {
	    for(int i =0;i<radius;i++)
		panel.drawCircle(clickX, clickY,i);
	    radius = 0;
	}else if ("Empty".equals(command)) {
	   panel.drawCircle(clickX, clickY,radius);
	    radius = 0;
	}else if ("FillR".equals(command)) {
	    dragX = e.getX();
	    dragY = e.getY();
	    panel.fillRect1(clickX, clickY,dragX-clickX, dragY-clickY);
	    dragX = 0;
	    dragY = 0;
	    repaint();
	}else if ("EmptyR".equals(command)) {
	    dragX = e.getX();
	    dragY = e.getY();
	    panel.drawRect1(clickX, clickY,dragX-clickX, dragY-clickY);
	    dragX = 0;
	    dragY = 0;
	    repaint();
	}
	System.out.println("mouse released!");	
    }
    public void mouseEntered(MouseEvent e){
	System.out.println("mouse entered!");
    }
    public void mouseExited(MouseEvent e){
	System.out.println("mouse exited!");
    }

    
    
}
