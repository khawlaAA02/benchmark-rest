package variantA.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="category")
public class Category {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,unique=true,length=32) private String code;
    @Column(nullable=false,length=128) private String name;
    @Column(name="updated_at",nullable=false) private LocalDateTime updatedAt = LocalDateTime.now();
    public Category() {}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getCode(){return code;} public void setCode(String code){this.code=code;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public LocalDateTime getUpdatedAt(){return updatedAt;} public void setUpdatedAt(LocalDateTime u){this.updatedAt=u;}
}
