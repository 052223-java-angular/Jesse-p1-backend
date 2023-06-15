package com.revature.music.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "forumThreads")
public class ForumThread {
    @Id
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "creationDate", nullable = false)
    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "forumThread", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<ForumComment> forumComments;


    public ForumThread(String title, String description, User user)
    {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.user = user;
        this.creationDate = new Date();
        this.forumComments = new HashSet<>();
    }

}
