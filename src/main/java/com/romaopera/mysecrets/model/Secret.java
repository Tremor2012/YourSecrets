package com.romaopera.mysecrets.model;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.scheduling.support.SimpleTriggerContext;

import javax.persistence.*;

@OnDelete(action = OnDeleteAction.CASCADE)
@Entity
@Data
@Table(name = "secrets")
public class Secret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String indicator;
    private String login;
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    public Secret() {
    }

    public Secret(String indicator, String login, String password, User user){
        this.indicator = indicator;
        this.login = login;
        this.password = password;
        this.author = user;
    }
    public String getAuthorName(){
        return author != null ? author.getUsername() : "null";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
