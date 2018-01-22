package projectWork1;

public class DataDate {
	
	
	String date, time, wind; // not all wind data has numeric value so I use String
	double temperature;
	public DataDate(String date, String time,double temperature)
	{
		this.date = date;
		this.time = time;
		this.temperature = temperature;
	}
	
	
	//use to check if compare correct date
	public boolean isDate(String date)
	{
		if (this.date.equalsIgnoreCase(date))
		{
			return true;
		}
		else
			return false;
	}
	
	public String toString()
	{
		String str = "Date: " + date + " at " + time+ ", Temp: " + temperature
				+ "°C";
		return str;
	}
	
	
	
}
