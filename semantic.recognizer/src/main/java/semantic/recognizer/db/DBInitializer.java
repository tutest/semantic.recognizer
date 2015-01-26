package semantic.recognizer.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import semantic.recognizer.connectivity.ConnectionProvider;

public class DBInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(DBInitializer.class);
	private static final Pattern CREATE_PATTERN = Pattern.compile("CREATE [\\w\\s]* \"(\\w*)\".*", Pattern.DOTALL); //$NON-NLS-1$

	public DBInitializer() {
	}

	public void init() throws IOException, SQLException {
		PreparedStatement stmt = null;
		try {
			Connection con = ConnectionProvider.getInstance().getConnection();
			Iterator<Entry<String, String>> sqlStatements = getSQLStatements();
			while (sqlStatements.hasNext()) {
				Entry<String, String> entry = sqlStatements.next();
				String statement = entry.getValue();
				String objectName = getObjectName(statement);
				try {
					if (objectName == null) {
						LOGGER.debug("Statement to be executed: " + statement); //$NON-NLS-1$
						executeStatement(con, statement);
					} else {
						if (!checkIfObjectsExists(con, objectName)) {
							LOGGER.debug("Object to be created: " + objectName); //$NON-NLS-1$
							executeStatement(con, statement);
						} else {
							LOGGER.debug("Object already exists: " + objectName); //$NON-NLS-1$
						}
					}
				} catch (SQLException ex) {
					LOGGER.warn("Statement execution failed", ex); //$NON-NLS-1$
				}
			}
		} finally {
			DbUtil.closeResources(null, stmt, null);
		}
	}

	private void executeStatement(Connection con, String statement) throws SQLException {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(statement);
			boolean result = stmt.execute();
			if (!result) {
				LOGGER.debug(stmt.getUpdateCount() + " rows affected"); //$NON-NLS-1$
			}
		} finally {
			DbUtil.closeResources(null, stmt, null);
		}
	}

	private Iterator<Entry<String, String>> getSQLStatements() throws IOException {
		SQLStatements sqlStatements = new SQLStatements(DBInitializer.class, "/sql.sql");
		return sqlStatements.iterator();
	}

	private boolean checkIfObjectsExists(Connection con, String name) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement("select count(*) from OBJECTS where SCHEMA_NAME=CURRENT_SCHEMA and OBJECT_NAME=?"); //$NON-NLS-1$
			stmt.setString(1, name);
			rs = stmt.executeQuery();
			int count = 0;
			if (rs.next()) {
				count = rs.getInt(1);
			}
			return count != 0;
		} finally {
			DbUtil.closeResources(rs, stmt, null);
		}
	}

	private String getObjectName(String statement) {
		Matcher m = CREATE_PATTERN.matcher(statement);
		if (m.matches() && m.groupCount() > 0) {
			return m.group(1);
		}
		return null;
	}

}