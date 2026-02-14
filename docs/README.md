# Peggy User Guide

Peggy is a simple task-tracking chatbot with a JavaFX GUI. You can add tasks, list them, mark/unmark, delete, search, and set priorities. Your tasks are saved automatically to disk.

---

## Setting up in IntelliJ

Prerequisites: **JDK 17**, IntelliJ IDEA (latest), and an internet connection (for Gradle to download dependencies).

1. Open IntelliJ (if you are not in the welcome screen, click `File` > `Close Project` first).
1. Open the project:
    1. Click `Open`.
    1. Select the project directory, and click `OK`.
    1. If prompted, accept defaults.
1. Configure IntelliJ to use **JDK 17**:
    - `File` > `Project Structure` > `Project`
    - Set **Project SDK** to **JDK 17**
    - Set **Project language level** to `SDK default`
1. Let Gradle finish syncing.
1. Run the app:
    - Open the Gradle tool window (usually on the right)
    - Run `Tasks > application > run`
    - Or use the terminal command shown below.

**Warning:** Keep Java files under `src/main/java` (don’t rename/move the folder), because Gradle expects that default structure.

---

## Running Peggy

### Option A: Run the GUI (recommended)

From the project root:

```bash
./gradlew run
```

### Option B: Build and run the JAR

1. Build the shaded JAR:

```bash
./gradlew shadowJar
```

2. Run it:

```bash
java -jar build/libs/peggy.jar
```

Optional: enable Java assertions while running (useful if you added `assert` statements):

```bash
java -ea -jar build/libs/peggy.jar
```

---

## Where your data is saved

Peggy stores your tasks in:

- `data/peggy.txt`

Tasks are saved automatically after you add/mark/unmark/delete, so you normally don’t need to save manually.

---

## Using the GUI

- Type your command in the text box at the bottom.
- Click **Send** (or press Enter if enabled).
- **Bot messages** appear on the **left**, **User messages** on the **right**.
- Each message appears in a “chat bubble”.
- Your tasks persist between runs.

---

## Features & Commands

### 1) List tasks

Shows all tasks.

```
list
```

### 2) Add a todo

Adds a simple task.

```
todo <description>
```

Example:
```
todo read book
```

### 3) Add a deadline

Adds a task with a due date/time.

```
deadline <description> /by <time>
```

Examples:
```
deadline submit report /by 2026-02-20 1800
deadline return book /by 2/12/2019 18:00
```

### 4) Add an event

Adds a task with start and end date/time.

```
event <description> /from <time> /to <time>
```

Example:
```
event project meeting /from 2026-02-20 1400 /to 2026-02-20 1600
```

### 5) Mark a task done

```
mark <task number>
```

Example:
```
mark 2
```

### 6) Unmark a task (not done)

```
unmark <task number>
```

Example:
```
unmark 2
```

### 7) Delete a task

```
delete <task number>
```

Example:
```
delete 3
```

### 8) Find tasks by keyword

Searches task descriptions (case-insensitive).

```
find <keyword>
```

Example:
```
find report
```

### 9) Set priority (if implemented in your build)

If your Priority feature is implemented, you can set a task’s priority.  
(If not implemented yet, remove this section or update it to match your command format.)

Example formats you might use:
```
priority <task number> <priority>
```

Examples:
```
priority 2 high
priority 4 1
```

### 10) Help

Shows the command list.

```
help
```

### 11) Exit

Closes the app (GUI will close shortly after showing the bye message).

```
bye
```

---

## Supported date/time formats

Peggy accepts several common formats for `/by`, `/from`, `/to` (based on your Parser):

- `yyyy-MM-dd` (date only)
- `yyyy-MM-dd HHmm` (e.g., `2026-02-20 1800`)
- `yyyy-MM-dd HH:mm` (e.g., `2026-02-20 18:00`)
- `d/M/yyyy HHmm` (e.g., `2/12/2019 1800`)
- `d/M/yyyy HH:mm` (e.g., `2/12/2019 18:00`)
- Storage format may also include ISO strings like `2019-12-02T18:00`

---

## Troubleshooting

### “Could not find method javafx() …” (Gradle)
This usually means the JavaFX Gradle plugin isn’t applied. Ensure your `build.gradle` matches the required JavaFX setup for the course template (or remove the `javafx { ... }` block if you are not using that plugin and rely on dependencies instead).

### “Root value already specified” (FXML)
This happens if:
- your `.fxml` file already has a root, but you also call `loader.setRoot(this)` in code.

For `fx:root` FXML, your file should start with `<fx:root ...>` and you should keep `setRoot(this)` in the `DialogBox` constructor.

### “HBox is not a valid type” (FXML)
This usually means the import is missing in FXML. Ensure your FXML includes:

```xml
<?import javafx.scene.layout.HBox?>
```

Or use `fx:root type="javafx.scene.layout.HBox"` correctly.

---
