package cat.iesesteveterradas.dbapi.persistencia;
import java.util.List;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;



@Entity
public class Grups {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nom;
    
    @ManyToMany(mappedBy = "grups")
    private List<Usuaris> usuaris;
    
    // Constructores, getters y setters
    
    public Grups() {
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Usuaris> getUsuaris() {
        return usuaris;
    }

    public void setUsuaris(List<Usuaris> usuaris) {
        this.usuaris = usuaris;
    }
}

