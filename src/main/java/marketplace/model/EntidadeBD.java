package marketplace.model;

import jakarta.persistence.*;

@MappedSuperclass
public class EntidadeBD {

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(nullable = false)
    private boolean bot = false;


    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }
}
