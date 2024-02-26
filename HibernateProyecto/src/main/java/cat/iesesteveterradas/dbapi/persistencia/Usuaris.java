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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Usuaris {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private String apitoken;
    private Boolean ToS;
    private String telefon;
    private String codi_validacio;
    private String email;
    private String contrasena;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pla_id")
    private Pla pla;

    @OneToMany(mappedBy = "usuari", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Peticions> peticions = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "usuaris_grups", 
        joinColumns = @JoinColumn(name = "usuari_id"), 
        inverseJoinColumns = @JoinColumn(name = "grup_id")
    )
    private List<Grups> grups;

    

    
    public Usuaris() {
    }

    public Usuaris(String nickname, String apitoken, Boolean ToS, Pla pla, String telefon, String codi_validacio,String contrasena) {
        this.nickname = nickname;
        this.apitoken = apitoken;
        this.ToS = ToS;
        this.pla = pla; 
        this.telefon = telefon;
        this.codi_validacio = codi_validacio;
        this
    }

    public Usuaris(String nickname, String telefon,String email,String codi_validacio) {
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

    public List<Grups> getGrups() {
        return grups;
    }

    public void setGrups(List<Grups> grups) {
        this.grups = grups;
    }


    public Set<Peticions> getPeticions() {
        return peticions;
    }

    public void setPeticions(Set<Peticions> peticions) {
        this.peticions = peticions;
    }

    public void addPeticio(Peticions peticio) {
        this.peticions.add(peticio);
        peticio.setUsuari(this);
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
