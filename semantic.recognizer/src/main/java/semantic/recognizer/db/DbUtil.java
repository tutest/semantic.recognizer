package semantic.recognizer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DbUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(DbUtil.class);

	private DbUtil() {
	}

	public static void closeResources(ResultSet rs, Statement stmt, Connection con) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				LOGGER.warn("Cannot close result set", e); //$NON-NLS-1$
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				LOGGER.warn("Cannot close statement", e); //$NON-NLS-1$
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				LOGGER.warn("Cannot close connection", e); //$NON-NLS-1$
			}
		}
	}

	public static void rollbackTransaction(Connection con) {
		if (con != null) {
			try {
				con.rollback();
			} catch (SQLException e) {
				LOGGER.warn("Cannot rollback transaction", e); //$NON-NLS-1$
			}
		}
	}

	public static void setStatementParameters(PreparedStatement stmt, Object... params) throws SQLException {
		if (stmt.getParameterMetaData().getParameterCount() != params.length) {
			throw new IllegalArgumentException("The parameters count in statement isn't equal to params length.");
		}
		for (int i = 0; i < params.length; i++) {
			stmt.setObject((i + 1), params[i]);
		}
	}

	public static boolean hasSingleRecord(String sql, Connection connection, Object... params) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = connection.prepareStatement(sql);
			setStatementParameters(stmt, params);
			rs = stmt.executeQuery();
			if (!rs.next()) {
				return false;
			}
			return rs.getInt(1) == 1;
		} finally {
			closeResources(rs, stmt, null);
		}
	}
}
