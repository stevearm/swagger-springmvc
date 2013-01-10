package com.mangofactory.swagger.springmvc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import com.mangofactory.swagger.ControllerDocumentation;
import com.wordnik.swagger.core.DocumentationSchema;

@Slf4j
public class ModelReader {

	private final Map<String, Class<?>> m_models = new HashMap<String, Class<?>>();
	private final Map<Class<?>, String> m_overrides = new HashMap<Class<?>, String>();
	
	public ModelReader() {
		m_overrides.put(String.class, "string");
		m_overrides.put(byte.class, "byte");
		m_overrides.put(Byte.class, "byte");
		m_overrides.put(int.class, "int");
		m_overrides.put(Integer.class, "int");
		m_overrides.put(long.class, "long");
		m_overrides.put(Long.class, "long");
		m_overrides.put(float.class, "float");
		m_overrides.put(Float.class, "float");
		m_overrides.put(double.class, "double");
		m_overrides.put(Double.class, "double");
		m_overrides.put(boolean.class, "boolean");
		m_overrides.put(Boolean.class, "boolean");
	}

	/**
	 * @param clazz
	 *            The real class
	 * @return the short name
	 */
	public String addAsModel(Class<?> clazz) {
		String name = m_overrides.get(clazz);
		if (name != null) {
			return name;
		}
		name = clazz.getSimpleName();
		Class<?> cachedClass = m_models.get(name);
		if (cachedClass == null) {
			m_models.put(name, clazz);
		} else {
			int i = 1;
			while (!cachedClass.equals(clazz)) {
				name = clazz.getSimpleName() + i;
				cachedClass = m_models.get(name);
				i++;
			}
		}
		return name;
	}

	public void exportModelDocumentation(ControllerDocumentation doc) {
		Set<String> exported = new HashSet<String>();
		boolean repeat = true;
		while (repeat) {
			Map<String, Class<?>> existingModels = new HashMap<String, Class<?>>(m_models);
			repeat = false;
			for (String name : existingModels.keySet()) {
				if (!exported.add(name)) {
					continue;
				}
				repeat = true;
				Class<?> clazz = existingModels.get(name);
				DocumentationSchema schema = new DocumentationSchema();
				schema.setName(name);
				schema.setDescription(clazz.toString());
				Map<String, DocumentationSchema> props = new HashMap<String, DocumentationSchema>();
				for (Field field : clazz.getDeclaredFields()) {
					if (Modifier.isStatic(field.getModifiers())) {
						continue;
					}
					DocumentationSchema value = new DocumentationSchema();
					value.setType(addAsModel(field.getType()));
					props.put(field.getName(), value );
				}
				schema.setProperties(props);
				doc.addModel(name, schema);
			}
		}
	}
}
