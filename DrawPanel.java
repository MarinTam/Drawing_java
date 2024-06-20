import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.image.*;
import java.io.File;  // File クラスを追加
import java.io.IOException;  // IOException クラスを追加
import javax.imageio.ImageIO;  // ImageIO クラスを追加
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
public class DrawPanel extends JPanel {
    Color currentColor=Color.black;
    Float currentWidth=2.0f;
    BufferedImage bufferImage=null;
    Graphics2D bufferGraphics=null;
    
    public DrawPanel() {
        this.setDoubleBuffered(false);
    }
    
    public void createBuffer(int width, int height) {
	//バッファ用のImageとGraphicsを用意する
	bufferImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);
	bufferGraphics=bufferImage.createGraphics();
	//getGraphicsと似ているが、戻り値がGraphics2D。
	bufferGraphics.setBackground(Color.white);
	//バッファクリア
	bufferGraphics.clearRect(30, 30, width-60, height-60);
	//drawToBuffer(bufferGraphics);
	repaint();
    }
    
    
    public void clearBuffer(int width, int height) {
	if (null != bufferGraphics) {
            
            bufferGraphics.clearRect(30, 30, width-60, height-60);
            bufferGraphics.setBackground(Color.white);
	    //drawToBuffer(bufferGraphics);
            //repaint();  // バッファのクリア後、再描画を行う
        }
    }

    public BufferedImage openFile(File file2open){
	BufferedImage pictureImage=null;
	try {
	    pictureImage = ImageIO.read(file2open);
	} catch(Exception e){
	    System.out.println("Error: reading file="+file2open.getName());
	}
	return pictureImage;
   }
   
    public BufferedImage getBufferImage(int x,int y,Rectangle bounds) {
        BufferedImage bufferImage = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferImage.createGraphics();
	int offsetX = bounds.x;  
	int offsetY = bounds.y;  
	g.translate(-offsetX, -offsetY);
	paint(g);
	g.translate(offsetX, offsetY);
        
        g.dispose();
	return bufferImage;
    }

    public void saveFile(File file2save) {
	try {
	    ImageIO.write(bufferImage, "jpg",
			  file2save);
	} catch (Exception e) {
	    System.out.println("Error: writing file="+file2save.getName());
	    return;
	}
    }
    
    public void drawLine(int x1, int y1, int x2, int y2){
	if(null==bufferGraphics) {
	    //バッファをまだ作ってなければ作る
	    this.createBuffer(this.getWidth(),this.getHeight());//最初の描画要求
	}
	bufferGraphics.setColor(currentColor);
	bufferGraphics.setStroke(new BasicStroke(currentWidth ,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
	bufferGraphics.drawLine(x1, y1, x2, y2);
	repaint();//再描画するためpaintComponentを呼び出す。
	//repaintメソッドは親Classで定義されているがpaintComponentを呼び出す。
	
    }
	
    public void setPenColor(Color newColor) {
	currentColor = newColor;
    }
    public void setPenWidth(float newWidth) {
	currentWidth = newWidth;
    }
    
    public void drawLine1(int x1, int y1, int x2, int y2){
	Graphics2D g = (Graphics2D)this.getGraphics();
	
	g.setColor(currentColor);
	g.setStroke(new BasicStroke(currentWidth ,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));
	g.drawLine(x1, y1, x2, y2);
    }

    public void drawCircle(int centerX, int centerY, int radius) {
        if (bufferGraphics != null) {
            bufferGraphics.setColor(currentColor);
            bufferGraphics.setStroke(new BasicStroke(currentWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
            bufferGraphics.drawOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
            repaint();
        }
    }

    public void drawRect1(int x,int y,int x1,int y1){
	bufferGraphics.drawRect(x, y,x1, y1);
      
    }
    public void fillRect1(int x,int y,int x1,int y1){
	bufferGraphics.fillRect(x, y,x1, y1);
      
    }

    public void pasteImage(BufferedImage image, int x, int y) {
	bufferGraphics.drawImage(image, x, y, this);
	repaint();
    }
    public void fillBuffer(int width,int height){
	Graphics2D g = (Graphics2D)this.getGraphics();	
	g.setColor(Color.white);
	g.fillRect(30, 30, width-60, height-60);
    }

    public void drawRect1(int width,int height){
	Graphics2D g = (Graphics2D)this.getGraphics();	
	g.setColor(Color.white);
	g.fillRect(30, 30, width-60, height-60);
    }
    
    public void drawToBuffer(Graphics2D g) {
        Dimension d = getSize();
        g.setColor(new Color(100, 64, 52));
        g.fillRect(0, 0, d.width, 30);
        g.fillRect(0, 0, 30, d.height);
        g.fillRect(0, d.height - 30, d.width, 30);
        g.fillRect(d.width - 30, 0, 30, d.height);
        g.setColor(new Color(116, 120, 100));

        g.drawLine(0, 0, 30, 30);
        g.drawLine(0, d.height, 30, d.height - 30);
        g.drawLine(d.width - 30, 30, d.width, 0);
        g.drawLine(d.width - 30, d.height - 30, d.width, d.height);
        g.setColor(Color.red);
        g.fillRect(100, 5, 48, 20);
        g.setColor(Color.white);
        g.setFont(new Font("Serif", Font.BOLD, 20));
        g.drawString("Draw Tube", 50, 22);

        for (int i = 0; i < 21; i++) {
            g.setColor(Color.red);
            g.drawOval(d.width - 150 - i / 2, d.height - 45 - i / 2, i, i);
            g.setColor(Color.blue);
            g.drawOval(d.width - 125 - i / 2, d.height - 45 - i / 2, i, i);
            g.setColor(Color.green);
            g.drawOval(d.width - 100 - i / 2, d.height - 45 - i / 2, i, i);
            g.setColor(Color.black);
            g.drawOval(d.width - 75 - i / 2, d.height - 45 - i / 2, i, i);
        }
    }
    
    public void paintComponent(Graphics g) {
	Dimension d = getSize();
	super.paintComponent(g);//他に描画するものがあるかもしれないので親を呼んでおく
	if(null!=bufferImage) g.drawImage(bufferImage,0,0,this);
	g.setColor(new Color(100,64,52));
	g.fillRect(0, 0, d.width, 30);
	g.fillRect(0, 0, 30, d.height);
	g.fillRect(0, d.height-30, d.width, 30);
	g.fillRect(d.width-30, 0, 30, d.height);
	g.setColor(new Color(116,120,100));
	
	g.drawLine(0, 0, 30, 30);
	g.drawLine(0, d.height, 30, d.height-30);
	g.drawLine(d.width-30, 30, d.width, 0);
	g.drawLine(d.width-30, d.height-30, d.width, d.height);
	g.setColor(Color.red);
	g.fillRect(100,5,48,20);
	g.setColor(Color.white);
	g.setFont(new Font("Serif",Font.BOLD,20));
	g.drawString("Draw Tube",50,22);
	//g.fillRect(30, 30, d.width-60, d.height-60);
	
	
    } 
}
/* 
   drawArc 円弧を描く
   drawLine 直線を描く
   drawOval 楕円を描く
   drawPolygon 多角形を描く
   drawPolyline 折れ線を描く
   drawRect 長方形を描く
   drawString 文字列を描く
   setColor 描画色を指定する
*/
