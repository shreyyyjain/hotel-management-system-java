package com.shrey.hotel.cli;

import com.shrey.hotel.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.SwingUtilities;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Starting Hotel Management App");
        SwingUtilities.invokeLater(() -> {
            try {
                App app = new App();
                app.start();
            } catch (Exception ex) {
                log.error("Fatal error while starting app", ex);
                System.exit(1);
            }
        });
    }
}
