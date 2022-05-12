#### Bibliofy - ein Android-App Projekt von Martin Jugenheimer, Anna-Lisa Merkel, Molham Al-khodari und Mohammad Assaf

Link zum Repository: [Bibliofy](https://git.ai.fh-erfurt.de/bibliofy/bibliofy)

## Was ist Bibliofy?

Bibliofy ist eine App zur Verwaltung von Büchern, die im Rahmen des Kurses "Programmierung mobiler Endgeräte" im Wintersemester 21/22 an der Fachhochschule Erfurt entstand. Nutzer können mit der App einen Überblick über alle Bücher in ihrem Besitz behalten, sie kategorisieren und filtern.

## Kernfunktionalitäten der App

- Hinzufügen von Büchern in die Sammlung
- Sammlung aller wichtigen Informationen über ein Buch an einem Ort
- mögliche Kategorisierung in gelesene, gewünschte und favorisierte Bücher
- Filterung der Bücher nach diesen Kategorien, Büchertiteln oder Autoren
- Bearbeiten oder Löschen von Büchern
- Einteilung von Büchern in verschiedene Regale zur weiteren Kategorisierung

## Aufbau der App

Der Übersichtlichkeit halber besteht Bibliofy aus vier einfachen Ansichten, die über die Navigation erreicht werden können, sowie weiteren Unteransichten. Das Design der App ist sehr schlicht gehalten. Die einzige Akzentfarbe orange sorgt in Verbindung mit grauer Schrift, weißem Hintergrund (beziehungsweise schwarz im Darkmode) und einem sehr übersichtlichen Layout dafür, dass der Nutzer stets den Überblick bewahren kann und sich sofort in der App zurecht findet.

<details>
<summary>Home</summary>
<br>
Auf der Home-Seite sind alle Bücher aufgelistet. Die Seite wird auch beim Starten der App als Startseite angezeigt. Nutzer können hier über eine Navigationsleiste am oberen Bildschirmrand die Bücherliste nach gelesenen, gewünschten, favorisierten und allen Büchern filtern. Über das Lupensymbol am obeen rechten Rand können bestimmte Bücher anhand von Buchtitel und Autor gesucht werden. Außerdem können Nutzer eins der Bücher antippen und werden so auf eine Buchdetail-Seite weitergeleitet.
<br>
<br>
<details>
<summary>Buchdetails</summary>
<br>
Auf dieser Unterseite erhält der Nutzer eine Übersicht über die wichtigsten Informationen des ausgewählten Buches. Er kann es hier außerdem zu den drei Kategorien hinzufügen, das Buch löschen oder bearbeiten.
<br>
</details>
</details>

<details>
<summary>Bibliothek</summary>
<br>
Hier werden dem Nutzer Bücher in Regalen angezeigt, zu denen er sie zuvor beim Hinzufügen des Buches eingeordnet hat. Es stehen sechs Regale (Regal A-F) zur Verfügung.
<br>
</details>

<details>
<summary>Buch hinzufügen</summary>
<br>
Auf dieser Seite kann der Nutzer ein neues Buch zu seiner Sammlung hinzufügen. Dazu gibt er Daten zum Autor und zum Buch ein, in Form von diversen Eingabefeldern und Buttons. Ein Bild zum Buch kann der Nutzer auch hinterlegen. Tut er dies nicht, wird das Buch in der List mit einem Standardbild angezeigt. Die getätigten Eingaben müssen zu erst validiert werden, bevor das Buch gespeichert werden kann.
<br>
</details>

<details>
<summary>Einstellungen</summary>
<br>
In den Einstellungen kann der Nutzer, wie der Name schon sagt, Einstellungen für die App festlegen. Beispielsweise kann ein Darkmode ausgewählt werden.
<br>
</details>

## Präsentationen
- [Teamvorstellung](documents/Teamvorstellung_Bibliofy.pdf)
- [Zwischenpräsentation](documents/Zwischenprasentation_Bibliofy.pdf)
- [Abschlusspräsentation](documents/Abschlussprasentation_Bibliofy.pdf)

## genutzte Werkzeuge und Tools
- Picasso (Laden von Standardbildern)
- Faker (Erzeugen von Testdaten)

<br>

- Android Studio (IDE)
- GitLab (Versionsverwaltung)
- Figma (Erstellen von Mockups)
- Discord (Kommunikation und Meetings)
