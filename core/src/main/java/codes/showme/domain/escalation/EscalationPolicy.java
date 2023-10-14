package codes.showme.domain.escalation;

import codes.showme.domain.tenant.TenantAbility;
import codes.showme.techlib.ioc.InstanceFactory;
import codes.showme.techlib.pagination.Pagination;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.ebean.annotation.DbJson;
import io.ebean.annotation.DbMap;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Table(name = "cp_escalation_policies")
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class EscalationPolicy extends TenantAbility {

    private static final Logger log = LoggerFactory.getLogger(EscalationPolicy.class);
    public static final String COLUMN_CREATED_TIME = "created_time";
    public static final String COLUMN_RULE_SET = "rule_set";
    public static final String COLUMN_TAGS = "tags";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", length = 64)
    private String name;

    @Column(name = "descr", length = 256)
    private String description;

    @DbJson
    @Column(name = COLUMN_RULE_SET)
    @JsonProperty(COLUMN_RULE_SET)
    private EscalationRuleSet escalationRuleSet = new EscalationRuleSet();

    @Column(name = "handoff_notify_enabled")
    private boolean HandoffNotificationEnabled = true;


    @Column(name = COLUMN_TAGS, columnDefinition = "hstore")
    @DbMap
    @JsonProperty(COLUMN_TAGS)
    private Map<String, String> tags = new TreeMap<>();

    @Column(name = COLUMN_CREATED_TIME)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    /**
     * If on one acknowledges repeat this policy _ times
     */
    @Column(name = "no_one_ack_repeat", length = 4)
    public int noOneAckThenRepeatTimes = 0;

    public EscalationPolicy addTag(String key, String value){
        tags.put(key, value);
        return this;
    }

    public static Pagination<EscalationPolicy> list(int pageIndex, int pageSize) {
        EscalationPolicyRepository escalationPolicyRepository = InstanceFactory.getInstance(EscalationPolicyRepository.class);
        return escalationPolicyRepository.list(pageIndex, pageSize);
    }

    public static Pagination<EscalationPolicy> listExistsByScheduleTarget(int pageIndex, int pageSize, long scheduleId) {
        EscalationPolicyRepository escalationPolicyRepository = InstanceFactory.getInstance(EscalationPolicyRepository.class);
        return escalationPolicyRepository.listExistsByScheduleTarget(pageIndex, pageSize, scheduleId);
    }

    public long save(){
        EscalationPolicyRepository escalationPolicyRepository = InstanceFactory.getInstance(EscalationPolicyRepository.class);
        return escalationPolicyRepository.save(this);
    }

    public boolean isHandoffNotificationEnabled() {
        return HandoffNotificationEnabled;
    }

    public EscalationPolicy addRule(EscalationRule rule) {
        if (Objects.isNull(rule)) {
            log.debug("Skip it add rule to a escalation policy, cause by a null");
            return this;
        }
        this.escalationRuleSet.addRule(rule);
        return this;
    }

    @JsonIgnore
    public List<EscalationRule> getSortedRules(){
        return escalationRuleSet.getRules();
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setHandoffNotificationEnabled(boolean handoffNotificationEnabled) {
        HandoffNotificationEnabled = handoffNotificationEnabled;
    }

    public boolean noOneAckThenRepeatTimesEnabled() {
        return noOneAckThenRepeatTimes > 0;
    }

    public EscalationRuleSet getEscalationRuleSet() {
        return escalationRuleSet;
    }

    public int getNoOneAckThenRepeatTimes() {
        return noOneAckThenRepeatTimes;
    }

    public void setNoOneAckThenRepeatTimes(int noOneAckThenRepeatTimes) {
        this.noOneAckThenRepeatTimes = noOneAckThenRepeatTimes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
