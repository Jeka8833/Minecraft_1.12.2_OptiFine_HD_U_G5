import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.main.Main;

import java.net.Proxy;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Start
{

    /**
     * To start the license, the -email and -pass option are required. Example: -license true -email yourEmail -pass yourPass
     * The -name option is required to start the pirate. Example: -license false -name User228
     */
    public static void main(String[] args) throws AuthenticationException {

        final Path path = Paths.get(System.getenv("AppData"), ".minecraft");
        if (getValue("-license", args).equalsIgnoreCase("true")) {
            final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) (new YggdrasilAuthenticationService(Proxy.NO_PROXY, "")).createUserAuthentication(Agent.MINECRAFT);
            auth.setUsername(getValue("-email", args));
            auth.setPassword(getValue("-pass", args));
            auth.logIn();
            Main.main(new String[]{"--username", auth.getSelectedProfile().getName(), "--version", "mcp", "--gameDir", path.toString(), "--assetsDir", path.resolve("assets").toString(), "--assetIndex", "1.12", "--uuid", auth.getSelectedProfile().getId().toString(), "--accessToken", auth.getAuthenticatedToken(), "--userType", "mojang"});
        } else {
            Main.main(new String[]{"--version", "mcp", "--accessToken", "0", "--gameDir", path.toString(), "--assetsDir", path.resolve("assets").toString(), "--assetIndex", "1.12", "--userProperties", "{}", "--username", getValue("-name", args)});
        }
    }

    private static String getValue(final String key, final String[] array) {
        for (int i = 0; i < array.length - 1; i++)
            if (array[i].equalsIgnoreCase(key))
                return array[i + 1];
        throw new NullPointerException("Key(" + key + ") not found");
    }
}
