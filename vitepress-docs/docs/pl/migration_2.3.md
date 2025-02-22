# Migracja projektu do wersji 2.3

## Wewnętrzne zdarzenia

::: tip v2.3
```java
@Override
public void onComponentInit(ComponentConstructor event) {
    // kiedy komponent zostanie uruchomiony
}
    
@Override
public void onComponentDestroy(ComponentDestructor event) {
    // kiedy komponent zostanie wyłączony
}

@Override
public void onPlayerInit(PlayerConstructor event) {
    // kiedy gracz dołączy do sesji lub komponent zostanie uruchomiony (dla każdego gracza będącego już w sesji)
}

@Override
public void onPlayerDestroy(PlayerDestructor event) {
    // kiedy gracz wyjdzie z sesji lub komponent zostanie wyłączony (dla każdego gracz będącego w sesji)
}

@Override
public void onPlayerJoinRequest(PlayerJoinRequest event) {
    // kiedy gracz próbuje dołączyć do sesji (używane aby zablokować dołączenie np. pełne lobby)
}

@Override
public void onComponentRecovery(ComponentRecovery event) {
    // używane, aby przypisać zdarzenie awaryjne, które wykonają się po restarcie serwera kiedy serwera miał crash
    // służy do usuwania trwałych zasobów np. usunięcie NPC z innego pluginu, który został stworzony na potrzeby sesji
    // jest trwały, więc system recovery zadba, aby go usunąć nawet przy przerwaniu działania serwera
}

@Override
public void onPlayerRecovery(PlayerRecovery event) {
    // używane, aby przypisać zdarzenia awaryjne, które wykonają się kiedy gracz werjdzie na serwer, po crashu serwera
    // służy do naprawiania stanu graca (np. zabranie graczowi itemów z sesji co jest wbudowane w OtherEquipment constoller)
    // aby zachować spójność stanu
}
```
:::

::: danger Old
```java
@Override
public void componentConstructor(ComponentConstructorEvent event) {}

@Override
public void componentDestructor(ComponentDestructorEvent event) {}

@Override
public void playerConstructor(PlayerConstructorEvent event) {}

@Override
public void playerDestructor(PlayerDestructorEvent event) {}

@Override
public void playerCanJoin(PlayerCanJoinEvent event) {}

@Override
public void playerCanQuit(PlayerQuitEvent event) {}

@Override
public void playerDestructorRecovery(PlayerRecoveryEvent event) {}
```
:::

## Bukkit Listener i filtrowanie eventów

::: tip v2.3
Dodano nowoczesne metody, które mają stałą złożoność obliczeniową w przeszukiwaniu komponentów powiązanych z listenerem.
```java
// MyComponent.class
@Override
public Listeners defineListeners() {
    return super.defineListeners().add(MyListener.class);
}

private static class MyListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        var player = event.getPlayer();
        var session = SessionManager.session(player);
        
        // Uwaga! Nigdy nie używaj do wyszukiwania komponentów.
        // Te metody służą tylko filtrowania zdarzeń z bukkit listenerów.
        // I pozwalają znaleźć komponenty danego typu, które posiadają listener danego typu.
        
        // getClass() -- zwraca klasę MyListener.class (podajemy klasę listenera)
        
        // Sposób 1
        ListenerManager.each(session, getClass(), MyComponent.class, myComponent -> {
            // wydarzenia dla każdego komponentu tego typu na sesji
        });
        
        // Sposób 2
        ListenerManager.first(session, getClass(), MyComponent.class, myComponent -> {
            // wydarzenia dla pierwszego komponentu tego typu na sesji
        });
        
        // Sposób 3
        if (session == null) return;
        var myComponents = ListenerManager.components(session, getClass(), MyComponent.class);
        for (var myComponent : myComponents) {
            // wydarzenia dla każdego komponentu tego typu na sesji
        }
        
        // Sposób 4
        if (session == null) return;
        var myComponent = ListenerManager.component(session, getClass(), MyComponent.class);
        if (myComponent == null) return;
        // wydarzenia dla pierwszego komponentu tego typu na sesji
    }
}
```
:::

::: danger Old
```java
// MyController.class
@Override
public @Nullable Listener getListener() {
    return new MyListener();
}

private static class MyListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        var player = event.getPlayer();
        var session = SessionManger.getSession(player);
        if (session == null) return;
        var controllers = ControllerManager.getControllers(session, MyController.class);
        for (var controller : controllers) {
            // wydarzenia dla każdego komponentu tego typu na sesji
        }
    }
}
```
:::

