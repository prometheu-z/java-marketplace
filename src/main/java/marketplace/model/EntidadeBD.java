package marketplace.model;

import jakarta.persistence.*;

@MappedSuperclass
public class EntidadeBD {

    @Column(nullable = false)
    private boolean ativo = true;


    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
