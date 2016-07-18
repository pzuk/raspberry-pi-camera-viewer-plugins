package pl.effisoft.rpicamviewer2.aidl;
import pl.effisoft.rpicamviewer2.aidl.IRpiCamViewerInterface;

interface IPluginInterface {
  void parentActivityOnCreate(String pipeline, int camera, IRpiCamViewerInterface callback);
  void parentActivityOnClose();
  void parentActivityOnPause();
  void parentActivityOnResume();
  void parentActivityOnStart();
  void parentActivityOnStop();
  void parentActivityOnFullscreen(boolean fullscreen);
  void parentActivityOnControlsVisibilityChange(boolean visible);
  
  void pipelinePlayingProgress(int progress, int total, int camera);
  void pipelineStateChanged(int state, int camera);
  void pipelineScreenshotMade(in Bitmap bitmap, int camera);
  
  void commandReceived(String cmd, int camera);
  
  String pluginName();
  String pluginDescription();
  String pluginVersion();
}
