package pl.szelagi.tag.serialization;

import org.bukkit.Location;
import pl.szelagi.tag.SignTag;
import pl.szelagi.tag.SignTagData;

import java.io.Serializable;
import java.util.ArrayList;

public class SignTagDataStorage implements Serializable {
    private ArrayList<SignTagStorage> signTagStorages = new ArrayList<>();

    public void add(SignTag signTag) {
        var signTagStorage = new SignTagStorage(
                signTag.getTag(),
                signTag.getRelativeLocation().toRelativePoint(),
                signTag.getArgs()
        );
        signTagStorages.add(signTagStorage);
    }
    public SignTagData toSignTagData(Location base) {
        var signTagData = new SignTagData();
        for (var s : signTagStorages) {
            var signTag = new SignTag(s.getTag(), s.getRelativePoint().toRelativeLocation(base), s.getArgs());
            signTagData.add(signTag);
        }
        return signTagData;
    }
}
