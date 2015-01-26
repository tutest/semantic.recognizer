package semantic.recognizer.analysis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import semantic.recognizer.connectivity.ConnectionProvider;

public class AnalysisDAO {

	private static final String INSERT_TEXT_QUERY = "INSERT INTO SENTENCES VALUES  (SENTENCES_IDSEQ.NEXTVAL,?,?)";
	private static final String SELECT_TEXT_ID_QUERY = "SELECT ID FROM SENTENCES WHERE TEXT = ?";
	private static final String GET_ANALYSIS_QUERY = "SELECT * FROM \"$TA_SENTENCES_TEXT_INDEX\" WHERE ID = ?";

	public int insertText(TextRequest textReq) throws SQLException {
		try (Connection connection = ConnectionProvider.getInstance().getConnection()) {
			PreparedStatement stmnt = connection.prepareStatement(INSERT_TEXT_QUERY);
			stmnt.setString(1, textReq.getLanguage());
			stmnt.setString(2, textReq.getText());
			stmnt.executeUpdate();
			return getEntryId(connection, textReq);
		}
	}

	private int getEntryId(Connection connection, TextRequest textReq) throws SQLException {
		PreparedStatement stmnt = connection.prepareStatement(SELECT_TEXT_ID_QUERY);
		stmnt.setString(1, textReq.getText());
		ResultSet resultSet = stmnt.executeQuery();
		if (!resultSet.next()) {
			throw new IllegalStateException("The requested text is not inserted.");
		}
		return resultSet.getInt(1);
	}

	public List<TextAnalysis> getTextAnalyzes(int textId) throws SQLException {
		try (Connection connection = ConnectionProvider.getInstance().getConnection()) {
			PreparedStatement stmnt = connection.prepareStatement(GET_ANALYSIS_QUERY);
			stmnt.setLong(1, textId);
			ResultSet resultSet = stmnt.executeQuery();
			List<TextAnalysis> analyzes = new ArrayList<>();
			while (resultSet.next()) {
				TextAnalysis textAnalysis = new TextAnalysis();
				textAnalysis.setText(resultSet.getString("TA_TOKEN"));
				textAnalysis.setType(resultSet.getString("TA_TYPE"));
				analyzes.add(textAnalysis);
			}
			return analyzes;
		}
	}

}
