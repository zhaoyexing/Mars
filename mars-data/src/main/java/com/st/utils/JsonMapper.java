package com.st.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;


public class JsonMapper {

	private static ObjectMapper mapper = new ObjectMapper();

	private JsonMapper() {
	}

	public static String obj2json(Object obj) throws Exception {
		StringWriter strWriter = new StringWriter();
		JsonGenerator generator = null;
		try {
			generator = new JsonFactory().createGenerator(strWriter);
			mapper.writeValue(generator, obj);
			String json = strWriter.toString();
			return json;
		} catch (IOException e) {
			throw new Exception(e.getCause());
		} finally {
			try {
				if (generator != null) {
					generator.close();
				}
				strWriter.close();
			} catch (IOException e) {
				throw new Exception(e.getCause());
			}
		}
	}

}
