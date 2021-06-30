package mayproject.news.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "news")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "text",columnDefinition = "TEXT")
    private String text;
    @Column(name = "time_date",columnDefinition = "TIMESTAMP")
    private Timestamp time_date;
    @Column(name = "image",columnDefinition = "TEXT")
    private String image;
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cities cities;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Categories> categories;
}