## Wyszukiwanie komponentu w drzewie sesji
::: tip v2.3
```java
// MyComponent.class
var components = ComponentManager.components(session, MyComponent.class);
// komponenty, które są instancją klasy MyComponent.class i są aktywne w drzewie sesji (session)

var component = ComponentManager.firstComponent(session, MyComponent.class);
// pierwszy komponent, który jest instancją klasy MyComponent.class i jest aktywny w drzewie sesji (session)
```
:::

::: danger Old
```java
// MyComponent.class
var controllers = ControllerManager.getControllers(session, MyComponent.class);
// kontrolery, które są instancją klasy MyComponent.class i są aktywne w drzewie sesji (session)

var controller = ControllerManager.getFirstController(session, MyComponent.class);
// pierwszy kontroler, który jest instancją klasy MyComponent.class i jest aktywny w drzewie sesji (session)

```
:::

## Zadania i wątki
::: tip v2.3
Zrezygnowano z metody **getProcess()**. Od teraz kontrolowane zadania uruchamia się bezpośrednio z komponentu.
```java
// MyController.class
@Override
public void onComponentInit(ComponentConstructor event) {
    // Uruchamia zadanie (po upływanie zero czasu), które będzie powtarzać się co sekundę
    runTaskTimer(() -> {
        players().forEach(player -> player.sendMessage("Hello!"));
    }, Time.zero(), Time.seconds(1));
    
    // Dostępne warianty zadań:
    //runTask
    //runTaskAsync
    //runTaskLater
    //runTaskLaterAsync
    //runTaskTimer
    //runTaskTimerAsync
}
```
:::

::: danger Old
```java
@Override
public void componentConstructor(ComponentConstructorEvent event) {
    // Uruchamia zadanie (po upływanie zero czasu), które będzie powtarzać się co sekundę
    getProcess().runControlledTaskTimer(() -> {
        getSession().getPlayers().forEach(player -> player.sendMessage("Hello!"));
    }, Time.Zero(), Time.Seconds(1));
}
```
:::

## Kontenery stanu gracza
::: tip v2.3
Przykład wbudowanego komponentu, który używa **PlayerContainer**, aby zarządzać stanem poszczególnych graczy. W ten sposób może skojarzyć gracza z jego początkowym GameMode.

```java
public class GameModeState extends PlayerState {
	private final GameMode gameMode;

	public GameModeState(Player player) {
		super(player);
		gameMode = player.getGameMode();
	}

	public GameMode getGameMode() {
		return gameMode;
	}
}

public class OtherGameMode extends Controller {
    private PlayerContainer<GameModeState> states;
    private final GameMode gameMode;

    public OtherGameMode(BaseComponent baseComponent, GameMode gameMode) {
        super(baseComponent);
        this.gameMode = gameMode;
    }

    @Override
    public void onComponentInit(ComponentConstructor event) {
        super.onComponentInit(event);
        states = new PlayerContainer<>(GameModeState::new);
    }

    @Override
    public void onPlayerInit(PlayerConstructor event) {
        super.onPlayerInit(event);
        states.createOrThrow(event.getPlayer());
        event.getPlayer().setGameMode(gameMode);
    }

    @Override
    public void onPlayerDestroy(PlayerDestructor event) {
        super.onPlayerDestroy(event);
        var player = event.getPlayer();
        var state = states.removeOrThrow(player);
        player.setGameMode(state.getGameMode());
    }

    @Override
    public void onPlayerRecovery(PlayerRecovery event) {
        super.onPlayerRecovery(event);
        var state = states.getOrThrow(event.owner());
        final var gameMode = state.getGameMode();
        event.register(this, player -> {
            player.setGameMode(gameMode);
        });
    }
}
```
:::

::: danger Old
```java
public class GameModeState extends PlayerState {
	private final GameMode gameMode;

	public GameModeState(Player player) {
		super(player);
		gameMode = player.getGameMode();
	}

	public GameMode getGameMode() {
		return gameMode;
	}
}

public class OtherGameMode extends Controller {
    private PlayerContainer<GameModeState> states;
    private final GameMode gameMode;

    public OtherGameMode(ISessionComponent sessionComponent, GameMode gameMode) {
        super(sessionComponent);
        this.gameMode = gameMode;
    }

    @Override
    public void componentConstructor(ComponentConstructorEvent event) {
        super.componentConstructor(event);
        states = new PlayerContainer<>(GameModeState::new);
    }

    @Override
    public void playerConstructor(PlayerConstructorEvent event) {
        super.playerConstructor(event);
        states.get(event.getPlayer());
        event.getPlayer().setGameMode(gameMode);
    }

    @Override
    public void playerDestructor(PlayerDestructorEvent event) {
        super.playerDestructor(event);
        var player = event.getPlayer();
        var state = states.get(player);
        player.setGameMode(state.getGameMode());
        states.clearState(player);
    }

    @Override
    public void playerDestructorRecovery(PlayerRecoveryEvent event) {
        super.playerDestructorRecovery(event);
        var state = states.get(event.getForPlayer());
        final var gameMode = state.getGameMode();
        event.getLambdas().add(player -> {
            player.setGameMode(gameMode);
        });
    }
}
```
:::

