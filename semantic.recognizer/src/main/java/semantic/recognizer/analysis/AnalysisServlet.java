package semantic.recognizer.analysis;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class AnalysisServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String TEXT_ID_QUERY_PARAM = "textId";
	private static final String JSON_CONTENT_TYPE = "application/json";

	private final AnalysisDAO analysisDAO;

	private final Gson gson;

	public AnalysisServlet() {
		this.analysisDAO = new AnalysisDAO();
		this.gson = new Gson();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int textId = Integer.valueOf(request.getParameter(TEXT_ID_QUERY_PARAM));
		List<TextAnalysis> analyzes = getTextAnalyzes(textId);
		response.getWriter().print(gson.toJson(analyzes));
		response.setContentType(JSON_CONTENT_TYPE);
	}

	private List<TextAnalysis> getTextAnalyzes(int textId) throws IOException {
		try {
			return analysisDAO.getTextAnalyzes(textId);
		} catch (SQLException ex) {
			throw new IOException("Failed to get text analysis for text with id " + textId, ex);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TextRequest textReq = getTextRequest(request);
		int textId;
		try {
			textId = analysisDAO.insertText(textReq);
		} catch (SQLException ex) {
			throw new IOException("Failed to insert text", ex);
		}
		response.getWriter().print(textId);
	}

	private TextRequest getTextRequest(HttpServletRequest request) throws IOException {
		try (InputStreamReader reader = new InputStreamReader(request.getInputStream())) {
			return gson.fromJson(reader, TextRequest.class);
		}
	}
}
