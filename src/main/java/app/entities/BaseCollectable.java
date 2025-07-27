package app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "base_collectable")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseCollectable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "initial_price")
    private String initialPrice;

    @Column(name = "market_price")
    private String marketPrice;

    @Column(name = "description", nullable = false)
    private Long quantity;
}
