package new_tech_dev.development.the_thing.return_thing;

import java.awt.List;

public class ReturnFactory {
	
	public static Return<?,?> getReturnCaster(Class<?> returnType){
		
		return List.class.isAssignableFrom(returnType)?new ReturnGenericList<>():new ReturnGeneric<>();
	}
	
}
