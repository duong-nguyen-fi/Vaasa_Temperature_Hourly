package projectWork1;

// this application will read the data from address, store all data line by line to array
// get available dates from the data and show them to the combobox
// user selecte the date from combobox, click Draw then the application will 
// use timeEET and temparature of the selected date to draw a chart demonstrate the data.

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import java.net.URL;
import java.net.URLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class Main extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static ArrayList<DataDate> Dates = new ArrayList(); 
	Graph pnlGraph;
	JComboBox<String> cbBox;
	JButton BtnDraw = new JButton("Draw");
	
	public Main()
	{
		//read data from url and put to arraylist
		getDataFromURL();	
		
		//cbBox let user choose what day to draw/ to see
		cbBox = new JComboBox<String>();
		initCbbox();
		JPanel pnlTop = new JPanel();
		pnlTop.add(cbBox);
		pnlTop.add(BtnDraw);
		
		// on button click
		
		JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem SaveMenu = new JMenuItem("Save");
		JMenuItem ExitMenu = new JMenuItem("Exit");
		menubar.add(menu);
		menu.add(SaveMenu);
		menu.add(ExitMenu);
		
		SaveMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("click");
				try {
					pnlGraph.saveData();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		BtnDraw.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				if(pnlGraph != null) // if pnl avaiable
					remove(pnlGraph); // remove the panel from the frame
				String selectedDate =cbBox.getSelectedItem().toString();
				pnlGraph = new Graph(); // initialization
				
				addValueToGraph(selectedDate); //see below
				add(pnlGraph,BorderLayout.CENTER); // add the panel to the frame
				
				validate(); // validate/update the frame
			}
		});
		// end of on button click
		setJMenuBar(menubar);
		//add(menubar, BorderLayout.NORTH);
		add(pnlTop,BorderLayout.NORTH);
		setSize(1350,700);
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Main main = new Main();
		main.setVisible(true);
		
		
	}
	
	//read data from address and put data line by line to arraylist
	static private void getDataFromURL()
	{
		String address = 
				"http://api.wunderground.com/api/571e1dcd52691742/hourly/q/FI/VAASA.json"
				//"http://api.wunderground.com/api/571e1dcd52691742/conditions/q/FI/VAASA.json"
				;
		try
		{
			JSONParser parser = new JSONParser();
			URL url = new URL(address);
			URLConnection uc = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
            while ((line = br.readLine()) != null) {  		


            	sb.append(line);
			}
            JSONObject resp = (JSONObject) parser.parse(sb.toString());
			JSONArray hourly = (JSONArray) resp.get("hourly_forecast");
			for(Object o : hourly)
			{
				JSONObject hourObj = (JSONObject)o;
				JSONObject temp = (JSONObject)hourObj.get("temp");
				Double metric =Double.parseDouble(temp.get("metric").toString());
				
				JSONObject FCTTIME = (JSONObject)hourObj.get("FCTTIME");
				String hour = FCTTIME.get("hour_padded").toString();
				String month = FCTTIME.get("mon_padded").toString();
				String year = FCTTIME.get("year").toString();
				String day = FCTTIME.get("mday_padded").toString();
				String mdate = day+"-"+month+"-"+year;
				System.out.println(mdate);
				DataDate date = new DataDate(mdate,hour,metric);
				Dates.add(date);
			}
            
		}
		catch(MalformedURLException ex1) {
			System.out.println("URL cannot read");
		}catch(IOException ex2) {
			System.out.println("IO problem");
		}
		catch (NullPointerException ex3)
		{
			ex3.getMessage();
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// this will add temperature and time data to the pnlGraph that 
	// have the date match with the selected date
	private void addValueToGraph(String choseDate)
	{
		for (DataDate date : Dates)
		{
			pnlGraph.setDate(choseDate);
			if (date.isDate(choseDate))
			{
				pnlGraph.addTemp(date.temperature);
				pnlGraph.addTime(date.time);
				
			}
		}
	}
	
	// we want to show distinct values of dates avaiable
	private void initCbbox()
	{
		// create a new empty arraylist
		ArrayList<String> diffDate = new ArrayList<>();
		
		//check for avaiable date and add the diffDate array
		for (DataDate date : Dates)
		{
			
			if (!diffDate.contains(date.date)) // if the arraylist did not contain the date
			{
				diffDate.add(date.date); // we add the date to the list 
				
			}
		}
		//add item from diffDate array to combobox
		for (String date:diffDate)
		{
			cbBox.addItem(date);
		}
		
	}
}
