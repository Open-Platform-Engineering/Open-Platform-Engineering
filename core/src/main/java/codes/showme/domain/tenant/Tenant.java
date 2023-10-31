package codes.showme.domain.tenant;

import codes.showme.techlib.ioc.InstanceFactory;
import jakarta.persistence.*;
import jakarta.persistence.GenerationType;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "cp_tenants")
public class Tenant implements Serializable {

    private static final long serialVersionUID = -5861660365616091245L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 32)
    private String name;

    @Column(name = "descr", length = 255)
    private String description;

    public UUID save(){
        TenantRepository instance = InstanceFactory.getInstance(TenantRepository.class);
        return instance.save(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
