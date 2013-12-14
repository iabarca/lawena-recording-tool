
package lwrt;

import util.CopyDirVisitor;
import util.DeleteDirVisitor;
import util.LawenaException;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import lwrt.SettingsManager.Key;

public class FileManager {

    private static final Logger log = Logger.getLogger("lawena");

    private CustomPathList customPathList;
    private SettingsManager cfg;
    private CommandLine cl;

    public FileManager(SettingsManager cfg, CommandLine cl) {
        this.cfg = cfg;
        this.cl = cl;
    }

    private Path copy(Path from, Path to) throws IOException {
        return Files.walkFileTree(from, new CopyDirVisitor(from, to));
    }

    private Path copy(Path from, Path to, Filter<Path> filter) throws IOException {
        return Files.walkFileTree(from, new CopyDirVisitor(from, to, filter));
    }

    private Path delete(Path dir) throws IOException {
        return Files.walkFileTree(dir, new DeleteDirVisitor(cl));
    }

    private boolean isEmpty(Path dir) {
        if (Files.exists(dir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                return !stream.iterator().hasNext();
            } catch (IOException e) {
                return false;
            }
        } else {
            return true;
        }
    }

    public CustomPathList getCustomPathList() {
        return customPathList;
    }

    public void setCustomPathList(CustomPathList customPathList) {
        this.customPathList = customPathList;
    }

    public void replaceAll() throws LawenaException {
        Path tfpath = cfg.getTfPath();
        Path customBackupPath = tfpath.resolve("lwrtcustom");
        Path customPath = tfpath.resolve("custom");
        Path configBackupPath = tfpath.resolve("lwrtcfg");
        Path configPath = tfpath.resolve("cfg");

        if (!Files.exists(configBackupPath)) {
            try {
                log.fine("Making a backup of your config files");
                configPath.toFile().setWritable(true);
                Files.move(configPath, configBackupPath);
                Files.createDirectories(configPath);
                copy(Paths.get("cfg"), configPath);
            } catch (IOException e) {
                log.log(Level.WARNING, "Could not replace cfg files", e);
                throw new LawenaException("Failed to replace cfg files", e);
            }
        }
        if (!Files.exists(customBackupPath)) {
            try {
                log.fine("Making a backup of your custom files");
                if (!Files.exists(customPath) || !Files.isDirectory(customPath)) {
                    Files.createDirectory(customPath);
                }
                customPath.toFile().setWritable(true);
                Files.move(customPath, customBackupPath);
            } catch (IOException e) {
                log.log(Level.WARNING, "Could not backup custom folder", e);
                throw new LawenaException("Failed to replace custom files", e);
            }
            try {
                log.fine("Copying selected hud files");
                Path resourcePath = tfpath.resolve("custom/lawena/resource");
                Path scriptsPath = tfpath.resolve("custom/lawena/scripts");
                Files.createDirectories(resourcePath);
                Files.createDirectories(scriptsPath);
                String hudName = cfg.getHud();
                if (!hudName.equals("custom")) {
                    copy(Paths.get("hud", hudName, "resource"), resourcePath);
                    copy(Paths.get("hud", hudName, "scripts"), scriptsPath);
                }
            } catch (IOException e) {
                log.log(Level.INFO, "Could not replace hud files", e);
                throw new LawenaException("Failed to replace hud files", e);
            }
            Path skyboxPath = tfpath.resolve("custom/lawena/materials/skybox");
            try {
                String sky = cfg.getSkybox();
                if (sky != null && !sky.isEmpty() && !sky.equals(Key.Skybox.defValue())) {
                    log.fine("Copying selected skybox files");
                    Files.createDirectories(skyboxPath);
                    replaceSkybox();
                }
            } catch (IOException e) {
                log.log(Level.WARNING, "Could not replace skybox files", e);
                try {
                    delete(skyboxPath);
                    log.fine("Skybox folder deleted, no skybox files were replaced");
                } catch (IOException e1) {
                    log.log(Level.WARNING, "Could not delete lawena skybox folder", e1);
                }
                throw new LawenaException("Failed to replace skybox files", e);
            }
            // Copy selected custom files
            if (customPathList != null) {
                copyCustomFiles();
            }
        }
    }

    private void copyCustomFiles() {
        Path tfpath = cfg.getTfPath();
        Path customBackupPath = tfpath.resolve("lwrtcustom");
        Path customPath = tfpath.resolve("custom");
        Path localCustomPath = Paths.get("custom");
        Path customParticlesPath = customPath.resolve("lawena/particles");

        log.fine("Copying selected custom vpks and folders");
        for (CustomPath cp : customPathList.getList()) {
            try {
                if (cp.isSelected()) {
                    Path source;
                    if (cp.getPath().startsWith(customPath)) {
                        source = customBackupPath.resolve(cp.getPath().getFileName());
                    } else if (cp.getPath().startsWith(localCustomPath)) {
                        source = localCustomPath.resolve(cp.getPath().getFileName());
                    } else {
                        log.info("Skipping custom file with wrong path: " + cp.getPath());
                        continue;
                    }
                    if (Files.exists(source)) {
                        if (Files.isDirectory(source)) {
                            log.fine("Copying custom folder: " + source.getFileName());
                            Path dest = customPath.resolve(source.getFileName());
                            copy(source, dest);
                        } else if (cp == CustomPathList.particles) {
                            List<String> contents = cl.getVpkContents(tfpath, cp.getPath());
                            List<String> selected = cfg.getParticles();
                            if (!selected.contains("*")) {
                                contents.retainAll(selected);
                            }
                            if (!contents.isEmpty()) {
                                log.fine("Copying enhanced particles: " + contents);
                                Files.createDirectories(customParticlesPath);
                                cl.extractIfNeeded(tfpath, cp.getPath().toString(),
                                        customParticlesPath.getParent(), contents);
                            } else {
                                log.fine("No enhanced particles were selected");
                            }
                        } else if (source.getFileName().toString().endsWith(".vpk")) {
                            log.fine("Copying custom VPK: " + cp.getPath());
                            Path dest = customPath.resolve(source.getFileName());
                            Files.copy(source, dest);
                        } else {
                            log.info("Not copying: " + source.getFileName());
                        }
                    } else {
                        log.info("Does not exist: " + source);
                    }
                }
            } catch (IOException e) {
                log.log(Level.INFO, "Could not copy custom file: " + cp.getPath(), e);
            }
        }
    }

