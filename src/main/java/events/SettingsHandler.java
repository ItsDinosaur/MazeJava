/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package events;

/**
 *
 * @author maciek
 */
public interface SettingsHandler {
    public void settingsSelected(int settingID, boolean value);
    public void settingsSetValue(int settingID, int value);
}
