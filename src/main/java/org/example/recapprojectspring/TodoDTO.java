package org.example.recapprojectspring;

public record TodoDTO(String id, String description, String status) {
    public TodoDTO(Entry e) {
        this(e.id(), e.description(), String.valueOf(e.status()));
    }
}
