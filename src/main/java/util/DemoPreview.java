package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DemoPreview extends RandomAccessFile {

	private static final Logger log = Logger.getLogger("lawena");
    private static final String n = System.getProperty("line.separator");

	private final static int maxStringLength = 260;

	private int demoProtocol;
	private int networkProtocol;
	private String demoStamp;
	private String playerName;
	private String mapName;
	private String serverName;
	private String gameDirectory;
	private double playbackTime;
	private int tickNumber;

	private DemoPreview(String demoName) throws FileNotFoundException {
		super(demoName, "r");
		try {
			demoStamp = readString(8);
			demoProtocol = readIntBackwards();
			networkProtocol = readIntBackwards();
			serverName = readString(maxStringLength);
			playerName = readString(maxStringLength);
			mapName = readString(maxStringLength);
			gameDirectory = readString(maxStringLength);
			playbackTime = readFloatBackwards();
			tickNumber = readIntBackwards();
		} catch (Exception e) {
			log.log(Level.FINE, "Could not retrieve demo details", e);
		}
	}

	public DemoPreview(Path demopath) throws FileNotFoundException {
		this(demopath.toString());
	}

	private String readString(int length) {
		byte[] aux = new byte[length];
		try {
			read(aux);
			String result = new String(aux, Charset.forName("UTF-8"));
			return result.substring(0, result.indexOf(0));
		} catch (IOException e) {
			log.warning("Error while reading demo info: " + e);
		}
		return null;
	}

	private float readFloatBackwards() {
		byte[] aux = new byte[4];
		try {
			read(aux);
			int value = 0;
			for (int i = 0; i < aux.length; i++) {
				value += (aux[i] & 0xff) << (8 * i);
			}
			return Float.intBitsToFloat(value);
		} catch (IOException e) {
			log.warning("Error while reading demo info: " + e);
		}
		return 0;
	}

	private int readIntBackwards() {
		byte[] aux = new byte[4];
		try {
			read(aux);
			int value = 0;
			for (int i = 0; i < aux.length; i++) {
				value += (aux[i] & 0xff) << (8 * i);
			}
			return value;
		} catch (IOException e) {
			log.warning("Error while reading demo info: " + e);
		}
		return 0;
	}

	private String formatSeconds(double seconds) {
		long s = (long) seconds;
		return String.format("%02d:%02d:%02d", TimeUnit.SECONDS.toHours(s),
				TimeUnit.SECONDS.toMinutes(s) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(s)),
				TimeUnit.SECONDS.toSeconds(s) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(s)));
	}

	@Override
	public String toString() {
        return String.join(n,
            "Stamp: " + demoStamp,
            "DemoProtocol: " + demoProtocol,
            "NetworkProtocol: " + networkProtocol,
            "GameDirectory: " + gameDirectory,
            "PlaybackTime: " + formatSeconds(playbackTime),
            "Server: " + serverName,
            "Player: " + playerName,
            "Map: " + mapName,
            "Ticks: " + tickNumber);
	}

}
