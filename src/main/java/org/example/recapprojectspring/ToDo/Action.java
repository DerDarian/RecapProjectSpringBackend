package org.example.recapprojectspring.ToDo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Action {
    final Entry newValue;
    final Entry oldValue;
}
