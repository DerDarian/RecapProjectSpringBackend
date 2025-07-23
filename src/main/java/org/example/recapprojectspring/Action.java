package org.example.recapprojectspring;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Action {
    final Entry newValue;
    final Entry oldValue;
}
