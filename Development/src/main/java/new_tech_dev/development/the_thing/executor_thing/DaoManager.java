package new_tech_dev.development.the_thing.executor_thing;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import new_tech_dev.development.sistems_acces.dom_acces.DomReader;
import new_tech_dev.development.the_thing.method_thing.DaoMethod;
import new_tech_dev.development.the_thing.return_thing.ReturnCaster;

public class DaoManager {

	private Map<String, DaoMethod> daoMethods = new HashMap<>();

	/*
	 * Recupera informacion de los metodos del dao y del daoXml
	 * Crea un mapa de DaoMetodos con el nombre del metodo como clave
	 */
	public DaoManager(Class<?> clazz) {
		DomReader dReader = new DomReader(clazz.getSimpleName());
		Map<String, String[]> queryArgsNames = dReader.getParams();
		Map<String, String> querys = dReader.getQuerys();
		try {
			Class<?> classV = (clazz.getGenericInterfaces() != null)
					? (Class<?>) ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[0]
					: null;
			Class<?> classK = (clazz.getGenericInterfaces() != null)
					? (Class<?>) ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[1]
					: null;

			Method[] methodsList = clazz.getMethods();
			for (int i = 0; i < methodsList.length; i++) {
				this.daoMethods.put(methodsList[i].getName(),
						new DaoMethod(methodsList[i], querys.get(methodsList[i].getName()),
								queryArgsNames.get(methodsList[i].getName()), classV, classK));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * recupera el DaoMethod del mapa, crea un PreparedStatement 
	 * pasa los argumentos, ejecuta el prepared stmt y procesa el resultado
	 */
	public Object execute(String name, Object[] args, Connection conect) {
		try {
			return ReturnCaster.execute(daoMethods.get(name).execute(conect, args),daoMethods.get(name));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
