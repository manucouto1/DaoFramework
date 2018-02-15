package new_tech_dev.development.base_shit.base_cache;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CachedObject implements Cacheable{
	
	private Logger LOG = LoggerFactory.getLogger(CachedObject.class);
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
				LOG.info("CachedResultSet.isExpired: Expired Cache! EXPIRE TIME: " +
						dateofExpiration.toString() + " CURRENT TIME: " + new Date());
				return true;
			} else {
				LOG.info("CachedResultSet.isExpired: not Expired from Cache!");
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
