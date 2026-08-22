package com.sk.hotelnotificationservice.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;//id of user who receives this notification
    private String to;
    private String subject;
    private String content;
    private String type; //ACTIVATION_EMAIL, RESET_PASSWORD_EMAIL, SUCCESSFUL_RESERVATION_EMAIL, CANCEL_RESERVATION_EMAIL, TWO_DAYS_REMINDER_EMAIL
    private Date dateCreated;

    public Notification(Long userId, String to, String subject, String content, String type, Date dateCreated) {
        this.userId = userId;
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.type = type;
        this.dateCreated = dateCreated;
    }
    public Notification(){

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
