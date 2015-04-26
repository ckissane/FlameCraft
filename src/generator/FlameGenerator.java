package generator;

import acm.graphics.GImage;
import acm.program.GraphicsProgram;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;

public class FlameGenerator extends GraphicsProgram {
	//private BigDecimal px;
	//private BigDecimal py;
	JButton export = new JButton("Export");
	private GeneratingThread genThread;
	private GeneratingThread genThread2;
	public int width = 2000;
	public int height = width;
	public int halfway = width / 2;
	public int coeffCount = 6;
	public ArrayList<ArrayList<Double>> coeffs = new ArrayList<ArrayList<Double>>(coeffCount);
	public float colorOff=0.1F;
	public Graphics2D imgGraphics;
	public BufferedImage image = new BufferedImage(width, height,
			BufferedImage.TYPE_INT_RGB);
	public GImage display=new GImage(image);
	public float coffset=0.2F;
	public ArrayList<ArrayList<Float>> colors=new ArrayList<ArrayList<Float>>(width);
	public ArrayList<ArrayList<Integer>> shades=new ArrayList<ArrayList<Integer>>(width);
	public void init(){
		for(int i=0;i<coeffCount;i++){
			coeffs.add(new ArrayList<Double>(6));
			for(int c=0;c<6;c++){
				coeffs.get(i).add(random(0.5,1));
				if(new Random().nextBoolean()){
					coeffs.get(i).set(c,random(-1,-0.5));
				}
			}
		}
		for(int i=0;i<width;i++){
			shades.add(new ArrayList<Integer>(height));
			colors.add(new ArrayList<Float>(height));
			for(int c=0;c<height;c++){
				colors.get(i).add(0.0F);
				shades.get(i).add(0);
			}
		}
		image.createGraphics();
		imgGraphics = (Graphics2D) image.getGraphics();
		imgGraphics.setColor(Color.black);
		imgGraphics.fillRect(0, 0, width,height);
		this.setSize(1000, 1000);
		export.setActionCommand("export");
		export.addActionListener(this);
		add(export,NORTH);
		add(display);
	}
	public void run(){
		//px=random(-1,1);
		//py=random(-1,1);
		genThread=new GeneratingThread(this,Color.green);
		genThread2=new GeneratingThread(this,Color.blue);
		//float c = 0F;
		genThread.setPriority(Thread.MAX_PRIORITY);
		genThread2.setPriority(Thread.MAX_PRIORITY);
		genThread.start();
		genThread2.start();
		while(genThread.isAlive()){
			/*for(int i=0;i<1000;i++){
				int choice = new Random().nextInt(coeffCount-1);
				transform(choice,1);
				if(steps==0){
					c = 0F;
				}
				if(c==0F){
					c=choice/coeffCount;
				}
				c=(c+choice/coeffCount)/2;
				Color hsb=Color.getHSBColor(c, 1F, 50);
				plotPoint(px.doubleValue(),py.doubleValue(),new Color(hsb.getRed(),hsb.getBlue(),hsb.getGreen(),50));
			}*/
			
			this.removeAll();
			display=new GImage(image);
			display.setImage(image);
			display.scale(1000.0/width);
			add(display);
			pause(100);
		}
		
	}
	public void actionPerformed(ActionEvent e) {
		if ("export".equals(e.getActionCommand())) {
			openSave();
		}
	}
	public void exit(){
		add(new GImage(image));
			genThread.stop();
			genThread2.stop();
		super.exit();
	}

	public double random(double min, double max) {
		return (new Random().nextDouble()*(max-min)+min);
	}
	public void plotPoint( double x, double y, Color color) {
		imgGraphics.setColor(color);
		imgGraphics.drawLine((int) (x*halfway+halfway),
				(int) (y*halfway+halfway),
				(int) (x*halfway+halfway),
				(int) (y*halfway+halfway));
	}
	
	public static void saveImage(BufferedImage img, String ref) {
		try {
			String newref=ref;
			if(!ref.endsWith(".png") && !ref.endsWith(".jpg")){
				newref=ref+".png";
			}
			String format = (newref.endsWith(".png")) ? "png" : "jpg";
			ImageIO.write(img, format, new File(newref));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void openSave() {
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setName("fractal.png");
		int returnVal = fc.showSaveDialog(this.createProgramFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			println("saving: " + file.getName() +","+ file.getPath()+".");
			saveImage(this.image,file.getPath());
		} else {
			println("Save command cancelled by user.");
		}
	}

	// -----------------------------------------------------------------------------------
	private class GeneratingThread extends Thread {
		private int steps=0;
		public double px;
		public double py;
		private FlameGenerator boss;
		private Color myColor;
		public GeneratingThread(FlameGenerator runner,Color color) {
			super();
			boss=runner;
			myColor=color;
		}

		public void run() {
			px=random(-1,1);
			py=random(-1,1);
			//genThread=new GeneratingThread(this);
			float c = 0F;
			//genThread.start();
			for(int m=0;m<1000;m++){
				for(int i=0;i<100000;i++){
					int choice = new Random().nextInt(boss.coeffCount-1);
					transform(choice,2);
					//if(steps==0){
					//	c = 0F;
					//}
					if(c==0F){
						c=(float) (1.0/boss.coeffCount*choice);
					}
					c=(float) ((c+(1.0/boss.coeffCount*choice))/2);

					Color hsb=Color.getHSBColor(c, 1F, 50);

					if(steps>0){
						boss.plotPoint(px,py,new Color(hsb.getRed(),hsb.getGreen(),hsb.getBlue(),50));
					}
					transform(choice,3);
					if(steps>0){
						boss.plotPoint(px,py,new Color(hsb.getRed(),hsb.getGreen(),hsb.getBlue(),50));
					}
				}
			}
			this.stop();
		}
		public void transform(int coeffID,int method){
			ArrayList<Double> coeffShort=boss.coeffs.get(coeffID);
			double x = px*coeffShort.get(0)+py*coeffShort.get(1)+coeffShort.get(2);
			double y = px*coeffShort.get(3)+py*coeffShort.get(4)+coeffShort.get(5);
			variate(x,y,method);
		}

		public void variate(double x, double y,int method) {
			double r=(x*x)+(y*y);
			if(method==0){
				px=x;
				py=y;
			}
			if(method==1){
				px=x*Math.sin(r);//.max(new BigDecimal(-1)).min(new BigDecimal(1));
				py=y*Math.cos(r);//.max(new BigDecimal(-1)).min(new BigDecimal(1));
			}
			if(method==2){
				px=x*(1/r);//.max(new BigDecimal(-1)).min(new BigDecimal(1));
				py=y*(1/r);//.max(new BigDecimal(-1)).min(new BigDecimal(1));
			}
			if(method==3){
				px=x*Math.sin(r)-y*Math.cos(r);
				py=x*Math.cos(r)+y*Math.sin(r);
			}
			if(method==4){
				px=Math.sin(x);
				py=x*Math.sin(y);
			}
			if(method==16){
				px=2/(r+1)*y;
				py=2/(r+1)*x;
			}
			if(method==21){
				px=1/(r+1)*x;
				py=1/(r+1)*y;
			}
			if(method==27){
				px=2/(r+1)*x;
				py=2/(r+1)*y;
			}
			steps++;
			if(Math.abs(px)>1||Math.abs(py)>1){
				px=random(-1,1);
				py=random(-1,1);
				steps=0;
			}
		}
	}
	public static void main(String[] args) {
		FlameGenerator program=new FlameGenerator();
		program.setTitle("Flame Craft 1.0");
		program.start();
		
	}

}
