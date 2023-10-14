package codes.showme.domain.observability.incident;

import codes.showme.domain.incident.Incident;
import codes.showme.domain.incident.IncidentRepository;

public class IncidentRepositoryMock implements IncidentRepository {
    @Override
    public long save(Incident incident) {

        return 0;
    }
}
