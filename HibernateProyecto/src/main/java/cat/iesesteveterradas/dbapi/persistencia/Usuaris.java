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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Usuaris {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private String apitoken;
    private Boolean ToS;
    // Se eliminó el campo String pla; ya que ahora será una relación con la entidad Pla
    private String telefon;
    private String codi_validacio;
    private String email;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pla_id")
    private Pla pla;

    @OneToMany(mappedBy = "usuari", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Peticions> peticions = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "usuaris_grups", 
        joinColumns = @JoinColumn(name = "usuari_id"), 
        inverseJoinColumns = @JoinColumn(name = "grup_id")
    )
    private List<Grups> grups;

    @OneToMany(mappedBy = "usuari", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Missatges> missatges = new ArrayList<>();

    
    public Usuaris() {
    }

    public Usuaris(String nickname, String apitoken, Boolean ToS, Pla pla, String telefon, String codi_validacio) {
        this.nickname = nickname;
        this.apitoken = apitoken;
        this.ToS = ToS;
        this.pla = pla; 
        this.telefon = telefon;
        this.codi_validacio = codi_validacio;
    }

    public Usuaris(String nickname, String telefon, String codi_validacio, String email) {
        this.nickname = nickname;
        this.email = email;
        this.telefon = telefon;
        this.codi_validacio = codi_validacio;
    }

    
    public Pla getPla() {
        return pla;
    }

    public void setPla(Pla pla) {
        this.pla = pla;
    }
    

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCodivalidacio() {
        return this.codi_validacio;
    }

    public void setCodiValidacio(String codi_validacio) {
        this.codi_validacio = codi_validacio;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getTelefon() {
        return this.telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
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
