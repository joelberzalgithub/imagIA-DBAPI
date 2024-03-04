package cat.iesesteveterradas.dbapi.persistencia;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class Quota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer total;
    private Integer consumida;
    private Integer disponible;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "usuari_id", referencedColumnName = "id")
    private Usuaris usuari;

    public Quota() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getConsumida() {
        return consumida;
    }

    public void setConsumida(Integer consumida) {
        this.consumida = consumida;
    }

    public Integer getDisponible() {
        return disponible;
    }

    public void setDisponible(Integer disponible) {
        this.disponible = disponible;
    }

    public Usuaris getUsuari() {
        return usuari;
    }

    public void setUsuari(Usuaris usuari) {
        this.usuari = usuari;
    }

    @Override
    public String toString() {
        return "Quota{" +
                "id=" + id +
                ", total=" + total +
                ", consumida=" + consumida +
                ", disponible=" + disponible +
                ", usuari=" + (usuari != null ? usuari.getId() : null) +
                '}';
    }
}

