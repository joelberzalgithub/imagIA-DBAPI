package cat.iesesteveterradas.dbapi.persistencia;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity // Indica que esta clase es una entidad JPA
public class Peticions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "peticions_paths", joinColumns = @JoinColumn(name = "peticion_id")) 
    @Column(name = "path")
    private List<String> paths; 

    private Date data;
    private String Prompt;
    private String model;

    @ManyToOne
    @JoinColumn(name = "id_usuari")
    private Usuaris usuari;

    public Peticions(List<String> paths, Date data, String Prompt, String model) {
        this.paths = paths;
        this.data = data;
        this.Prompt = Prompt;
        this.model = model;
    }

    public Peticions() {
    }

    public Usuaris getUsuari() {
        return this.usuari;
    }

    public void setUsuari(Usuaris usuari) {
        this.usuari = usuari;
    }
    

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getDate() {
        return this.data;
    }

    public void setDate(Date data) {
        this.data = data;
    }

    public String getDescripcio() {
        return this.Prompt;
    }

    public void setDescripcio(String Prompt) {
        this.Prompt = Prompt;
    }

    public List<String> getPaths() {
        return this.paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
}
