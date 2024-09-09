package com.percyyang.FantasyInn.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "chatbots")
@AllArgsConstructor
@NoArgsConstructor
public class Chatbot {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    private int age;

    private String ethnicity;

    private String gender;

    private String bio;

    private String imageUrl;

    private String personality;

}
