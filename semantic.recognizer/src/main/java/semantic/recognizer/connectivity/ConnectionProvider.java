package semantic.recognizer.connectivity;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public final class ConnectionProvider {

	private static ConnectionProvider INSTANCE;

	private DataSource dataSource;

	private ConnectionProvider() throws SQLException {
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/DefaultDB"); //$NON-NLS-1$
		} catch (NamingException e) {
			throw new SQLException("Cannot find datasource", e); //$NON-NLS-1$
		}
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public synchronized static ConnectionProvider getInstance() throws SQLException {
		if (INSTANCE == null) {
			INSTANCE = new ConnectionProvider();
		}

		return INSTANCE;
	}
}