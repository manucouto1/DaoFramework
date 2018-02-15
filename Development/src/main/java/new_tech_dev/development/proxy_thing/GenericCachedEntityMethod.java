package new_tech_dev.development.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import new_tech_dev.development.base_cache.BaseEntityCache;
import new_tech_dev.development.base_entity.BaseEntity;
import new_tech_dev.development.executor.DaoExecutor;
import new_tech_dev.development.executor_thing.DaoManager;

public class GenericCachedEntityMethod <T> implements InvocationHandler{
//	<T extends BaseEntity> problema en Cache 
	private Logger LOG = LoggerFactory.getLogger(GenericCachedEntityMethod.class);
	private BaseEntityCache<BaseEntity> cache = new BaseEntityCache<>();
	private Class<?> clazz;
	private DaoManager man;
	
	public GenericCachedEntityMethod (Class<?> clazz){
		this.clazz = clazz;
		this.man = new DaoManager(clazz);
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		String method_name = method.getName();
		
		
		
		
//		return DaoExecutor.exec(QueryExecutor.exec(DomProccesor(method, args, clazz)));
//		
		
		Type[] genericInterfaces = clazz.getGenericInterfaces();
		Type[] parameterTypes = method.getParameterTypes();
		Type[] typos = null ;
		Class<T> entityClass = null;
		DaoExecutor<T> ex = null;
		
		for (Type genericInterface : genericInterfaces) {
		    if (genericInterface instanceof ParameterizedType) {
		        Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
		        typos = genericTypes;
		        entityClass = (Class<T>) Class.forName(genericTypes[0].getTypeName());
		        ex = new DaoExecutor<>(entityClass);
		    }
		}
		
		/*
		 * TODO Asumir que la entidad extiende de BaseEntity.
		 * 		Refactorizar el executor para que traje con BaseEntity y aplicar patrones para separar los casos. 
		 */
		System.out.println(" @@## Class "+clazz.getSimpleName());
		for(Type tipo : parameterTypes){
			System.out.println(" @@## parameter Types "+tipo.getTypeName());
		}
		for(Type tipo : typos){
			System.out.println(" @@## generoc parameter Types "+tipo.getTypeName());
		}
		// Decide the method to execute the action
		LOG.info(" PROXY : Method name > "+method_name+" , parameters > "+method.getGenericParameterTypes().length);
		// Logg Typos
		String auxString = "[";
		if(typos != null){
			for(Type tipo : typos){
				auxString+=tipo.getTypeName()+", ";
			}
		}
		auxString += "]";
		LOG.info(" PROXY : Method Typos > "+auxString);
		//
		if(method.getGenericParameterTypes().length > 0){
			
			if((method.getGenericParameterTypes().length==1)){
				
				if(method_name.equalsIgnoreCase("findOne")){
					return (null != cache.getCache(args[0])) ? cache.getCache(args[0]) : cache.putCache((BaseEntity) ex.executeOne(method_name, typos, args));
				} 
				if(method_name.equalsIgnoreCase("delete")){
					return (null != ex.executeOne(method_name, typos, args) && 0 < (Integer) ex.executeOne(method_name, typos, args))? cache.deleteFromCache((Integer) args[0]) : false;
				}
				if(method_name.equalsIgnoreCase("update")){
					return (null != ex.executeOne(method_name, typos, args) && 0 < (Integer) ex.executeOne(method_name, typos, args))? cache.update((BaseEntity) args[0]) : false;
				}
				if(method_name.equalsIgnoreCase("add")){
					return (null != ex.executeOne(method_name, typos, args))? cache.putCache((BaseEntity) args[0]): false;
				}
				
			} 
			if(method.getReturnType().getSimpleName().contains("List")){
				return ex.execute(method_name, parameterTypes, args);
			}
			return ex.executeOne(method_name, parameterTypes, args);
			
		} else if(method_name.equalsIgnoreCase("findAll")) {
			return ex.executeNoParams(method_name);
				// TODO guardar en cache las entidades del findAll
		} else if(method.getReturnType().getSimpleName().contains("List")) {
			return ex.executeNoParams(method_name);
		}
	return ex.executeOneNoParams(method_name, method.getReturnType());
	} 

}
