package org.example.recapprojectspring.Spellcheck;

import java.util.List;

public record OpenAIResponse(List<Message> output) {
}
