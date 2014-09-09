package com.github.lawena.os;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import net.tomahawk.ExtensionsFilter;
import net.tomahawk.XFileDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lawena.util.Util;

public class WindowsInterface extends OSInterface {

  private static final Logger log = LoggerFactory.getLogger(WindowsInterface.class);

  private String hl2 = "hl2.exe";

  @Override
  public ProcessBuilder getBuilderStartTF2() {
    return new ProcessBuilder(getSteamPath() + "/steam.exe");
  }

  @Override
  public ProcessBuilder getBuilderTF2ProcessKiller() {
    return new ProcessBuilder("taskkill", "/F", "/IM", hl2);
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
  public boolean isRunningTF2() {
    String line;
    ProcessBuilder[] builders =
        {
            new ProcessBuilder("tasklist", "/fi", "\"imagename eq " + hl2 + "\"", "/nh", "/fo",
                "csv"),
            new ProcessBuilder("cscript", "//NoLogo", new File("batch\\procchk.vbs").getPath(), hl2)};
    for (ProcessBuilder pb : builders) {
      try {
        Process p = pb.start();
        try (BufferedReader input = Util.newProcessReader(p)) {
          while ((line = input.readLine()) != null) {
            if (line.contains(hl2)) {
              log.trace("TF2 process detected by {}", pb.command().get(0));
              return true;
            }
          }
        }
      } catch (IOException e) {
        log.warn("Problem while finding if TF2 is running", e);
      }
    }
    log.debug("TF2 process not detected");
    return false;
  }

  private void regedit(String key, String value, String content) {
    try {
      File file = new File("batch", "rg.bat");
      ProcessBuilder pb = new ProcessBuilder(file.getAbsolutePath(), key, value, content);
      pb.environment().clear();
      pb.directory(null);
      Process p = pb.start();
      p.waitFor();
    } catch (InterruptedException | IOException e) {
      log.warn("Could not restore registry value: " + e);
    }
  }

  private String regQuery(String key, String value, int mode) {
    StringBuilder result = new StringBuilder();
    try {
      ProcessBuilder pb = new ProcessBuilder("reg", "query", key, "/v", value);
      Process p = pb.start();
      try (BufferedReader input = Util.newProcessReader(p)) {
        String line;
        while ((line = input.readLine()) != null) {
          result.append(line + '\n');
        }
      }
      p.waitFor();
    } catch (InterruptedException | IOException e) {
      log.warn("Could not retrieve dxlevel registry value: " + e);
    }

    try {
      if (mode == 0) {
        return result.substring(result.lastIndexOf("0x") + 2,
            result.indexOf("\n", result.lastIndexOf("0x")));
      }
      return result.substring(result.lastIndexOf(":") - 1,
          result.indexOf("\n", result.lastIndexOf(":")));
    } catch (IndexOutOfBoundsException e) {
      log.debug("Could not find registry value for dxlevel. Using 98 as fallback");
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
      File file = new File("batch", "handle.exe");
      ProcessBuilder pb = new ProcessBuilder(file.getAbsolutePath(), "-c", handle, "-p", pid, "-y");
      pb.environment().clear();
      pb.directory(null);
      Process p = pb.start();
      try (BufferedReader input = Util.newProcessReader(p)) {
        String line;
        int count = 0;
        while ((line = input.readLine()) != null) {
          if (count > 7) {
            log.debug("[handle] " + line);
          }
          count++;
        }
      }
      p.waitFor();
    } catch (InterruptedException | IOException e) {
      log.warn("", e);
    }
  }

  @Override
  public void closeHandles(Path path) {
    try {
      File file = new File("batch", "handle.exe");
      ProcessBuilder pb = new ProcessBuilder(file.getAbsolutePath(), path.toString());
      pb.environment().clear();
      pb.directory(null);
      Process p = pb.start();
      try (BufferedReader input = Util.newProcessReader(p)) {
        String line;
        int count = 0;
        while ((line = input.readLine()) != null) {
          if (count > 4) {
            String[] columns = line.split("[ ]+type: [A-Za-z]+[ ]+|: |[ ]+pid: ");
            if (columns.length == 4) {
              log.debug("[handle] Closing handle {} opened by {}", columns[3], columns[0]);
              closeHandle(columns[1], columns[2]);
            } else {
              log.debug("[handle] " + line);
            }
          }
          count++;
        }
      }
      p.waitFor();
    } catch (InterruptedException | IOException e) {
      log.warn("", e);
    }
  }

  @Override
  public void delete(Path path) {
    try {
      ProcessBuilder pb = new ProcessBuilder("del", "/f", "/s", "/q", "/a", path.toString());
      Process p = pb.start();
      try (BufferedReader input = Util.newProcessReader(p)) {
        String line;
        while ((line = input.readLine()) != null) {
          log.debug("[delete] " + line);
        }
      }
      p.waitFor();
    } catch (InterruptedException | IOException e) {
      log.warn("", e);
    }
  }

  @Override
  public String getVTFCmdLocation() {
    return "vtfcmd/VTFCmd.exe";
  }

  @Override
  public String chooseSingleFolder(Frame parent, String title, String path) {
    if (parent == null) {
      log.warn("Parent frame must not be null to use native dialog");
      return super.chooseSingleFolder(parent, title, path);
    } else {
      XFileDialog dialog = new XFileDialog(parent);
      dialog.setTitle(title);
      dialog.setDirectory(path);
      String folder = dialog.getFolder();
      dialog.dispose();
      return folder;
    }
  }

  @Override
  public String chooseSaveFile(Frame parent, String title, String path,
      List<ExtensionsFilter> filters) {
    if (parent == null) {
      log.warn("Parent frame must not be null to use native dialog");
      return super.chooseSaveFile(parent, title, path, filters);
    } else {
      XFileDialog dialog = new XFileDialog(parent);
      dialog.setTitle(title);
      dialog.addFilters(filters);
      String content = dialog.getSaveFile();
      String folder = dialog.getDirectory();
      dialog.dispose();
      return (content == null ? content : new File(folder, content).toString());
    }
  }

  @Override
  public String chooseSingleFile(Frame parent, String title, String path,
      List<ExtensionsFilter> filters) {
    if (parent == null) {
      log.warn("Parent frame must not be null to use native dialog");
      return super.chooseSingleFile(parent, title, path, filters);
    } else {
      XFileDialog dialog = new XFileDialog(parent);
      dialog.setTitle(title);
      dialog.addFilters(filters);
      String content = dialog.getFile();
      String folder = dialog.getDirectory();
      dialog.dispose();
      return (content == null ? content : new File(folder, content).toString());
    }
  }
}