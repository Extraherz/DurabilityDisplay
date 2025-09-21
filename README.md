# Durability Display

Ein einfacher Minecraft-Mod für **NeoForge**, der dir die Haltbarkeit deiner Items übersichtlich anzeigt.  
Inspiriert von [Durability101](https://modrinth.com/mod/durability101).

---

## ✨ Features
- Zeigt die Haltbarkeit von Werkzeugen, Waffen und Rüstungen im HUD und der Hotbar an.
- Anpassbare Darstellung (Zahlen, Prozent, Balken).
- Client Side Mod, kann daher ohne Probleme überall verwendet werden.

---

## 📦 Installation
1. Lade die neueste Version von den [Releases](https://github.com/Extraherz/DurabilityDisplay/releases) herunter.
2. Kopiere die `.jar` in deinen `mods/` Ordner.
3. Stelle sicher, dass du **NeoForge 1.21.1** verwendest.
4. Starte Minecraft – fertig!

---

## 🕹️ Nutzung
- Die Haltbarkeit wird automatisch im HUD angezeigt, sobald du ein Item in der Hand hältst oder Rüstung trägst oder auch in der Hotbar
- Einstellungen wie Farbe der einzelnen Prozente (100, 75, 50, 25%), so wie das Ein- und Ausschalten der Tooltip- oder Nummeranzeige können über die Konfigurationsdatei `config/durabilitydisplay.toml` angepasst werden.

---

## 🔧 Entwicklung
Falls du das Projekt selbst kompilieren oder erweitern möchtest:

```bash
git clone https://github.com/Extraherz/DurabilityDisplay.git
cd DurabilityDisplay
./gradlew build
