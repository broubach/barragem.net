package net.barragem.scaffold;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.barragem.persistence.entity.Validatable;

public class ValidatableSampleImpl implements Validatable {

	private Validatable toValidate;

	public ValidatableSampleImpl(Validatable toValidate) {
		this.toValidate = toValidate;
	}

	public List<String> validate() {
		try {
			List<String> result = new ArrayList<String>();
			List<Field> requiredFields = ReflectionHelper.getValidateRequiredFields(toValidate);
			for (Field field : requiredFields) {
				Object content = ReflectionHelper.get(toValidate, field.getName());
				if (content == null) {
					result.add(field.getName());
				} else if (content instanceof String && content.toString().trim().equals("")) {
					result.add(field.getName());
				} else if (content instanceof Number && ((Number) content).doubleValue() == 0) {
					result.add(field.getName());
				}
			}
			return result;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}
