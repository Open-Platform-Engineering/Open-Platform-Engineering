package codes.showme.domain.schedule;

import codes.showme.domain.AbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ScheduleIntegrationTest extends AbstractIntegrationTest {

    @Before
    public void init() throws Exception {
        ScheduleRuleRepository scheduleRuleRepository = new ScheduleRuleRepositoryImpl();
        Mockito.when(instanceProvider.getInstance(ScheduleRuleRepository.class)).thenReturn(scheduleRuleRepository);
    }

    @Test
    public void save() {
        ScheduleRule scheduleRule = new ScheduleRule();
        scheduleRule.setName("sre");
        scheduleRule.setTenantId(UUID.randomUUID());

        ScheduleLayer scheduleLayer = new ScheduleLayer();
        DailyRotation rotationType = new DailyRotation();
        rotationType.addDailyRestriction(LocalTime.of(12, 30), LocalTime.of(18, 10));
        rotationType.addDailyRestriction(LocalTime.of(05, 30), LocalTime.of(8, 11));
        rotationType.setHandoffTime(12, 0);
        scheduleLayer.setRotation(rotationType);
        scheduleLayer.setStartDate(LocalDate.of(2023, 7, 12));
        scheduleRule.addLayer(scheduleLayer);

        ScheduleLayer scheduleLayer2 = new ScheduleLayer();
        WeeklyRotation weeklyRotationType = new WeeklyRotation();
        weeklyRotationType.setHandoffDayOfWeek(DayOfWeek.MONDAY);
        weeklyRotationType.setHandoffTime(LocalTime.of(7, 20));
        scheduleLayer2.setRotation(weeklyRotationType);
        scheduleRule.addLayer(scheduleLayer2);

        ScheduleLayer customLayer = new ScheduleLayer();
        CustomRotation customRotationType = new CustomRotation();
        customLayer.setRotation(customRotationType);
        scheduleRule.addLayer(customLayer);

        scheduleRule.save();
        Assert.assertEquals(1l, scheduleRule.getId().longValue());

        Optional<ScheduleRule> result = ScheduleRule.findByIdAndTenantId(1l, scheduleRule.getTenantId());
        Assert.assertTrue(result.isPresent());
        ScheduleRule scheduleRuleResult = result.get();
        Assert.assertEquals("sre", scheduleRuleResult.getName());

        ScheduleLayers layers = scheduleRuleResult.getLayers();
        Assert.assertEquals(3, layers.size());

        List<ScheduleLayer> layersList = layers.getList();
        ScheduleLayer firstLayer = layersList.get(0);
        DailyRotation firstLayerRotation = (DailyRotation) firstLayer.getRotation();
        Assert.assertEquals(2, firstLayerRotation.getRestrictions().size());
        Assert.assertEquals(DailyRotation.class, firstLayer.getRotation().getClass());
        Assert.assertEquals(WeeklyRotation.class, layersList.get(1).getRotation().getClass());
        Assert.assertEquals(CustomRotation.class, layersList.get(2).getRotation().getClass());


    }
}
