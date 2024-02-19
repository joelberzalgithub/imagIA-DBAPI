package cat.iesesteveterradas.dbapi.persistencia;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Usuaris {
    @Id // Marca el camp com a clau primària
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la generació automàtica del valor
    private Long id;

    private String nickname;

    private String apitoken;

    private Boolean ToS;

    private String pla;

    private int telefon;

    private int codi_validacio;

    // Constructors
    public Usuaris() {
    }


    public Usuaris( String nickname, String apitoken, Boolean ToS, String pla, int telefon, int codi_validacio) {
        this.nickname = nickname;
        this.apitoken = apitoken;
        this.ToS = ToS;
        this.pla = pla;
        this.telefon = telefon;
        this.codi_validacio = codi_validacio;
    }
    

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getCodivalidacio() {
        return this.codi_validacio;
    }

    public void setNickname(int codi_validacio) {
        this.codi_validacio = codi_validacio;
    }

    public String getApitoken() {
        return this.apitoken;
    }

    public void setApitoken(String apitoken) {
        this.apitoken = apitoken;
    }

    public Boolean isToS() {
        return this.ToS;
    }

    public Boolean getToS() {
        return this.ToS;
    }

    public void setToS(Boolean ToS) {
        this.ToS = ToS;
    }

    public String getPla() {
        return this.pla;
    }

    public void setPla(String pla) {
        this.pla = pla;
    }

    public int getTelefon() {
        return this.telefon;
    }

    public void setTelefon(int telefon) {
        this.telefon = telefon;
    }


    @OneToMany(mappedBy = "usuari", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Peticions> peticions = new ArrayList<>();


    @ManyToMany
    @JoinTable(
    name = "usuaris_grups", // nombre de la tabla de unión
    joinColumns = @JoinColumn(name = "usuari_id"), // clave foránea que apunta a Usuaris
    inverseJoinColumns = @JoinColumn(name = "grup_id") // clave foránea que apunta a Grups
    )
    private List<Grups> grups;


    @OneToMany(mappedBy = "usuari", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Missatges> missatges = new ArrayList<>();

    // Getters i setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Missatges> getMissatges() {
        return missatges;
    }

    public void setMissatges(List<Missatges> missatges) {
        this.missatges = missatges;
    }

    public void addMissatge(Missatges missatge) {
        missatges.add(missatge);
        missatge.setUsuari(this);
    }

    public List<Grups> getGrups() {
        return grups;
    }

    public void setGrups(List<Grups> grups) {
        this.grups = grups;
    }


    public List<Peticions> getPeticions() {
        return peticions;
    }

    public void setPeticions(List<Peticions> peticions) {
        this.peticions = peticions;
    }

    public void addPeticio(Peticions peticio) {
        this.peticions.add(peticio);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Configuració [id=").append(id)
                
                .append(", propietats=[");
        for (Peticions propietat : peticions) {
            sb.append(propietat.toString()).append(", ");
        }
        if (!peticions.isEmpty()) {
            sb.setLength(sb.length() - 2); 
        }
        sb.append("]]");
        return sb.toString();
    }
}
