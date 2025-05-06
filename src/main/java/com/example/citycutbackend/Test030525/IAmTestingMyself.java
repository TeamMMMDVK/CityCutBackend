package com.example.citycutbackend.Test030525;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IAmTestingMyself {
    private String name;
    private int num = 5;
    private int num2 = 7;

    public int mathing() {
        return num+num2;
    }

}
