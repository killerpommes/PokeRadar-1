package pokeradar.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import com.pixelmonmod.pixelmon.Pixelmon;
import org.apache.commons.io.IOUtils;

import net.minecraftforge.fml.common.Loader;

public class VersionCheck implements Runnable
{
	private static boolean isLatestVersion = true;
	private static boolean isPKVersion = false;
	private static String latestVersion = "";
	private static String updateURL = "";

	/**
	 * @author jabelar
	 * @link
	 *       http://jabelarminecraft.blogspot.nl/p/minecraft-forge-1721710-
	 *       making
	 *       -mod.html
	 */

	@Override
	public void run()
	{
		InputStream in = null;
		try
		{
			URL url = new URL(Reference.VersionURL);
			URLConnection con = url.openConnection();
			con.setRequestProperty("User-Agent", Reference.USER_AGENT);
			con.setUseCaches(false);
			in = con.getInputStream();
		}
		catch (MalformedURLException e)
		{
		}
		catch (IOException e)
		{
		}

		try
		{
			List<String> list = IOUtils.readLines(in);
			int index = -100;
			for (int i = 0; i < list.size(); i++)
			{
				if (list.get(i).contains(Loader.instance().getMCVersionString()))
				{
					if (list.get(i+2).contains("Pixelmon " + Pixelmon.VERSION)) {
						index = i;
						break;
					}
				}
			}

			String version = list.get(index + 1);
			version = version.replace("\"modVersion\": \"", "");
			version = version.replace("\",", "");
			version = version.replace(" ", "");
			latestVersion = version;

			String pkVersion = list.get(index + 2);
			pkVersion = pkVersion.replace("\"pkVersion\": \"", "");
			pkVersion = pkVersion.replace("\",", "");
			pkVersion = pkVersion.replace(" ", "");
			pkVersion = pkVersion.replace("Pixelmon", "");

			String updateURL = list.get(index + 4);
			updateURL = updateURL.replace("\"updateURL\": \"", "");
			updateURL = updateURL.replace("\",", "");
			updateURL = updateURL.replace(" ", "");
			VersionCheck.updateURL = updateURL;

			//System.out.println("PKVersion " + pkVersion);
			//System.out.println("MODVersion " + version);
			isLatestVersion = Reference.VERSION.equals(version);
		}
		catch (IOException e)
		{
		}
		catch (Exception e)
		{
		}

	}

	public static boolean isLatestVersion()
	{
		return isLatestVersion;
	}

	public static String getLatestVersion()
	{
		return latestVersion;
	}

	public static String getUpdateURL()
	{
		return updateURL;
	}
}
