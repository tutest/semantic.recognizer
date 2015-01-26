package semantic.recognizer.startup;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import semantic.recognizer.db.DBInitializer;

public class AppInitializer implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent ctxEvnt) {
		try {
			new DBInitializer().init();
		} catch (IOException | SQLException ex) {
			throw new IllegalStateException("Failed to initialize the DB", ex);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent ctxEvnt) {
		// nothing to happens here
	}

}
