# Adobe Media Encoder Autoshutdown
Automatically shutdown after Adobe Media Encoder encoding/rendering. This will also work for almost any programs where you can create a file once it is finished. Or something like Sony Vegas where it only creates the final file once the rendering is finished. This programm will also work with Blender, Sony Vegas, Adobe After Affects, Adobe Premiere Pro, etc...

![](https://img.shields.io/github/license/lshallo/ame_autoshutdown.svg?style=flat-square) [![](https://img.shields.io/github/release-date/SubtitleEdit/subtitleedit.svg?style=flat-square)](https://github.com/LsHallo/ame_autoshutdown/releases)

## How to use
### Windows
1. Download the program
2. Setup your render list
3. Add a project at the end (an image) that creates a file at a specific location
![Rendering List](https://cdn.instructables.com/FCC/20B1/J1QP0GG9/FCC20B1J1QP0GG9.LARGE.jpg)
4. Run the program
5. Enter the location and the name of the file into the program and enable it (e.g.: C:\Desktop\ and render.mp4)
![Program Screenshot](https://cdn.instructables.com/FK5/48SO/J1QP0GFR/FK548SOJ1QP0GFR.LARGE.jpg)
6. Tick the enabled checkbox
7. Start rendering
8. Done - The program will shutdown your computer

### Linux/Ubuntu/MacOS X
#### Since Version v0.2:
1. Download the program
2. Setup your render list
3. Add a project at the end (an image) that creates a file at a specific location
![Rendering List](https://cdn.instructables.com/FCC/20B1/J1QP0GG9/FCC20B1J1QP0GG9.LARGE.jpg)
4. Run the jar file like you always do and skip step 5 *OR* Open a terminal and continue with step 5
5. Type: java -jar location/to/AME.jar
6. Enter the location and the name of the final file into the program and enable it (e.g.: /home/desktop and render.mp4)
![Program Screenshot](https://cdn.instructables.com/FK5/48SO/J1QP0GFR/FK548SOJ1QP0GFR.LARGE.jpg)
7. Tick the enabled checkbox
8. Start rendering
9. Done - The program will shutdown your computer

*Additional Info*  
This program uses `shutdown -h 1` or `sudo -S shutdown -h 1` (if the first fails) to shutdown on unix based systems. Make sure your system uses the same commands and supports **sudo**.  

<details>
<summary>
  <span><b>Pre Version v0.2</b></span>
</summary>
  <ol>
    <li>Download the program</li>
    <li>Setup your render list</li>
    <li>Add a project at the end (an image) that creates a file at a specific location</li>
    <li>Open a terminal</li>
    <li>Type: java -jar location/to/AME.jar</li>
    <li>Enter your sudo password when asked</li>
    <li>Enter the location and the name of the file into the program and enable it (e.g.: /home/desktop and render.mp4)</li>
    <li>Start rendering</li>
    <li>Done - The program will shutdown your computer</li>
  </ol>
</details>

## Download
[![HitCount](http://hits.dwyl.io/LsHallo/ame_autoshutdown.svg)](http://hits.dwyl.io/LsHallo/ame_autoshutdown)  ![](https://img.shields.io/github/downloads/lshallo/ame_autoshutdown/total.svg?style=flat-square)

https://github.com/LsHallo/ame_autoshutdown/releases

Working on **Windows** *(Confirmed)* and **Linux** *(Ubuntu 16.04 and 18.04 confirmed)*.
It should also work for **MacOS X**
