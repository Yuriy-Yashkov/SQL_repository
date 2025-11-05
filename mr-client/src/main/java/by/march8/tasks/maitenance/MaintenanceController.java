package by.march8.tasks.maitenance;

import by.march8.ecs.Bootstrap;
import by.march8.tasks.maitenance.doubling.EAN13Doubling;

public class MaintenanceController {


    private final Bootstrap bootstrap;

    public MaintenanceController(final Bootstrap bootstrap) {

        this.bootstrap = bootstrap;
        doMaintenanceTask();
    }

    private void doMaintenanceTask() {
        for (String s : bootstrap.getArguments()) {
            if (s.equals("-ean")) {
                new EAN13Doubling();
            }
        }
    }
}
