package org.example.recapprojectspring.Spellcheck;

public record OpenAIRequest(String model, String instructions, String input) {
}
