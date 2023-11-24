package codes.showme.domain.schedule;


import codes.showme.domain.team.Reassignable;
import codes.showme.techlib.ioc.InstanceFactory;
import io.ebean.annotation.DbJson;
import jakarta.persistence.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Table(name = "cp_schedules")
@Entity
public class ScheduleRule implements Reassignable {

    public static final int COLUMN_NAME_LENGTH = 64;
    public static final int COLUMN_DESCRIPTION_LENGTH = 1024;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name", length = COLUMN_NAME_LENGTH)
    private String name;

    @Column(name = "descr", length = COLUMN_DESCRIPTION_LENGTH)
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

    public static Optional<ScheduleRule> findById(long scheduleId) {
        ScheduleRuleRepository scheduleRuleRepository = InstanceFactory.getInstance(ScheduleRuleRepository.class);
        return scheduleRuleRepository.findById(scheduleId);
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
