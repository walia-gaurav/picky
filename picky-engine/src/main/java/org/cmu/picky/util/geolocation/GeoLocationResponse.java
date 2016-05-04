package org.cmu.picky.util.geolocation;

public class GeoLocationResponse {

	private GeoLocationResult[] results;
	private String status;

	public GeoLocationResponse() {
	}

	public GeoLocationResult[] getResults() {
		return results;
	}

	public void setResults(GeoLocationResult[] results) {
		this.results = results;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}