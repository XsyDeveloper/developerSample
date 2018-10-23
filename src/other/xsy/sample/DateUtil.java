package other.xsy.sample;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
/**
 * 日期工具类
 * @author admin
 *
 */
public class DateUtil {
	/** 
     * 得到两个日期相差的天数 
     */  
    public static int getBetweenDay(Date date1, Date date2) {  
        Calendar d1 = new GregorianCalendar();  
        d1.setTime(date1);  
        Calendar d2 = new GregorianCalendar();  
        d2.setTime(date2);  
        int days = d2.get(Calendar.DAY_OF_YEAR)- d1.get(Calendar.DAY_OF_YEAR);  
        System.out.println("days="+days);  
        int y2 = d2.get(Calendar.YEAR);  
        if (d1.get(Calendar.YEAR) != y2) {  
            do {  
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);  
                d1.add(Calendar.YEAR, 1);  
            } while (d1.get(Calendar.YEAR) != y2);  
        }  
        return days;  
    }  
    
    public static boolean isBefore(Date date1, Date date2){
    	return date1.before(date2);
    }
    /**
     * 获取两个时间点之间的分钟数
     * @param startTime开始时间
     * @param endTime结束时间
     * @return 
     */
	public static long getDuration(String startTime, String endTime)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try
		{
			return sdf.parse(endTime).getTime()/1000/60 - sdf.parse(startTime).getTime()/1000/60;
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return 0;
		
	}
    /**
     * 获取10位随机数
     * @return
     */
	public static String getRandomPassword()
	{
		StringBuilder password = new StringBuilder();
		for(int i=0;i<10;i++)
		{
			password.append(new Random().nextInt(10));
		}
		return password.toString();
	}
}