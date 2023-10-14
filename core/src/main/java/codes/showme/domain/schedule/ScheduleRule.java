package codes.showme.domain.schedule;


import codes.showme.domain.team.Reassignable;
import codes.showme.domain.tenant.TenantAbility;
import codes.showme.techlib.ioc.InstanceFactory;
import io.ebean.annotation.DbJson;
import jakarta.persistence.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Table(name = "cp_schedules")
@Entity
public class ScheduleRule extends TenantAbility implements Reassignable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", length = 64)
    private String name;

    @Column(name = "descr", length = 1024)
    private String description;

    /**
     * The time zone of the schedule.
     */
    @Column(name = "zone_id", length = 16)
    private ZoneId zoneId;


    @Column(name = "layers")
    @DbJson
    private ScheduleLayers layers = new ScheduleLayers();

    @DbJson
    @Column(name = "final_schedule")
    private FinalSchedule finalSchedule;

    public static Optional<ScheduleRule> findByIdAndTenantId(long scheduleId, String tenantId) {
        ScheduleRuleRepository scheduleRuleRepository = InstanceFactory.getInstance(ScheduleRuleRepository.class);
        return scheduleRuleRepository.findByIdAndTenantId(scheduleId, tenantId);
    }

    public ScheduleRule addLayer(ScheduleLayer scheduleLayer) {
        layers.add(scheduleLayer);
        return this;
    }

    public FinalSchedule calculateFinalSchedule(ZonedDateTime startTime, ZonedDateTime end, ZoneId zoneId) {
        return getLayers().calculateFinalSchedule(startTime, end, zoneId);
    }


    public long save() {
        ScheduleRuleRepository scheduleRuleRepository = InstanceFactory.getInstance(ScheduleRuleRepository.class);
        return scheduleRuleRepository.save(this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ScheduleLayers getLayers() {
        return layers;
    }

    public void setLayers(ScheduleLayers layers) {
        this.layers = layers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZoneId getZoneId() {
        return zoneId;
    }

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    public FinalSchedule getFinalSchedule() {
        return finalSchedule;
    }

    public void setFinalSchedule(FinalSchedule finalSchedule) {
        this.finalSchedule = finalSchedule;
    }
}
