package semantic.recognizer.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLStatements {

	private static Pattern PATTERN = Pattern.compile("--key:([\\w\\.]+)\\s?.*");

	private Map<String, String> statements;

	public SQLStatements(Class<?> clazz, String resourcePath) throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = clazz.getResourceAsStream(resourcePath);
			if (inputStream == null) {
				throw new IOException("Resource not found: " + resourcePath);
			}
			statements = readStatements(inputStream);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	public SQLStatements(String content) throws IOException {
		statements = readStatements(new BufferedReader(new StringReader(content)));
	}

	public SQLStatements(InputStream input) throws IOException {
		statements = readStatements(input);
	}

	public String get(String key) {
		return statements.get(key);
	}

	public Iterator<Entry<String, String>> iterator() {
		return statements.entrySet().iterator();
	}

	private Map<String, String> readStatements(InputStream inputStream) throws IOException {
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			return readStatements(reader);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	private Map<String, String> readStatements(BufferedReader reader) throws IOException {
		StringBuilder buf = new StringBuilder();
		String key = null;
		Map<String, String> stmts = new LinkedHashMap<String, String>();

		String line = null;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("--key:")) {
				Matcher matcher = PATTERN.matcher(line);
				if (!matcher.matches() || matcher.groupCount() < 1) {
					throw new IllegalArgumentException("Invalid key specification: " + line);
				}

				if (key != null) {
					stmts.put(key, buf.toString());
					buf.delete(0, buf.length());
				}

				String newKey = matcher.group(1);
				if (stmts.containsKey(newKey)) {
					throw new IllegalArgumentException("Duplicate key: " + newKey);
				}
				key = newKey;
			} else {
				buf.append(line).append("\n");
			}
		}

		if (key != null) {
			stmts.put(key, buf.toString().trim());
		}

		return stmts;
	}
}
