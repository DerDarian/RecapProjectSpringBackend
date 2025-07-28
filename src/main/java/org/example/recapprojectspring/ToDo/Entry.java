package org.example.recapprojectspring.ToDo;

import lombok.With;

@With
public record Entry(String id, String description, EntryStatus status) {
    public Entry{
        if(id == null || id.isEmpty()){
            throw new IllegalArgumentException("id cannot be null or empty");
        }

        if(description==null){
            description="";
        }
        if(status==null){
            status=EntryStatus.OPEN;
        }
    }

    public Entry(String id, String description){
        this(id, description,null);
    }

    public Entry(String id, TodoDTO dto){
        this(id, dto.description(), EntryStatus.valueOf(dto.status().toUpperCase()));
    }
}
