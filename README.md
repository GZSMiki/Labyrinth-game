[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/XbZw8B6J)
# Labirintus

TODO: a detailed description of your project (must contain at least the original description of the assignment)

A 12. ábrán egy labirintus látható, melybe egy világos koronggal jelzett
figurát és egy sötét koronggal jelzett szörnyet helyezünk.
Feladatunk a figura kijuttatása a labirintusból a kijáraton át úgy, hogy
közben a szörny nem kapja el. A figura függőlegesen és vízszintesen mozoghat
egy mezőt, és nem léphet át az ábrán vastag vonallal jelzett falakon.
    A figura minden egyes lépése maga után vonja a szörny elmozdulását. A
    szörny minden esetben két mezőt próbál lépni úgy, hogy közelebb kerüljön a
    figurához, mozgása során pedig előnyben részesíti a vízszintes irányú elmozdul
    ást. Összefoglalva, az alábbi algoritmus szerint mozog:
1. Ha balra vagy jobbra mozoghat egy mezőt, miközben közelebb kerül a
   figurához, akkor lépjen az adott irányba.
   (a) Ha még egyet léphet balra vagy jobbra, miközben közelebb kerül
   a figurához, akkor lépjen az adott irányba.
   
   (b) Egyébként ha még egyet léphet felfelé vagy lefelé, miközben közelebb
   kerül a figurához, akkor lépjen az adott irányba.
2. Egyébként ha felfelé vagy lefelé mozoghat egy mezőt, miközben közelebb
   kerül a figurához, akkor lépjen az adott irányba.
   (a) Ha még egyet léphet balra vagy jobbra, miközben közelebb kerül
   a figurához, akkor lépjen az adott irányba.
   (b) Egyébként ha még egyet léphet felfelé vagy lefelé, miközben közelebb
   kerül a figurához, akkor lépjen az adott irányba.
