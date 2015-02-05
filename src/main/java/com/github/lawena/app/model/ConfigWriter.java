package com.github.lawena.app.model;

import static com.github.lawena.util.Util.toPath;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lawena.profile.Key;

public abstract class ConfigWriter {

  private static final Logger log = LoggerFactory.getLogger(ConfigWriter.class);

  protected final Settings settings;

  public ConfigWriter(Settings settings) {
    this.settings = settings;
  }

  @SuppressWarnings("nls")
  public void writeAll() throws IOException {
    Files.write(
        Paths.get("lwrt", Key.gameFolderName.getValue(settings), "config/cfg/settings.cfg"),
        writeGameConfig(), Charset.forName("UTF-8"));
    writeSegmentSlotConfigs();
    try {
      writeNamescrollConfig();
    } catch (IOException e) {
      log.warn("Could not detect current movie slot");
    }
  }

  @SuppressWarnings("nls")
  protected static void addNotice(List<String> lines) {
    lines.add("// This file is auto-generated by the tool on launch");
    lines.add("// Don't modify this file, modify static.cfg instead!");
    lines.add("");
  }

  @SuppressWarnings("nls")
  protected void addFramerateLines(List<String> lines) {
    int framerate = Key.framerate.getValue(settings);
    lines.add("alias recframerate host_framerate " + framerate);
    lines.add("host_framerate " + framerate);
    if (framerate < 60) {
      lines.add("alias currentfpsup 60fps");
      lines.add("alias currentfpsdn 3840fps");
    } else if (framerate == 60) {
      lines.add("alias currentfpsup 120fps");
      lines.add("alias currentfpsdn 3840fps");
    } else if (framerate < 120) {
      lines.add("alias currentfpsup 120fps");
      lines.add("alias currentfpsdn 60fps");
    } else if (framerate == 120) {
      lines.add("alias currentfpsup 240fps");
      lines.add("alias currentfpsdn 60fps");
    } else if (framerate < 240) {
      lines.add("alias currentfpsup 240fps");
      lines.add("alias currentfpsdn 120fps");
    } else if (framerate == 240) {
      lines.add("alias currentfpsup 480fps");
      lines.add("alias currentfpsdn 120fps");
    } else if (framerate < 480) {
      lines.add("alias currentfpsup 480fps");
      lines.add("alias currentfpsdn 240fps");
    } else if (framerate == 480) {
      lines.add("alias currentfpsup 960fps");
      lines.add("alias currentfpsdn 240fps");
    } else if (framerate < 960) {
      lines.add("alias currentfpsup 960fps");
      lines.add("alias currentfpsdn 480fps");
    } else if (framerate == 960) {
      lines.add("alias currentfpsup 1920fps");
      lines.add("alias currentfpsdn 480fps");
    } else if (framerate < 1920) {
      lines.add("alias currentfpsup 1920fps");
      lines.add("alias currentfpsdn 960fps");
    } else if (framerate == 1920) {
      lines.add("alias currentfpsup 3840fps");
      lines.add("alias currentfpsdn 960fps");
    } else if (framerate < 3840) {
      lines.add("alias currentfpsup 3840fps");
      lines.add("alias currentfpsdn 1920fps");
    } else if (framerate == 3840) {
      lines.add("alias currentfpsup 60fps");
      lines.add("alias currentfpsdn 1920fps");
    } else {
      lines.add("alias currentfpsup 60fps");
      lines.add("alias currentfpsdn 3840fps");
    }
  }

  protected abstract List<String> writeGameConfig();

  @SuppressWarnings("nls")
  private void writeNamescrollConfig() throws IOException {
    List<String> lines = new ArrayList<>();
    Path data = settings.getParentDataPath();
    lines.add("// This file is auto-generated by the tool on launch");
    lines.add(getFullSegmentNamescroll());
    lines.add(getVideoSegmentNamescroll());
    lines.add(getAudioSegmentNamescroll());
    Files.write(data.resolve("config/cfg/namescroll.cfg"), lines, Charset.forName("UTF-8"));
  }

