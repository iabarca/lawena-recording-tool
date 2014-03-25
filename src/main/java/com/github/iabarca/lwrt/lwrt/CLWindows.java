
package com.github.iabarca.lwrt.lwrt;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public abstract class CLWindows extends OSInterface {

    private String hl2 = "hl2.exe";

    @Override
    public ProcessBuilder getBuilderStartGame() {
        return new ProcessBuilder(getSteamPath() + "/steam.exe");
    }

    @Override
    public ProcessBuilder getBuilderGameProcessKiller() {
        return new ProcessBuilder("taskkill", "/F", "/IM", hl2);
    }

    @Override
    public ProcessBuilder getBuilderVTFCmd(String skyboxFilename) {
        return new ProcessBuilder("vtfcmd\\VTFCmd.exe", "-file", "skybox\\" +
                skyboxFilename, "-output", "skybox", "-exportformat", "png");
    }

    @Override
    public Path getSteamPath() {
        return Paths.get(regQuery("HKEY_CURRENT_USER\\Software\\Valve\\Steam", "SteamPath", 1));
    }

    @Override
    public String getSystemDxLevel() {
        return regQuery("HKEY_CURRENT_USER\\Software\\Valve\\Source\\tf\\Settings", "DXLevel_V1", 0);
    }

    @Override
    public boolean isGameRunning() {
        String line;
        ProcessBuilder[] builders = {
                new ProcessBuilder("tasklist", "/fi", "\"imagename eq " + hl2 + "\"",
                        "/nh", "/fo", "csv"),
                new ProcessBuilder("cscript", "//NoLogo",
                        new File("batch\\procchk.vbs").getPath(), hl2)
        };
        for (ProcessBuilder pb : builders) {
            try {
                Process p = pb.start();
                BufferedReader input =
                        new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) {
                    log.finest("[" + pb.command().get(0) + "] " + line);
                    if (line.contains(hl2)) {
                        log.finer("TF2 process detected by " + pb.command().get(0));
                        return true;
                    }
                }
                input.close();
            } catch (IOException e) {
                log.log(Level.INFO, "Problem while finding if TF2 is running", e);
            }
        }
        log.finer("TF2 process not detected");
        return false;
    }

    private void regedit(String key, String value, String content) {
        try {
            ProcessBuilder pb = new ProcessBuilder("batch\\rg.bat", key, value, content);
            Process pr = pb.start();
            pr.waitFor();
        } catch (InterruptedException | IOException e) {
            log.log(Level.INFO, "", e);
        }
    }

    private String regQuery(String key, String value, int mode) {
        String result = "";
        try {
            ProcessBuilder pb = new ProcessBuilder("reg", "query", key, "/v", value);
            Process pr = pb.start();
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result = result + line + '\n';
            }
            pr.waitFor();
        } catch (InterruptedException | IOException e) {
            log.log(Level.INFO, "", e);
        }

        try {
            if (mode == 0) {
                return result.substring(result.lastIndexOf("0x") + 2,
                        result.indexOf('\n', result.lastIndexOf("0x")));
            }
            return result.substring(result.lastIndexOf(":") - 1,
                    result.indexOf('\n', result.lastIndexOf(":")));
        } catch (IndexOutOfBoundsException e) {
            return "98";
        }
    }

    @Override
    public Path resolveVpkToolPath(Path tfpath) {
        return tfpath.resolve("../bin/vpk.exe");
    }

    @Override
    public void setSystemDxLevel(String dxlevel) {
        regedit("HKEY_CURRENT_USER\\Software\\Valve\\Source\\tf\\Settings", "DXLevel_V1", dxlevel);
    }

    @Override
    public void openFolder(Path dir) {
        try {
            Process pr = Runtime.getRuntime().exec("explorer.exe /select," + dir.toString());
            pr.waitFor();
        } catch (IOException | InterruptedException e) {
            // fallback to Java desktop API
            super.openFolder(dir);
        }
    }

    private void closeHandle(String pid, String handle) {
        try {
            ProcessBuilder pb = new ProcessBuilder("batch\\handle.exe", "-c", handle, "-p", pid,
                    "-y");
            Process pr = pb.start();
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            int count = 0;
            while ((line = input.readLine()) != null) {
                if (count > 7) {
                    log.info("[handle] " + line);
                }
                count++;
            }
            pr.waitFor();
        } catch (InterruptedException | IOException e) {
            log.log(Level.INFO, "", e);
        }
    }

    @Override
    public void closeHandles(Path path) {
        try {
            ProcessBuilder pb = new ProcessBuilder("batch\\handle.exe", path.toString());
            Process pr = pb.start();
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            int count = 0;
            while ((line = input.readLine()) != null) {
                if (count > 4) {
                    String[] columns = line.split("[ ]+type: [A-Za-z]+[ ]+|: |[ ]+pid: ");
                    if (columns.length == 4) {
                        log.info("[handle] Closing handle " + columns[3] + " opened by "
                                + columns[0]);
                        closeHandle(columns[1], columns[2]);
                    } else {
                        log.info("[handle] " + line);
                    }
                }
                count++;
            }
            pr.waitFor();
        } catch (InterruptedException | IOException e) {
            log.log(Level.INFO, "", e);
        }
    }

    @Override
    public void delete(Path path) {
        try {
            ProcessBuilder pb = new ProcessBuilder("del", "/f", "/s", "/q", "/a", path.toString());
            Process pr = pb.start();
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                log.info("[delete] " + line);
            }
            pr.waitFor();
        } catch (InterruptedException | IOException e) {
            log.log(Level.INFO, "", e);
        }
    }
}
