package cat.iesesteveterradas.dbapi.persistencia;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pla {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private Integer quota;

    @OneToMany(mappedBy = "pla", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Usuaris> usuaris = new ArrayList<>();
    public Pla() {
    }

    public Pla(String nom) {
        this.nom = nom;
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

    public Integer getQuota() {
        return quota;
    }

    public void setQuota(Integer quota) {
        this.quota = quota;
    }

    public List<Usuaris> getUsuaris() {
        return usuaris;
    }

    public void setUsuaris(List<Usuaris> usuaris) {
        this.usuaris = usuaris;
    }

    // Methods to handle the bidirectional relationship
    public void addUsuari(Usuaris usuari) {
        usuaris.add(usuari);
        usuari.setPla(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pla [id=").append(id)
          .append(", nom=").append(nom)
          .append("]");
        return sb.toString();
    }
}
