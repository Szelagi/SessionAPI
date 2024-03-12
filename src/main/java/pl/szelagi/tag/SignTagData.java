package pl.szelagi.tag;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import pl.szelagi.tag.exception.NoTagException;
import pl.szelagi.tag.serialization.SignTagDataStorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class SignTagData implements Serializable {
    private final HashMap<String, ArrayList<SignTag>> map;
    public SignTagData() {
        map = new HashMap<>();
    }

    public void add(SignTag element) {
        ArrayList<SignTag> sameTypeElements;
        if (!map.containsKey(element.getTag())) {
            sameTypeElements = new ArrayList<>();
            map.put(element.getTag(), sameTypeElements);
        } else {
            sameTypeElements = map.get(element.getTag());
        }
        sameTypeElements.add(element);
    }
    @NotNull
    public SignTagQuery get(String tag) throws NoTagException {
        ArrayList<SignTag> data = map.get(tag);
        if (data == null || data.isEmpty()) throw new NoTagException(tag);
        return new SignTagQuery(tag, data);
    }
    public boolean isExists(String tag) {
        return map.containsKey(tag);
    }


    public ArrayList<SignTag> toProcessedElements() {
        var processedElements = new ArrayList<SignTag>();
        for (var list : map.values()) {
            processedElements.addAll(list);
        }
        return processedElements;
    }
    public ArrayList<Location> toLocations() {
        var locations = new ArrayList<Location>();
        for (var list : map.values()) {
            for (var element : list) {
                locations.add(element.getLocation());
            }
        }
        return locations;
    }

    public SignTagDataStorage toSignTagDataStorage() {
        var storage = new SignTagDataStorage();
        for (var array : map.values()) {
            if (array == null) continue;
            for (var signTag : array) {
                storage.add(signTag);
            }
        }
        return storage;
    }
}
