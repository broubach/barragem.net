package net.barragem.scaffold;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.barragem.persistence.entity.Validatable;
import net.barragem.persistence.entity.ValidateRequired;

public class ReflectionHelper {

	public static List<Field> getValidateRequiredFields(Validatable validatable) {
		List<Field> result = new ArrayList<Field>();
		Class clazz = validatable.getClass();
		while (!clazz.getSimpleName().equals(Object.class.getSimpleName())) {
			for (Field field : clazz.getDeclaredFields()) {
				field.getAnnotations();
				if (field.isAnnotationPresent(ValidateRequired.class)) {
					result.add(field);
				}
			}
			clazz = clazz.getSuperclass();
		}
		return result;
	}

	public static Object get(Object object, String field) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		return object.getClass().getMethod(getGetterMethod(field), null).invoke(object, null);
	}

	public static void set(Object container, String field, Object value) throws IllegalArgumentException,
			SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		container.getClass().getMethod(getSetterMethod(field), value.getClass()).invoke(container, value);
	}

	private static String getGetterMethod(String field) {
		return "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
	}

	private static String getSetterMethod(String field) {
		return "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
	}
}