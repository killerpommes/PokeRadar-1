package pokeradar.util;


import pokeradar.forge.Pokeradar;

public class Logging
{
	public static void logInfo(String s, Object... args)
	{
		Pokeradar.logger.info(String.format(s, args));
	}

	public static void logWarning(String s, Object... args)
	{
		Pokeradar.logger.warn(String.format(s, args));
	}

	public static void logError(String s, Object... args)
	{
		Pokeradar.logger.error(String.format(s, args));
	}

	public static void debug(String s, Object... args)
	{
		Pokeradar.logger.debug(String.format(s, args));
	}

	public static void log(String s, Object... args)
	{
		logInfo(String.format(s, args));
	}
}
