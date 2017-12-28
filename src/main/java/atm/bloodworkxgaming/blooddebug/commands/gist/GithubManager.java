package atm.bloodworkxgaming.blooddebug.commands.gist;


import atm.bloodworkxgaming.blooddebug.BloodDebug;
import atm.bloodworkxgaming.blooddebug.ModConfig;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.service.GistService;

import java.io.IOException;
import java.util.Collections;

public class GithubManager {
    private static GistService gistService = null;

    public static void login() {
        if (!ModConfig.githubOAuthKey.isEmpty()) {
            gistService = new GistService();
            gistService.getClient().setOAuth2Token(ModConfig.githubOAuthKey);
        } else {
            BloodDebug.warn("No OAuth key provided for Github. Check here how to get one: https://help.github.com/articles/creating-a-personal-access-token-for-the-command-line/");
        }
    }

    public static Gist postGist(String desc, String filename, String content) {
        return postGist(new Gist(), desc, filename, content, false);
    }

    public static Gist updateGist(Gist gistToUpdate, String desc, String filename, String content) {
        if (gistToUpdate == null) {
            return postGist(desc, filename, content);
        } else {
            return postGist(gistToUpdate, desc, filename, content, true);
        }
    }

    private static Gist postGist(Gist gist, String desc, String filename, String content, boolean update) {
        if (gistService == null) {
            BloodDebug.error("Couldn't create gist, gistService is null. Maybe not logged in?");
            return null;
        }

        BloodDebug.info("Current remaining Requests are: " + gistService.getClient().getRemainingRequests());

        GistFile file = new GistFile();
        file.setContent(content);
        file.setFilename(filename);
        gist.setDescription(desc);
        gist.setFiles(Collections.singletonMap(filename, file));
        gist.setPublic(!ModConfig.secretGist);

        try {
            if (update) {
                return gistService.updateGist(gist);
            } else {
                return gistService.createGist(gist);
            }
        } catch (IOException e) {
            BloodDebug.error("Error while trying to create gist: " + gist.toString(), e);
            return null;
        }
    }
}
