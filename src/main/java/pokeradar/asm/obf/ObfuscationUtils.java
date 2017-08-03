package pokeradar.asm.obf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ObfuscationUtils
{
    private static boolean runtimeDeobf = true;
    private static final String UNKNOWN = "*";
    private static BufferedReader buffer;

    public static String asmify(String name)
    {
        return name.replace('.', '/');
    }


    public static boolean isRuntimeDeobfuscated()
    {
        return runtimeDeobf;
    }

    public static void setRuntimeDeobfuscated(boolean theDeobf)
    {
        System.out.println("Setting runtime deobfuscation to " + theDeobf);
        runtimeDeobf = theDeobf;
    }
}
