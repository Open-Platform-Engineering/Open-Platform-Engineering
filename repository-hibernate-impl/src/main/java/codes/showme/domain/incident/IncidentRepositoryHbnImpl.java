package codes.showme.domain.incident;


import codes.showme.techlib.ioc.InstanceFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

@Component
public class IncidentRepositoryHbnImpl implements IncidentRepository {

    @Override
    public long save(Incident incident) {
        EntityManager entityManager = InstanceFactory.getInstance(EntityManager.class);
        entityManager.persist(incident);
        return incident.getId();
    }
}
