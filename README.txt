README

Smart Home Project

Authors:
Wenxuan Mao, Yuanyi Ding

1. Smartthings hub setup:

Power on hub, connect ethernet cable, wait for status light to be green
Pair Sensors

2. Log sensor data to google sheets
(We think it is easier to send it directly to google doc for collaboration and analysis.
For better performance in real-time implementation, server running inference is a better target.)

github repo: https://github.com/cschwer/googleDocsLogging
folder "googleDocsLogging"
follow README to install

Create new google sheet
Configure sheet id and desired sensor data to be logged
Create a new script on google sheet for saving incoming data	

3. Configure dash button for labeling

On Windows PC:
https://github.com/fiveseven808/AmazonDashButtonHack
two exe files in "dashButton" folder
One to discover and ping the button in LAN
Other running at background triggering log script
*.bat records time into a txt file

On Raspberry Pi:
(Didn't test because of the raspberry pi initial setup)
https://github.com/vancetran/amazon-dash-rpi

4. Data/Label processing
java program to read in csv file and output 5 minute timestamps of data

data_label is a combined datalabel csv that is used in next stage training

5. Training model
learning.py
Use tensorflow & keras & python
GPU enabled by default, Nvidia GPU GTX1080 used, needs CuDNN, CUDA lib


Loss function can be changed
Accuracy and precision details are output at last
loss function output as csv and can be plotted with epoch_loss.py


6. RealTime
CSV File Download from google:
url method: docs.google.com/feeds/download/spreadsheets/Export?key=<FILE_ID>&exportFormat=csv&gid=0
OR
Google API method: See FileDownload.py(needs to setup credentials at Google API)
https://developers.google.com/sheets/api/quickstart/python

load with "real_time_prediction.py"
get results


Credit to contributors to open license contributors on github
1. google docs logging: cschwer https://github.com/cschwer/googleDocsLogging
2. amazon dash button: fiveseven808 https://github.com/fiveseven808/AmazonDashButtonHack

Tools that we used:
Tensorflow: https://github.com/tensorflow/tensorflow
            http://tensorflow.org

Keras: https://keras.io/
       https://github.com/fchollet/keras
<<<<<<< HEAD
=======

>>>>>>> 8420b6a64eb38a94b36e53b1743adcbe5c14e200

