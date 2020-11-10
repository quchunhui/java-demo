package rexel.schedule;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

@Configuration
public class MyScheduledTask implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        System.out.println("---------");

        scheduledTaskRegistrar.addTriggerTask(() -> {
            TaskService taskService = new TaskService();
            taskService.println();
        }, triggerContext -> {
            CronTrigger trigger = new CronTrigger("0/5 0 0 * * ?");
            return trigger.nextExecutionTime(triggerContext);
        });

        scheduledTaskRegistrar.addCronTask(() -> {
            System.out.println("aaaaaaaaaaaaa");
        }, "0/5 0 0 * * ?");
    }

    private class TaskService {
        private void println() {
            System.out.println("--------task run.-----------");
        }
    }
}
