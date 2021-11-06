package com.example.md_back.model;
import lombok.Getter;

public enum WordType{
    WORD(1), TERM(2), DOMAIN(3), CODE(4);

    @Getter
    private int code;

    WordType(int code) {
        this.code = code;
    }
}
