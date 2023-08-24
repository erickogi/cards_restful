package com.kogi.cards_restful.payload.request;

import jakarta.validation.constraints.NotBlank;

public class CreateCardRequest {
	@NotBlank(message = "Mandatory field is required")
	private String name;

	private String description;

	@CardColorFormat
	private String color;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
