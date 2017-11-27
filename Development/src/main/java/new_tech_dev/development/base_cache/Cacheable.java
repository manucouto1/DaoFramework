package new_tech_dev.development.base_cache;

public interface Cacheable {
	public boolean isExpired();
	public Integer getId();
}
