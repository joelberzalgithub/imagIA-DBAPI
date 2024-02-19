package cat.iesesteveterradas.dbapi.persistencia;

import java.sql.Date;


import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;




@Entity // Indica que aquesta classe és una entitat JPA
public class Peticions {

    @Id // Marca el camp com a clau primària
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la generació automàtica del valor
    private Long id;
    private String imatge;
    private Date date;
    private String Descripcio;


    public Peticions(Long id, String imatge, Date date, String Descripcio) {
        this.id = id;
        this.imatge = imatge;
        this.date = date;
        this.Descripcio = Descripcio;
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

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescripcio() {
        return this.Descripcio;
    }

    public void setDescripcio(String Descripcio) {
        this.Descripcio = Descripcio;
    }

}
