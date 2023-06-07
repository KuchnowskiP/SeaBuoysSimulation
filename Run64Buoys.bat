echo Compiling.

::javac BuoyApplication.java
For /L %%y IN (1, 1, 40) DO (
    start /MIN java -Xmx6144k pl.edu.pwr.pkuchnowski.statki.Buoy.BuoyApplication
)

ping -n 2 127.0.0.1>NUL

For /L %%y IN (1, 1, 24) DO (
    start /MIN java -Xmx6144k pl.edu.pwr.pkuchnowski.statki.Buoy.BuoyApplication
)

echo Running!