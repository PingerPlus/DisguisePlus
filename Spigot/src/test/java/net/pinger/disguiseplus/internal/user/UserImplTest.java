package net.pinger.disguiseplus.internal.user;

import net.pinger.disguiseplus.statistic.DisguiseStatistic;
import net.pinger.disguiseplus.statistic.NickStatistic;
import net.pinger.disguiseplus.statistic.SkinStatistic;
import net.pinger.disguiseplus.statistic.Statistic;
import org.junit.jupiter.api.Test;

public class UserImplTest {

    @Test
    void removeStatistic() {
        Class<? extends Statistic> statistic = DisguiseStatistic.class;

        // Check the assignable from
        System.out.println(statistic.isAssignableFrom(DisguiseStatistic.class));
        System.out.println(statistic.isAssignableFrom(SkinStatistic.class));
        System.out.println(statistic.isAssignableFrom(NickStatistic.class));
    }

}


