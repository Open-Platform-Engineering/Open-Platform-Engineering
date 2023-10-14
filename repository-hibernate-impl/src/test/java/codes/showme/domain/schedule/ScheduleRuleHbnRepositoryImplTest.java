package codes.showme.domain.observability.schedule;

import codes.showme.domain.AbstractHibernateIntegrationTest;
import codes.showme.domain.schedule.ScheduleLayer;
import codes.showme.domain.schedule.ScheduleLayers;
import codes.showme.domain.schedule.ScheduleRuleRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ScheduleRuleHbnRepositoryImplTest extends AbstractHibernateIntegrationTest {

    @Before
    public void setUp() throws Exception {
        ScheduleRuleHbnRepositoryImpl scheduleRuleHbnRepository = new ScheduleRuleHbnRepositoryImpl();
        Mockito.when(instanceProvider.getInstance(ScheduleRuleRepository.class)).thenReturn(scheduleRuleHbnRepository);
    }

    @Test
    public void name() {
        ScheduleRule scheduleRule = new ScheduleRule();
        ScheduleLayers scheduleLayers = new ScheduleLayers();
        ScheduleLayer scheduleLayer = new ScheduleLayer();
        scheduleLayer.setName("layername");
        scheduleLayers.add(scheduleLayer);
        scheduleRule.setTimeZone("UTC");
        long id = scheduleRule.save();
    }
}
