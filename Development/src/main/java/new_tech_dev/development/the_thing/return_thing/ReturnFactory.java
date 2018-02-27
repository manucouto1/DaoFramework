package new_tech_dev.development.the_thing.return_thing;

import java.util.List;
import java.lang.reflect.Method;

public class ReturnFactory {
	
	public static <T> Return<T,?> getReturnCaster(Method metodo, Class<T> returnClass){
		return List.class.isAssignableFrom(metodo.getReturnType()) ? new ReturnGenericList<T>(returnClass)
				: new ReturnGeneric<T>(metodo, returnClass);
	}
	
}
