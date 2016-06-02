package house.rest.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

public class DateUtils {
	static TimeZone zone = TimeZone.getTimeZone("GMT+8");  //时区
	static SimpleDateFormat ISO_time_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
	static SimpleDateFormat ISO_date_format = new SimpleDateFormat("yyyy-MM-dd");
	static long DayTime=1000*3600*24;
	static{
		ISO_time_format.setTimeZone(zone);
		ISO_date_format.setTimeZone(zone);
	}
	public static String date(long time){
		return ISO_date_format.format(new Date(time));
	}
	public static String[] everyDate(String fromDate,String toDate) throws ParseException{
		long fromTime=ISO_date_format.parse(fromDate).getTime();
		long toTime=ISO_date_format.parse(toDate).getTime();
		List<String> dates=new LinkedList<String>();
		
		for(long tmpTime=fromTime;tmpTime<=toTime;tmpTime+=DayTime){
			dates.add(ISO_date_format.format(new Date(tmpTime)));
		}
		return dates.toArray(new String[dates.size()]);
	}
	
	public static void main(String[] args){
		
		try {
			System.out.println(Arrays.toString(DateUtils.everyDate("2015-05-02", "2015-05-07")));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
