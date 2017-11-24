package lwrt;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class CLLinux extends CommandLine {

	private static Set<PosixFilePermission> perms777 = new HashSet<>();

	static {
		perms777.add(PosixFilePermission.OWNER_READ);
		perms777.add(PosixFilePermission.OWNER_WRITE);
		perms777.add(PosixFilePermission.OWNER_EXECUTE);
		perms777.add(PosixFilePermission.GROUP_READ);
		perms777.add(PosixFilePermission.GROUP_WRITE);
		perms777.add(PosixFilePermission.GROUP_EXECUTE);
		perms777.add(PosixFilePermission.OTHERS_READ);
		perms777.add(PosixFilePermission.OTHERS_WRITE);
		perms777.add(PosixFilePermission.OTHERS_EXECUTE);
	}

    private String hl2 = "hl2.sh";


    @Override
    public ProcessBuilder getBuilderStartTF2(String gamePath) {
        Path path = Paths.get(gamePath, "..", "hl2.sh");
        try {
            Path run = Paths.get(this.getSteamPath().toString(),
                "ubuntu12_32", "steam-runtime", "run.sh");
            path = path.toRealPath();
            return new ProcessBuilder(run.toString(), path.toString());
        } catch (IOException e) {
            log.warning("Could not obtain real path of game executable: " + e.toString());
        }
        return new ProcessBuilder(path.toString());

    }

	@Override
	public ProcessBuilder getBuilderStartHLAE(String hlaePath, String gamePath) {
		throw new UnsupportedOperationException("HLAE is not supported here");
	}

	@Override
	public ProcessBuilder getBuilderStartSteam(String steamPath) {
		Path steam = Paths.get(steamPath).resolve("steam.sh");
		try {
			Files.setPosixFilePermissions(steam, perms777);
			return new ProcessBuilder(steam.toString());
		} catch (IOException e) {
			log.log(Level.INFO, "Problem while settings permissions to steam client", e);
		}
		return null;
	}

	@Override
	public ProcessBuilder getBuilderTF2ProcessKiller() {
		return new ProcessBuilder("pkill", "-9", "hl2_linux");
	}

	@Override
	public ProcessBuilder getBuilderHLAEProcessKiller() {
		throw new UnsupportedOperationException("HLAE not supported here");
	}

	@Override
	public ProcessBuilder getBuilderVTFCmd(String skyboxFilename) {
		return null;
	}

	@Override
	public void generatePreview(String skyboxFilename) {
		log.fine("[linux] Skybox preview for " + skyboxFilename + " won't be generated");
	}

	@Override
	public Path getSteamPath() {
		return Paths.get(System.getProperty("user.home"), ".local", "share", "Steam");
	}

	@Override
	public boolean isValidSteamPath(Path p) {
		// value must not be empty
		// value must represent a directory named "Steam" (case insensitive)
		// the directory must have a steam.sh file inside
		String s = p.toString();
		return (!s.isEmpty() && Files.isDirectory(p)
				&& p.getFileName().toString().equalsIgnoreCase("Steam") && Files.exists(p
				.resolve("steam.sh")));
	}

	@Override
	public String getSystemDxLevel() {
		return "90";
	}

	@Override
	public void setSystemDxLevel(String dxlevel) {
		log.fine("[linux] SystemDxLevel won't be set");
	}

	@Override
	public boolean isRunningTF2() {
		boolean found = false;
		try {
			ProcessBuilder pb = new ProcessBuilder("pgrep", "hl2_linux");
			Process pr = pb.start();
			try (BufferedReader input = newProcessReader(pr)) {
				String line;
				line = input.readLine();
				if (line != null) {
					found = true;
				}
			}
		} catch (IOException e) {
			log.log(Level.INFO, "", e);
		}
		return found;
	}

	@Override
	public Path resolveVpkToolPath(Path tfpath) {
		Path path = tfpath.resolve(String.join(File.separator, "..", "bin", "vpk_linux32"));
		try {
			Files.setPosixFilePermissions(path, perms777);
		} catch (IOException e) {
			log.log(Level.FINE, "Could not set file permissions to VPK tool", e);
		}
		return path;
	}

	@Override
	public void setLookAndFeel() {
		// use java default: Nimbus
	}

	@Override
	public void closeHandles(Path path) {
		// no need to implement this yet
	}

	@Override
	public void delete(Path path) {
		// no need to implement this yet
	}

}
