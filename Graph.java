package projectWork1;
import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import javax.swing.JPanel;

public class Graph extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Double> TempList;
	ArrayList<String> TimeList;
	static private final int BORDER_GAP = 50;
	static private final int WIDTH = 1300;
	static private final int HEIGHT = 550;
	static private final int POINT_WIDTH = 10;
	static private final Color POINT_COLOR = Color.RED;
	static private final Color LINE_COLOR = new Color(44, 102, 230, 180);
	static private final Color AXIS_COLOR = Color.GRAY;
	static private final Color GRID_COLOR = new Color(200, 200, 200, 200);
	private static final Stroke LINE_STROKE = new BasicStroke(3f);

	private double min, max, gap;
	String date;
	
	public Graph()
	{
		TempList = new ArrayList<>();
		TimeList = new ArrayList<>();
		
	}
	
	public void addTemp(Double temp)
	{
		TempList.add(temp);
		repaint();	
	}
	
	public void setDate(String date)
	{
		this.date = date;
	}
	
	public void addTime(String time)
	{
		TimeList.add(time);
	} 
	
	//get reference value for y axis - y axis is divided into 10 parts from min to max
	private void getMaxMin()
	{
		min = TempList.get(0);
		max = TempList.get(0);
		for (double temp : TempList)
		{
			
			if (temp<min)
			{
				min = temp;
			}
			else
			if (temp>max)
			{
				max = temp;
			}
		}
		
		gap =  (max-min)/9.00;
	} //end of getMaxMin
	
	
	public void saveData() throws IOException
	{
		BufferedWriter bw = null;
		String URL = "temp1.txt";
		try 
		{
			bw = new BufferedWriter(new FileWriter(URL));
			bw.write("Date : " + date);
			bw.newLine();
			for (int i = 0; i<TempList.size();i++)
			{
				String content = TimeList.get(i) + " : " + TempList.get(i) ;
				bw.append(content);
				bw.newLine();
			}
			
			
		}
		catch (IOException e)
		{
			System.out.println("Cannot save data");
		}
		catch (NullPointerException e1)
		{
			System.out.println("Check your data");
		}
		finally 
		{
			bw.flush();
			bw.close();
		}
	}
	
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
        
		getMaxMin(); 
		g.setColor(Color.WHITE);
		g.fillRect(BORDER_GAP, BORDER_GAP, WIDTH-2*BORDER_GAP, HEIGHT-2*BORDER_GAP);
		g.setColor(AXIS_COLOR);
		//draw axes
		//x
		g.drawLine(BORDER_GAP, HEIGHT-BORDER_GAP, WIDTH-BORDER_GAP , HEIGHT-BORDER_GAP);
		//y
		g.drawLine(BORDER_GAP, HEIGHT-BORDER_GAP, BORDER_GAP, BORDER_GAP);
		
		g.setColor(GRID_COLOR);
		//draw grids on axes
		//y axis - divide into 10 parts
		for (int i = 0; i < 10; i++) 
		{
	         int x0 = BORDER_GAP;
	         int x1 = WIDTH-BORDER_GAP;
	         int y0 = HEIGHT - (((i + 1) * (HEIGHT - BORDER_GAP * 2)) / 10 + BORDER_GAP);
	         int y1 = y0;
	         g.setColor(Color.BLACK);       
	         g.drawLine(x0, y0, x1, y1);
	         g.drawLine(x0, y0, BORDER_GAP-10, y1);
	         DecimalFormat df = new DecimalFormat("##.##");
	         g.drawString(df.format(min+i*gap), x0-40, y1);
	         
	    }
		//x axis - divide into n part (size of list)
		for (int i = 0; i < TempList.size() ; i++) 
		{
			//int x0 = (i+1)*xScale + BORDER_GAP;
	         int x0 = (i + 1) * (WIDTH - BORDER_GAP * 2) / (TempList.size() ) + BORDER_GAP;
	         int x1 = x0;
	         int y0 = HEIGHT - BORDER_GAP;
	         int y1 = BORDER_GAP;
	         g.drawLine(x0, y0, x1, y1);
	         g.drawLine(x0, y0, x1, y0+10);
	         g.setColor(Color.BLACK);
	         g.setFont(new Font(Font.SANS_SERIF, 5, 10));
	         
	         //draw shape/image (will be rotated)
	         if (TempList.size()>20)
	         {
	        	 if (i%2==0)
		         {
		        	 g.drawString(TimeList.get(i), x0-20, y0+20);
		         }
		         else
		         {
		        	 g.drawString(TimeList.get(i), x0-20, y0+40);
		         }
	         }
	         else
	         {
	        	 g.drawString(TimeList.get(i), x0-20, y0+20);
	         }
	         
	    }
		
		g.setColor(POINT_COLOR);
		ArrayList<Point> Points = new ArrayList<Point>();
		for (int i = 0; i < TempList.size() ; i++) 
		{
			 int border = HEIGHT-2*BORDER_GAP;
	         int x = (i+1 ) * (WIDTH - BORDER_GAP * 2) / (TempList.size() ) + BORDER_GAP-POINT_WIDTH/2;
	         int y = HEIGHT-BORDER_GAP-border/10 - POINT_WIDTH/2 - (int)( (TempList.get(i) - min)*(border-border/10)/(max-min)) ;
	         
	         g.fillOval(x, y, POINT_WIDTH, POINT_WIDTH);
	         Point point = new Point(x,y);
	         Points.add(point);
		}
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(LINE_COLOR);
		g2.setStroke(LINE_STROKE);
		//draw line
		for (int i=0; i<TempList.size()-1;i++)
		{
			int x1 = Points.get(i).x+POINT_WIDTH/2;
			int y1 = Points.get(i).y+POINT_WIDTH/2;
			int x2 = Points.get(i+1).x+POINT_WIDTH/2;
			int y2 = Points.get(i+1).y+POINT_WIDTH/2;
			g.drawLine(x1, y1, x2, y2);
		}
		
		
	}// end of paint
}
