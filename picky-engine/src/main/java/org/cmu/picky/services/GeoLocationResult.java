package org.cmu.picky.services;

public class GeoLocationResult {

	private String formatted_address;

	private boolean partial_match;

	private Object address_components;

	private Object types;

	public String getFormatted_address() {
		return formatted_address;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	public boolean isPartial_match() {
		return partial_match;
	}

	public void setPartial_match(boolean partial_match) {
		this.partial_match = partial_match;
	}

	public Object getAddress_components() {
		return address_components;
	}

	public void setAddress_components(Object address_components) {
		this.address_components = address_components;
	}

	public Object getTypes() {
		return types;
	}

	public void setTypes(Object types) {
		this.types = types;
	}

}