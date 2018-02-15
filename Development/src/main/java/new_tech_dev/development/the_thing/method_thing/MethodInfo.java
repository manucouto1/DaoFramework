package new_tech_dev.development.the_thing.method_thing;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import new_tech_dev.development.base_shit.base_entity.BaseEntity;

public class MethodInfo {

	private final String name;
	private final String rawQuery;
	private final String[] qArgsNames;
	private final List<Class<?>> constructorClasses;
	private final Map<String, Type> argNameType = new HashMap<>();
	private final Class<?> returnClass;

	/*
	 * Se mira que el numero de parámetros de la consulta y del metodo de la
	 * interfaz coincidan. Se ve que tengan el mismo nombre y esten en el mismo
	 * orden si todo va bien se crean atributos que asocian nombre de parametro
	 * con tipo de parametro, tambien se guarda la query cruda y los nombres de
	 * los parametros
	 */
	public MethodInfo(Method metodo, String rawQuery, String[] qArgsNames, Class<?> V, Class<?> K) throws Exception {

		this.rawQuery = rawQuery;
		this.name = metodo.getName();
		this.qArgsNames = qArgsNames;
		this.returnClass = metodo.getReturnType();
		this.constructorClasses =  generateConstructorTypes();
		
		Type[] types = typeFromGenericParams(V,K,metodo.getParameters());
		
		if(qArgsNames != null){
			if (types.length == qArgsNames.length) {
				for (int i = 0; i < types.length; i++) {
					this.argNameType.put(qArgsNames[i].trim(), types[i]);
					// TODO Usar Spring para obtener el nombre de los parametros del metodo
				}
			} else {
				// TODO EXCEPTION numero de nombres y de tipos diferentes
			}
		} else if(types.length != 0){
			// TODO EXCEPTION numero de nombres y de tipos diferentes
		}
	}

	/*
	 * Este metodo asocia en un map el nombre de un variable con su valor
	 */
	public Map<String, Object> ArgKeyValueGenerator(Object[] args) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(qArgsNames!=null){
			for (int i = 0; i < args.length; i++) {
				result.put(qArgsNames[i].trim(), args[i]);
			}
		}
		return result;
	}
	
	public Type getType(String name ){
		return argNameType.get(name);
	}
	
	private List<Class<?>> generateConstructorTypes(){
		List<Class<?>>ccClass = new ArrayList<>();
		if(BaseEntity.class.isAssignableFrom(this.returnClass)){
			for(Field field : this.returnClass.getFields()){
				ccClass.add(field.getType());
			}
		}else{
			ccClass.add(returnClass);
		}
		return ccClass;
	}
	
	private Type[] typeFromGenericParams(Class<?> V, Class<?> K, Parameter[] parameters) {
		Type[] types = new Type[parameters.length];
		for (int x = 0; x < parameters.length; x++) {
			;
			types[x] = (parameters[x].getParameterizedType().getTypeName().equalsIgnoreCase("V")) ? V
					 : (parameters[x].getParameterizedType().getTypeName().equalsIgnoreCase("K")) ? K
							: parameters[x].getType();
		}
		return types;

	}

	// Getters
	public String getQuery() {
		return this.rawQuery;
	}

	public String getName() {
		return this.name;
	}
	
	public Class<?> getReturnType(){
		return returnClass;
	}
	
	public List<Class<?>> getConstructorTypes(){
		return this.constructorClasses;
	}
}