    private void replaceSkybox() throws IOException {
        Path tfpath = cfg.getTfPath();
        Set<Path> vmtPaths = new LinkedHashSet<>();
        Set<Path> vtfPaths = new LinkedHashSet<>();
        Path skyboxPath = tfpath.resolve("custom/lawena/materials/skybox");
        String skyboxVpk = "custom/skybox.vpk";
        String skyboxFilename = cfg.getSkybox();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("skybox"))) {
            for (Path path : stream) {
                String pathStr = path.toFile().getName();
                if (pathStr.endsWith(".vmt")) {
                    Files.copy(path, skyboxPath.resolve(pathStr));
                    vmtPaths.add(path);
                }
                if (pathStr.endsWith(".vtf") && pathStr.startsWith(skyboxFilename)) {
                    vtfPaths.add(path);
                }
            }
        }

        for (String pathStr : cl.getVpkContents(tfpath, Paths.get(skyboxVpk))) {
            Path path = Paths.get("skybox", pathStr);
            if (pathStr.endsWith(".vmt")) {
                cl.extractIfNeeded(tfpath, skyboxVpk, Paths.get("skybox"), Arrays.asList(pathStr));
                Files.copy(path, skyboxPath.resolve(pathStr), StandardCopyOption.REPLACE_EXISTING);
                vmtPaths.add(path);
            }
            if (pathStr.endsWith(".vtf") && pathStr.startsWith(skyboxFilename)) {
                vtfPaths.add(path);
            }
        }

        for (Path vtfPath : vtfPaths) {
            for (Path vmtPath : vmtPaths) {
                String vtf = vtfPath.getFileName().toString();
                String vmt = vmtPath.getFileName().toString();
                if ((vtf.endsWith("up.vtf") && vmt.endsWith("up.vmt"))
                        || (vtf.endsWith("dn.vtf") && vmt.endsWith("dn.vmt"))
                        || (vtf.endsWith("bk.vtf") && vmt.endsWith("bk.vmt"))
                        || (vtf.endsWith("ft.vtf") && vmt.endsWith("ft.vmt"))
                        || (vtf.endsWith("lf.vtf") && vmt.endsWith("lf.vmt"))
                        || (vtf.endsWith("rt.vtf") && vmt.endsWith("rt.vmt"))) {
                    cl.extractIfNeeded(tfpath, skyboxVpk, Paths.get("skybox"),
                            Arrays.asList(vtfPath.getFileName().toString()));
                    Files.copy(vtfPath,
                            skyboxPath.resolve(vmt.substring(0, vmt.indexOf(".vmt")) + ".vtf"));
                }
            }
        }
    }

    public boolean restoreAll() {
        Path tfpath = cfg.getTfPath();
        Path customBackupPath = tfpath.resolve("lwrtcustom");
        Path customPath = tfpath.resolve("custom");
        Path configBackupPath = tfpath.resolve("lwrtcfg");
        Path configPath = tfpath.resolve("cfg");
        boolean restoreComplete = true;

        if (Files.exists(configBackupPath)) {
            log.fine("Restoring all your config files");
            try {
                delete(configPath);
            } catch (NoSuchFileException e) {
            } catch (IOException e) {
                log.log(Level.INFO, "Could not delete lawena cfg folder", e);
            }
            try {
                if (isEmpty(configPath)) {
                    Files.move(configBackupPath, configPath, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    restoreComplete = false;
                }
            } catch (IOException e) {
                log.log(Level.INFO, "Could not restore cfg files", e);
                restoreComplete = false;
            }
        }
        if (Files.exists(customBackupPath)) {
            log.fine("Restoring all your custom files");
            try {
                delete(customPath);
            } catch (NoSuchFileException e) {
            } catch (IOException e) {
                log.log(Level.INFO, "Could not delete lawena custom files", e);
            }
            try {
                if (isEmpty(customPath)) {
                    Files.move(customBackupPath, customPath, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    restoreComplete = false;
                }
            } catch (IOException e) {
                log.log(Level.INFO, "Could not restore custom files", e);
                restoreComplete = false;
            }
        }
        if (!restoreComplete) {
            log.info("Some lawena files might still exist inside 'cfg' or 'custom'");
            log.info("Your files will be restored once you close lawena or run TF2");
        }
        return restoreComplete;
    }

    public boolean copyToCustom(final Path path) {
        Path localCustomPath = Paths.get("custom");
        try {
            copy(path, localCustomPath.resolve(path.getFileName()), new Filter<Path>() {

                @Override
                public boolean accept(Path entry) throws IOException {
                    if (entry.getParent().equals(path)) {
                        String f = entry.getFileName().toString();
                        return f.matches("^(resource|scripts|cfg|materials|addons|sound|particles)$");
                    } else {
                        return true;
                    }
                }
            });
            return true;
        } catch (IOException e) {
            log.log(Level.FINE, "Failed to copy folder to lawena's custom files", e);
        }
        return false;
    }
}
