package com.softech.vu360.lms.webservice.client.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "topics",
    "guid",
    "message"
})
public class ExpertiesResponse {

	@JsonProperty("topics")
    private List<String> topics;
	
	@JsonProperty("guid")
	 private List<String> guid;

	@JsonProperty("message")
	private String message;

	
	public ExpertiesResponse(List<String> topics, List<String> guid,
			String message) {
		super();
		this.topics = topics;
		this.guid = guid;
		this.message = message;
	}

	public List<String> getTopics() {
		return topics;
	}

	public void setTopics(List<String> topics) {
		this.topics = topics;
	}

	
	public List<String> getGuid() {
		return guid;
	}

	public void setGuid(List<String> guid) {
		this.guid = guid;
	}

	
	public String getMessage() {
		return message;
	}

	
	public void setMessage(String message) {
		this.message = message;
	}
	
   
}
