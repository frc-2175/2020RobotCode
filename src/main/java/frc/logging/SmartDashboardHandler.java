package frc.logging;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartDashboardHandler implements LogHandler {
    public void handleLogMessage(LogMessage logMessage) {
        for(LogField field : logMessage.fields) {
            boolean isSmartDashboardField = false;
            for(String tag : field.tags) {
                if(tag.equals(Logger.SMART_DASHBOARD_TAG)) {
                    isSmartDashboardField = true;
                }
            }

            if(isSmartDashboardField) {
                Object value = field.value;
                if(value instanceof Integer || value instanceof Double || value instanceof Float) {
                    SmartDashboard.putNumber(field.name, (double) field.value);
                } else if(value instanceof Boolean) {
                    SmartDashboard.putBoolean(field.name, (boolean) field.value);
                } else {
                    SmartDashboard.putString(field.name, field.value.toString());
                }
            }
        }
    }
}
