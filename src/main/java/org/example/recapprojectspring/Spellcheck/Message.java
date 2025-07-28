package org.example.recapprojectspring.Spellcheck;

import java.util.List;

public record Message(String role, List<ResponseContent> content) {
}
