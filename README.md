# #girlswhoviking

![Build Status](https://github.com/michael-simons/girlswhoviking/actions/workflows/build.yml/badge.svg)

_Ein interaktives Online-Quiz um den Launch des Buches 'Schildmaid' von Judith und Christian Vogt zu begleiten._

Das Projekt ist in Java programmiert und basiert auf [Quarkus](https://quarkus.io). Es nutzt das Server-Side-Rendering-Framework
[Qute](https://quarkus.io/guides/qute-reference) zusammen mit JAX-RS. Das Stylesheet ist [Simple.css](https://simplecss.org).

Das ["contributing" Dokument](/CONTRIBUTING.md) beinhaltet englisch-sprachige Informationen, wie das Projekt gebaut und 
ausgeführt werden kann.

Der Programmcode wird unter The Apache Software License, Version 2.0 veröffentlicht.

Fragen und Antworten können in den Dateien `src/main/resouces/questions.json` beziehungsweise
für den Entwicklungsmodus in `src/main/resouces/dummy-questions.json` gepflegt werden.

Zur Ausführung wird Java 17 benötigt. Eine freie und kostenlose Java-Version kann unter https://adoptium.net geladen werden.
Eine von Oracle verteilte Variante steht unter https://dev.java bereit. Auf Linux oder macOS wird anschließend im Projektordner
`./mvnw compile quarkus:dev` aufgerufen, unter Windows `mvnw.cmd compile quarkus:dev`. Nach kurzer Zeit ist ein 
lokaler Server unter http://localhost:8080 erreichbar.

Geänderte Dateien oder Java-Klassen werden durch den eingebauten Entwicklungsmodus automatisch neu kompiliert und geladen. 
Eine interaktive Entwickler:innen-UI ist im Dev-Modus unter http://localhost:8080/q/dev/ verfügbar.

Dieses Repository wird nach erfolgreichen [Builds](https://github.com/michael-simons/girlswhoviking/actions/workflows/build.yml) direkt unter https://girlswhoviking.herokuapp.com live zur Verfügung gestellt.
