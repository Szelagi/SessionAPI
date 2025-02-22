/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.util;

import org.slf4j.LoggerFactory;
import pl.szelagi.SessionAPI;
import pl.szelagi.component.baseComponent.BaseComponent;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Debug {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Debug.class);
    private static boolean enable = false;
    private static Logger logger;

    public static void send(String message) {
        if (!enable) return;
        logger.info(message);
    }

    public static void send(BaseComponent component, String message) {
        if (!enable) return;
        send(component.name() + ": " + message);
    }

    public static boolean enable() {
        return enable;
    }

    public static void enable(boolean enable) {
        if (enable) {
            logger = Logger.getLogger("SAPIDebug");
            var instance = SessionAPI.instance();
            var dataFolder = instance.getDataFolder();
            try {
                var debugFolder = new java.io.File(dataFolder, "debug");
                if (!debugFolder.exists()) {
                    debugFolder.mkdirs();
                }


                for (var handler : logger.getHandlers()) {
                    logger.removeHandler(handler);
                }

                var dateString = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
                var fileLogName = debugFolder + "/" + dateString + ".log";

                var fileHandler = new FileHandler(fileLogName, true);
                fileHandler.setFormatter(new CustomFormatter());
                logger.addHandler(fileHandler);
            } catch (IOException e) {
                logger.severe("Error while creating the log file: " + e.getMessage());
            }
        }

        Debug.enable = enable;
    }

    private static class CustomFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            // Zwracamy tylko wiadomość logu, bez daty i poziomu logowania
            return record.getMessage() + "\n\n";
        }
    }
}
