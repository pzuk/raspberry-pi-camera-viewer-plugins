package pl.effisoft.rpicamviewer2.aidl;

interface IRpiCamViewerInterface {

    void sendCommand(String cmd, in Bundle params);

}