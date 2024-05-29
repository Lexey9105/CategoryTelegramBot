package ru.pavlikov.categorybot.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "category_name")
    private String name;
    private Long chatId;
    @ManyToOne
    @JoinColumn(name = "parent")
    private Category parent;
    @OneToMany(mappedBy = "parent")
    private List<Category> children;


    public Category(Category parent,String name, Long chatId) {
        this.parent = parent;
        this.name = name;
        this.chatId = chatId;
    }

}
