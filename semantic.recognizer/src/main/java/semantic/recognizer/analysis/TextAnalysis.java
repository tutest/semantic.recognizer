package semantic.recognizer.analysis;

public class TextAnalysis {

	private String text;
	private String type;

	public TextAnalysis() {

	}

	public TextAnalysis(String text, String type) {
		super();
		this.text = text;
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