  @SuppressWarnings("nls")
  private String getFullSegmentNamescroll() throws IOException {
    String lastmovie = "";
    String alias = "alias vnamescroll stmov1";
    Path recPath = toPath(Key.recordingPath.getValue(settings));
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(recPath, "f*.{tga,jpg,jpeg}")) {
      for (Path moviefile : stream) {
        String filename = moviefile.getFileName().toString();
        lastmovie = (lastmovie.compareTo(filename) > 0 ? lastmovie : filename);
      }
    }
    if (!lastmovie.equals("")) {
      // file format is f[slot]_[frame].[extension]
      // slot = ranges from a1, b2, ... to p16
      // frame = values 0 and higher (left padded with zeroes), empty for audio files
      // extension = tga, jpg, jpeg, wav
      int idx = "abcdefghijklmno".indexOf(lastmovie.charAt(1));
      if (idx >= 0) {
        alias = "alias vnamescroll stmov" + (idx + 2);
      } else if (lastmovie.charAt(1) == 'p') {
        alias = "alias vnamescroll noslots";
      }
    }
    return alias;
  }

  @SuppressWarnings("nls")
  private String getVideoSegmentNamescroll() throws IOException {
    String lastmovie = "";
    String alias = "alias anamescroll vorec1";
    Path recPath = toPath(Key.recordingPath.getValue(settings));
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(recPath, "v*.{tga,jpg,jpeg}")) {
      for (Path moviefile : stream) {
        String filename = moviefile.getFileName().toString();
        lastmovie = (lastmovie.compareTo(filename) > 0 ? lastmovie : filename);
      }
    }
    if (!lastmovie.equals("")) {
      int idx = "abcdefghijklmno".indexOf(lastmovie.charAt(1));
      if (idx >= 0) {
        alias = "alias anamescroll vorec" + (idx + 2);
      } else if (lastmovie.charAt(1) == 'p') {
        alias = "alias anamescroll novideoslots";
      }
    }
    return alias;
  }

  @SuppressWarnings("nls")
  private String getAudioSegmentNamescroll() throws IOException {
    String lastmovie = "";
    String alias = "alias namescroll aorec1";
    Path recPath = toPath(Key.recordingPath.getValue(settings));
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(recPath, "a*.wav")) {
      for (Path moviefile : stream) {
        String filename = moviefile.getFileName().toString();
        lastmovie = (lastmovie.compareTo(filename) > 0 ? lastmovie : filename);
      }
    }
    if (!lastmovie.equals("")) {
      int idx = "abcdefghijklmno".indexOf(lastmovie.charAt(1));
      if (idx >= 0) {
        alias = "alias namescroll aorec" + (idx + 2);
      } else if (lastmovie.charAt(1) == 'p') {
        alias = "alias namescroll noaudioslots";
      }
    }
    return alias;
  }

  @SuppressWarnings("nls")
  private void writeSegmentSlotConfigs() throws IOException {
    String[] prefixes =
        {"a1", "b2", "c3", "d4", "e5", "f6", "g7", "h8", "i9", "j10", "k11", "l12", "m13", "n14",
            "o15", "p16"};
    String video = Key.recorderVideoFormat.getValue(settings);
    int quality = Key.recorderJpegQuality.getValue(settings);
    String audio = "wav";
    Path recPath = toPath(Key.recordingPath.getValue(settings));
    Path data = settings.getParentDataPath();
    // video+audio segment
    for (String prefix : prefixes) {
      String line =
          "startmovie \"" + recPath + "/" + prefix + "_\" " + video + " " + audio
              + (video.equals("jpg") ? " jpeg_quality " + quality : "");
      Files.write(data.resolve("config/cfg/mov/f" + prefix + ".cfg"), Arrays.asList(line),
          Charset.forName("UTF-8"));
    }
    // video only segments
    for (String prefix : prefixes) {
      String line =
          "startmovie \"" + recPath + "/" + prefix + "_\" " + video
              + (video.equals("jpg") ? " jpeg_quality " + quality : "");
      Files.write(data.resolve("config/cfg/mov/v" + prefix + ".cfg"), Arrays.asList(line),
          Charset.forName("UTF-8"));
    }
    // audio only segments
    for (String prefix : prefixes) {
      String line = "startmovie \"" + recPath + "/" + prefix + "_\" " + audio;
      Files.write(data.resolve("config/cfg/mov/a" + prefix + ".cfg"), Arrays.asList(line),
          Charset.forName("UTF-8"));
    }
  }
}
