# raspberry-pi-camera-viewer-plugins
Example plugins for RaspberryPi Camera Viewer (Gstreamer viewer) Android application

RapsberryPi Camera Viewer (Gstreamer Viewer) available [here](https://play.google.com/store/apps/details?id=pl.effisoft.rpicamviewer2) allows you write your own plugins and attach them to any configured pipeline. Communication between main application and plugin is bidirectional. Application notifies plugin and plugin can manage application as well. Plugin can be any custom peace of code. Thanks to plugins you may build custom GUI, manage pipeline, create additional communication with your streaming server etc. If currently available interface is not enough for your needs – let us know and we extend it.

## Plugin interface
Application calls following methods:
#### Plugin introduce methods
```
String pluginName();
String pluginDescription();
String pluginVersion();
```
Plugin introduce itself using three metods above. They should return basic information about plugin, like it's name, description and current version.
#### Activity notifications
```
void parentActivityOnCreate(String pipeline, int camera, IRpiCamViewerInterface callback);
```
It’s not called directly from Activity’s onCreate() method. In onCreate() method RapsberryPi Camera Viewer binds plugins. Once connection to plugin is successfully established, application will query plugin using 3 methods:
```
String pluginName();
String pluginDescription();
String pluginVersion();
```
They should return basic information about plugin. In the last step (when Activity is visible and communication with plugins is made)  parentActivityOnCreate is called. 
```
void parentActivityOnClose();
```
Called directly from Activity onClose()
```
void parentActivityOnPause();
```
Called directly from Activity onPause()
```
void parentActivityOnResume();
```
Called directly from Activity onResume()
```
void parentActivityOnStart();
```
Called directly from Activity onStart()
```
void parentActivityOnStop();
```
Called directly from Activity onStop()
```
void parentActivityOnFullscreen(boolean fullscreen);
```
Called when Activity goes/returns to/from fullscreen mode. Boolean attribute determines current state; true when activity is currently in fullscreen mode, false otherwise
```
void parentActivityOnControlsVisibilityChange(boolean visible);
```
Called when Activity shows/hides visible controls. Boolean attribute determines current state; true when controls are currently visible, false otherwise
#### Pipeline notifications
```
void pipelinePlayingProgress(int progress, int total, int camera);
```
Be careful with this method implementation. It’s called every time when pipeline is in PLAYING state (many times per second), so any code inside must be fast, otherwise you block RapsberryPi Camera Viewer and your pipeline.
```
void pipelineStateChanged(int state, int camera);
```
Called when pipeline changes it’s state. State can be one of following:
```
GST_STATE_VOID_PENDING        = 0,
GST_STATE_NULL                = 1,
GST_STATE_READY               = 2,
GST_STATE_PAUSED              = 3,
GST_STATE_PLAYING             = 4
```
```
void pipelineScreenshotMade(in Bitmap bitmap, int camera);
```
Called when screenshot has been made. Camera determines camera identifier see below.

### Camera identifiers
Some methods have “camera” integer attribute. In current version of RapsberryPi Camera Viewer (Gstreamer Viewer) this value can be one of: 0,1,2 or 3. Index determines number of preview inside Activity. Current version can show up to 4 previews on single activity. The same plugin can be attached to more than one preview on the same activity, so this variable may help you identify preview source.

## RaspberryPi Camera Viewer interface
Plugin can manage RapsberryPi Camera Viewer preview activity it belongs to. To send commands use:
```
void sendCommand(String cmd, in Bundle params);
```
Method is expandable and in further RapsberryPi Camera Viewer versions commands list may grow, but interface remains unchanged. List of available commands:
```
PIPELINE_PLAY
PIPELINE_PAUSE
PIPELINE_STATE
PIPELINE_SCREENSHOT
PREVIEW_FULLSCREEN
PREVIEW_HIDECONTROLS
PREVIEW_HIDE_CONTROLS_BUTTON
```
In response, RapsberryPi Camera Viewer will call:
```
void commandReceived(String cmd, in Bundle result, int camera);
```
Bundle contains at least one boolean element "ok"=true/false which determins success or failure of operation.

##### PIPELINE_PLAY

Request bundle: 
> "camera" : int - camera index plugin belongs to

Response bundle: 
> "ok" : boolean - command result true(success)/false(failure)

##### PIPELINE_PAUSE
Request bundle: 
> "camera" : int - camera index plugin belongs to

Response bundle: 
> "ok" : boolean - command result true(success)/false(failure)

##### PIPELINE_STATE
Request bundle: 
> "camera" : int - camera index plugin belongs to

Response bundle: 
> "ok" : boolean - command result true(success)/false(failure)  
> "playing" : boolean - true when pipeline is playing, false otherwise  
> "position" : int - current position timestamp   
> "duration" : int - pipeline duration  
> "state" : int -  pipeline state  
> "fullscreen" : boolean - true when working in fulscreen, false otherwise  
> "controlsvisible" : boolean - true when GUI controls are visible, false otherwise  
> "controlswitchvisible" : boolean - true when switch GUI controls button is visible, false otherwise  
> "workAreaTop" : int -  work area dimensions  
> "workAreaBottom" : int -  work area dimensions  
> "workAreaLeft" : int -  work area dimensions  
> "workAreaRight" : int -  work area dimensions  
> "workAreaWidth" : int -  work area dimensions  
> "workAreaHeight" : int -  work area dimensions  
                
##### PIPELINE_SCREENSHOT
Request bundle: 
> "camera" : int - camera index plugin belongs to

Response bundle: 
> "ok" : boolean - command result true(success)/false(failure)  
> "bitmap" : Bitmap - image

##### PREVIEW_FULLSCREEN 
Request bundle: 
> "camera" : int - camera index plugin belongs to  
> "fullscreen" : boolean - true/false

Response bundle: 
> "ok" : boolean - command result true(success)/false(failure)

##### PREVIEW_HIDECONTROLS
Request bundle: 
> "camera" : int - camera index plugin belongs to  
> "hide" : boolean - true/false

Response bundle: 
> "ok" : boolean - command result true(success)/false(failure)

##### PREVIEW_HIDE_CONTROLS_BUTTON
Request bundle: 
> "camera" : int - camera index plugin belongs to  
> "hide" : boolean - true/false

Response bundle: 
> "ok" : boolean - command result true(success)/false(failure)

Please contact us in case of any questions.