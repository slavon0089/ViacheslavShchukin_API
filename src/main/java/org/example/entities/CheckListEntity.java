package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CheckListEntity (String id, String name, String idBoard, String idCard){
}
