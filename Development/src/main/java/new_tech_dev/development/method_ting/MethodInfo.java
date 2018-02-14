package new_tech_dev.development.method_ting;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import new_tech_dev.development.return_thing.Return;
import new_tech_dev.development.return_thing.ReturnCaster;
import new_tech_dev.development.return_thing.ReturnFactory;

public class MethodInfo {

	private final String name;
	private final String rawQuery;
	private final String[] qArgsNames;
	private final Map<String, Type> argNameType = new HashMap<>();
	private final Class<?> returnType;

	/*
	 * Se mira que el numero de parámetros de la consulta y del metodo de la
	 * interfaz coincidan. Se ve que tengan el mismo nombre y esten en el mismo
	 * orden si todo va bien se crean atributos que asocian nombre de parametro
	 * con tipo de parametro, tambien se guarda la query cruda y los nombres de
	 * los parametros
	 */
	public MethodInfo(Method metodo, String rawQuery, String[] qArgsNames) throws Exception {

		this.rawQuery = rawQuery;
		this.name = metodo.getName();
		this.qArgsNames = qArgsNames;
		this.returnType = metodo.getReturnType();
		Parameter[] parameters = metodo.getParameters();
		Type[] types = metodo.getParameterTypes();
		if(qArgsNames != null){
			if (parameters.length == qArgsNames.length) {
				for (int i = 0; i < parameters.length; i++) {
					if((Type) parameters[i].getType() == types[i]){
						this.argNameType.put(parameters[i].getName(), types[i]);
					} else {
						throw new IllegalArgumentException(" TODO Exception Message");
					}
					
//					TODO Usar Spring para obtener el nombre de los parametros del metodo
//					if (!parameters[i].isNamePresent()) {
//						throw new IllegalArgumentException("Parameter names are not present!");
//					}
//					if (parameters[i].getName().equals(qArgsNames[i])) {
//						this.argNameType.put(parameters[i].getName(), types[i]);
//					} else {
//						
//					}
				}
			} else {
				// TODO EXCEPTION numero de nombres y de tipos diferentes
			}
		} else if(parameters.length != 0){
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
				result.put(qArgsNames[i], args[i]);
			}
		}
		return result;
	}
	
	public Type getType(String name ){
		return argNameType.get(name);
	}

	// Getters
	public String getQuery() {
		return this.rawQuery;
	}

	public String getName() {
		return this.name;
	}
	
	public Class<?> getReturnType(){
		return returnType;
	}

}