## System plików

::: tip v2.3
Odpowiada na jaki folder, będzie wskazywać metoda **fileManager()**.
Z tego katalogu, będą ładowanie schematy tagi i inne zasoby.
```java
// MyBoard.class

@Override
public String defineDirectoryPath() {
    // Sposób 1 (statyczna nazwa)
    // Wskazuje na plugins/SessionAPI/board/myDirectory
    // Każdy komponent tej instancji będzie korzystał z tego katalogu
    // Dlatego lepiej używać dynamicznej nazwy z użyciem name()
    return "board/myDirectory";
            
    // Sposób 2 (dynamiczna nazwa)
    // name() zwraca: <className><C for controller | B for board | S for session>#<pluginName>
    return "board/" + name();
}
```
:::

::: danger Old
Można było definiować system plików mapy, przez przeciążenie metody **getName()**.
:::

## Ładowanie schematów

::: tip v2.3
Domyślna metoda **generate()**, ładuje mapę ze schematu w plikach mapy.
```java
@Override
protected void generate() {
    // ...
    // Ładowanie schematu z katalogu "SessionAPI/<defineDirectoryPath()>"
    var fileManger = fileManager();
    if (fileManger.existSchematic(CONSTRUCTOR_FILE_NAME)) {
        fileManger.loadSchematic(CONSTRUCTOR_FILE_NAME, space(), center());
    }
    // ...
}
```
:::

::: danger Old
```java
// MyBoard.class
@Override
protected void generate() {
    // ...
    // Ładowanie schematu z katalogu "SessionAPI/board/<getName()>"
    if (boardFileManager.existsSchematic(SCHEMATIC_CONSTRUCTOR_NAME)) {
        boardFileManager.loadSchematic(SCHEMATIC_CONSTRUCTOR_NAME);
    }
    // ...
}
```
:::

## Ładowanie tagów
::: tip v2.3
Domyślna metoda **defineTags()**, ładuje tagi **Board**.

Przykładowe operacje z ładowaniem tagów:
```java
// Używane w komponencie
// Korzysta z katalogu "SessionAPI/<defineDirectoryPath()>"
var fileManger = fileManager();
// OR

// Własny folder
// Korzysta z katalogu "SessionAPI/board/MyBoard"
var fileManager = new FileManager("board/MyBoard");

TagResolve tagResolve = null;
// Odczyt tagów
if (fileManger.existTag(TAG_FILE_NAME)) {
    tagResolve =  fileManger.loadTag(TAG_FILE_NAME, center());
}

// Zapis tagów
if (tagResolve != null) {
    fileManager.saveTag(TAG_FILE_NAME, tagResolve);
}
```
::: danger Old
```java
var boardFileManager = new BoardFileManager(getName(), getSpace());

TagResolve tagResolve = null;
if (boardFileManager.existsSignTagData(SIGN_TAG_DATA_NAME)) {
    tagResolve = boardFileManager.loadSignTagData(SIGN_TAG_DATA_NAME);
}

if (tagResolve != null) {
    boardFileManager.saveSignTagData(SIGN_TAG_DATA_NAME, tagResolve);
}
```
:::

## Bezpieczna strefa mapy

::: tip v2.3
Definiuje przestrzeń, która jest niszczona przez **degenerate()**. Tylko w bezpiecznej strefie można stawiać bloki i wykonywać operacje na blokach.
```java
// MyBoard.class
@Override
public ISpatial defineSecureZone() {
    // Sposób 1 (domyślny) -- na podstawie rozmiarów destruktora
    return fileManager().loadSchematicToSpatial(DESTRUCTOR_FILE_NAME, center());
    
    // Sposób 2 -- na całej przypisanej mapie sesji.
    // Należy upewnić się że degenerate() obejmuje całą przypisaną przestrzeń.
    return ISpatial.clone(space());
}
```
:::

::: danger Old
```java
// MyBoard.class
@Override
protected void generate() {
    // ... (pominięto fragment generujący mapę)
    
    // Ustawianie SecureZone
    if (boardFileManager.existsSchematic(SCHEMATIC_DESTRUCTOR_NAME)) {
        secureZone = boardFileManager.toSpatial(SCHEMATIC_DESTRUCTOR_NAME, getBase());
    }

}
```
:::