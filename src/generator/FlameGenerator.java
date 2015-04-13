package generator;

import acm.graphics.GImage;
import acm.program.GraphicsProgram;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.*;

public class FlameGenerator extends GraphicsProgram {
	//private BigDecimal px;
	//private BigDecimal py;
	private GeneratingThread genThread;
	private GeneratingThread genThread2;
	public int width = 1000;
	public int height = width;
	public int halfway = width / 2;
	public ArrayList<ArrayList<Double>> coeffs = new ArrayList<ArrayList<Double>>();
	public int coeffCount = 3;
	public float colorOff=0.5F;
	public Graphics2D imgGraphics;
	public BufferedImage image = new BufferedImage(width, height,
			BufferedImage.TYPE_INT_RGB);

	public void init(){
		for(int i=0;i<coeffCount;i++){
			coeffs.add(new ArrayList<Double>());
			for(int c=0;c<6;c++){
				coeffs.get(i).add(random(0.5,1));
				if(new Random().nextBoolean()){
					coeffs.get(i).set(c,random(-1,-0.5));
				}
			}
		}
		image.createGraphics();
		imgGraphics = (Graphics2D) image.getGraphics();
		imgGraphics.setColor(Color.black);
		imgGraphics.fillRect(0, 0, width,height);
		this.setSize(width, height);
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
			add(new GImage(image));
			pause(100);
		}
		
	}
	public void exit(){
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
					Color hsb=Color.getHSBColor(c+boss.colorOff, 1F, 50);
					if(steps>1){
						boss.plotPoint(px,py,new Color(hsb.getRed(),hsb.getGreen(),hsb.getBlue(),50));
					}
					steps++;
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
