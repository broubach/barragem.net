package net.barragem.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.barragem.persistence.entity.Validatable;
import net.barragem.persistence.entity.ValidateRequired;

public class ReflectionHelper {

	public static List<Field> getValidateRequiredFields(Validatable validatable) {
		List<Field> result = new ArrayList<Field>();
		for (Field field : validatable.getClass().getDeclaredFields()) {
			field.getAnnotations();
			if (field.isAnnotationPresent(ValidateRequired.class)) {
				result.add(field);
			}
		}
		return result;
	}

	public static Object get(Object object, String field) throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		return object.getClass().getMethod(getGetterMethod(field), null).invoke(object, null);
	}

	private static String getGetterMethod(String field) {
		return "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
	}
}
