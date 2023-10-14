package codes.showme.domain.escalation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import io.ebean.annotation.DbJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EscalationRuleSet implements Serializable {

    private static final long serialVersionUID = 3920611273936337658L;

    private static final Logger log = LoggerFactory.getLogger(EscalationRuleSet.class);
    public static final String COLUMN_RULES = "rules";

    @JsonProperty(COLUMN_RULES)
    @DbJson
    private List<EscalationRule> rules = Lists.newLinkedList();

    public EscalationRuleSet addRule(EscalationRule rule) {
        if (Objects.isNull(rule)) {
            log.debug("Skip it add rule to a rule set, cause by a null");
            return this;
        }
        rules.add(rule);
        return this;
    }

    public List<EscalationRule> getRules() {
        return rules;
    }

    public void setRules(List<EscalationRule> rules) {
        this.rules = rules;
    }
}
