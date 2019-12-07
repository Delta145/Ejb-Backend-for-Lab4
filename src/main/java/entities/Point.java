package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "points")
public class Point {
    private @Id
    @GeneratedValue
    Long id;
    private Double x;
    private Double y;
    private Double r;
    private Boolean result;

    @ManyToOne
    private User user;

}
