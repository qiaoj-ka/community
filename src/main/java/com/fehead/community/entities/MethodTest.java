package com.fehead.community.entities;

import lombok.Data;

import java.util.List;

@Data
public class MethodTest {
    private String text;
    private Boolean disabled;
    private List<Test1> children;
}
