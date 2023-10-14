package codes.showme.domain.observability.repository;

import codes.showme.domain.observability.Runbook;

public interface RunbookRepository {
    long save(Runbook runbook);
}
