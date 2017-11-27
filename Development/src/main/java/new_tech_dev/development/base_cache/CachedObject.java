package new_tech_dev.development.base_cache;

import java.util.Calendar;
import java.util.Date;

public class CachedObject implements Cacheable{
	
	private Date dateofExpiration = setExpDate(30);
	protected Integer id;
	
	public Date setExpDate(int minutesToLive) {
		Date dateofExpiration = new Date();
		if(minutesToLive != 0)
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateofExpiration);
			cal.add(Calendar.MINUTE, minutesToLive);
			dateofExpiration = cal.getTime();
		}
		return dateofExpiration;
	}
	@Override
	public boolean isExpired() {
		if(dateofExpiration != null){
			if(dateofExpiration.before(new Date())){
				System.out.println("CachedResultSet.isExpired: Expired Cache! EXPIRE TIME: " +
						dateofExpiration.toString() + " CURRENT TIME: " + new Date());
				return true;
			} else {
				System.out.println("CachedResultSet.isExpired: not Expired from Cache!");
				return false;
			}
		} else {
			return false;
		}
	}
	@Override
	public Integer getId() {
		return id;
	}

}
