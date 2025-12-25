package marketplace.dao;

import jakarta.persistence.EntityManager;
import marketplace.model.Compra;

public class CompraDAO extends DAO<Compra>{
    public CompraDAO() {
        super(Compra.class);
    }
}
