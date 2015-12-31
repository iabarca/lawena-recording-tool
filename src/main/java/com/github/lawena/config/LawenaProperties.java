package com.github.lawena.config;

import com.github.lawena.domain.AppProfile;
import com.github.lawena.domain.Launcher;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ConfigurationProperties(prefix = "lawena", ignoreUnknownFields = false)
public class LawenaProperties {

    /**
     * Path containing a JSON representation of the profile data
     */
    private String profilesPath = "settings.json";
    /**
     * Location for the Steam folder, the one that contains the Steam executable
     */
    private String steamPath = "";
    /**
     * Number of seconds to wait for a game to launch
     */
    private int launchTimeout = 120;
    /**
     * The selected profile name
     */
    private String selected = null;
    /**
     * The profiles
     */
    private List<AppProfile> profiles = new ArrayList<>();
    /**
     * The launchers
     */
    private List<Launcher> launchers = new ArrayList<>();

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public List<AppProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<AppProfile> profiles) {
        this.profiles = profiles;
    }

    public List<Launcher> getLaunchers() {
        return launchers;
    }

    public void setLaunchers(List<Launcher> launchers) {
        this.launchers = launchers;
    }

    public String getProfilesPath() {
        return profilesPath;
    }

    public void setProfilesPath(String profilesPath) {
        this.profilesPath = profilesPath;
    }

    public String getSteamPath() {
        return steamPath;
    }

    public void setSteamPath(String steamPath) {
        this.steamPath = steamPath;
    }

    public int getLaunchTimeout() {
        return launchTimeout;
    }

    public void setLaunchTimeout(int launchTimeout) {
        this.launchTimeout = launchTimeout;
    }

    @Override
    public String toString() {
        return toFullString();
    }

    public String toFullString() {
        return "LawenaProperties [" +
                "profilesPath='" + profilesPath + '\'' +
                ", steamPath='" + steamPath + '\'' +
                ", launchTimeout='" + launchTimeout + '\'' +
                ", selected='" + selected + '\'' +
                ", profiles=[" + profiles.stream().map(AppProfile::toFullString).collect(Collectors.joining(", ")) +
                "], launchers=[" + launchers.stream().map(Launcher::toFullString).collect(Collectors.joining(", ")) +
                "]]";
    }

    public String toPrettyString() {
        return "\n---------------------- Lawena Recording Tool -----------------------" +
                "\nOperating system          : " + System.getProperty("os.name") + " " + System.getProperty("os.arch") +
                "\nJava version              : " + System.getProperty("java.version") +
                "\nJava home                 : " + System.getProperty("java.home") +
                "\nCurrent locale            : " + System.getProperty("user.language") + "_" + System.getProperty("user.country") +
                "\n----------------------------- Settings -----------------------------" +
                "\nProfile config location   : " + profilesPath +
                "\nSteam directory           : " + formatNullOrEmpty(steamPath) +
                "\n----------------------------- Profiles -----------------------------" +
                "\nSelected                  : " + selected + '\n' +
                profiles.stream().map(AppProfile::toFullString).collect(Collectors.joining("\n")) +
                "\n----------------------------- Launchers ----------------------------\n" +
                launchers.stream().map(Launcher::toFullString).collect(Collectors.joining("\n")) +
                "\n--------------------------------------------------------------------";
    }

    private String formatNullOrEmpty(Object o) {
        if (o == null || o.toString().isEmpty()) {
            return "<no value set>";
        }
        return o.toString();
    }
}