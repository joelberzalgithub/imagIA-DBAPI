package cat.iesesteveterradas.dbapi.persistencia;

import java.util.Date;


import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;




@Entity // Indica que aquesta classe és una entitat JPA
public class Peticions {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    private String imatge;
    
    private Date data;
    private String Prompt;
    private String model;


    public Peticions(String imatge, Date data, String Prompt,String model) {
        this.imatge = imatge;
        this.data = data;
        this.Prompt = Prompt;
        this.model = model;
    }



    @ManyToOne
    @JoinColumn(name = "id_usuari")
    private Usuaris usuari;



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

    public String getImatge() {
        return this.imatge;
    }

    public void setImatge(String imatge) {
        this.imatge = imatge;
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

}
