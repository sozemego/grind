package com.soze.grind.core.game.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JsonUtil {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	/**
	 * Loads the content of given file name to a JsonNode.
	 *
	 * @param fileName name of the file to load
	 * @return JsonNode
	 */
	public static JsonNode loadJson(String fileName) {
		try {
			return OBJECT_MAPPER.readTree(new File(fileName));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
