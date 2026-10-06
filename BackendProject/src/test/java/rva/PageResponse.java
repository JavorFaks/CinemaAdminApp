package rva;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Pomocna klasa za testove: iz Spring Page odgovora citamo samo listu "content"
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageResponse<T> {

	private List<T> content;

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}
}
