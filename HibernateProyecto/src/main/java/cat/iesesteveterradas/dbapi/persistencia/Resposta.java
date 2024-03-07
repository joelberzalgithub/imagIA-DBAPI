package cat.iesesteveterradas.dbapi.persistencia;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;

@Entity
public class Resposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Lob 
    @Column(columnDefinition = "MEDIUMTEXT")
    private String text;

    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "peticions_id", unique = true)
    private Peticions peticions;

    
    public Resposta() {
    }

    
    public Resposta(String text, Peticions peticio){
        this.text = text;
        this.peticions = peticio;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Peticions getPeticions() {
        return peticions;
    }

    public void setPeticions(Peticions peticions) {
        this.peticions = peticions;
    }
}
