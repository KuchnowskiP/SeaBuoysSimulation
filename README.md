# SeaBuoysSimulation
Simulation of a network of water buoys informing the headquarters about sea level changes. From this we deduce where the ships are. Ships move randomly around the map.

Project contains four main components:
  The World - Applicaction providing the map around which the ships are gonna move.
  The Central - Application providing the map on which sea evels are gonna be shown
  The Buoy  - Application which will recive information from the World about ships passing nearby
  The Ship - moving randomly around the World map
  
 Communication between application is implemented with Server Sockets.
 
 To properly run this simulation:
  Run CentralApplication and click the start button
  Run WorldApplication and click the start button
  Run RUn64buoys.bat from 
