package com.example.rpicamviewer2.plugin;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import pl.effisoft.rpicamviewer2.aidl.IPluginInterface;
import pl.effisoft.rpicamviewer2.aidl.IRpiCamViewerInterface;

public class PluginService2 extends Service {
	static final String LOG_TAG = "PluginService2";


	public void onStart(Intent intent, int startId) {
		super.onStart( intent, startId );
	}

	public void onDestroy() {
		super.onDestroy();
	}

	public IBinder onBind(Intent intent) {
		return interfaceBinder;
	}
	
    private final IPluginInterface.Stub interfaceBinder = new IPluginInterface.Stub() {
		
		@Override
		public void parentActivityOnCreate(String pipeline, int camera, IRpiCamViewerInterface callback)
				throws RemoteException {
			Log.v(LOG_TAG, "parentActivityOnCreate, pipeline="+ pipeline+", cam="+camera);
		}

		@Override
		public void parentActivityOnClose() throws RemoteException {
			Log.v(LOG_TAG, "parentActparentActivityOnClose");
		}

		@Override
		public void parentActivityOnFullscreen(boolean fullscreen)
				throws RemoteException {
			Log.v(LOG_TAG, "parentActivityOnFullscreen, fullscreen="+fullscreen);
			
		}

		@Override
		public void parentActivityOnControlsVisibilityChange(boolean visible)
				throws RemoteException {
			Log.v(LOG_TAG, "parentActivityOnControlsVisibilityChange, visible="+visible);
			
		}

		@Override
		public void pipelinePlayingProgress(int progress, int total, int camera)
				throws RemoteException {
			Log.v(LOG_TAG, "pipelinePlayingProgress, progress="+progress+" ,total="+total);
			
		}

		@Override
		public void pipelineStateChanged(int state, int camera) throws RemoteException {
			Log.v(LOG_TAG, "pipelineStateChanged, state="+state);
			//   state:
					//		GST_STATE_VOID_PENDING        = 0,
					//		GST_STATE_NULL                = 1,
					//		GST_STATE_READY               = 2,
					//		GST_STATE_PAUSED              = 3,
					//		GST_STATE_PLAYING             = 4
		}

		@Override
		public void pipelineScreenshotMade(Bitmap bitmap, int camera)
				throws RemoteException {
			Log.v(LOG_TAG, "pipelineScreenshotMade");
			
		}

		@Override
		public String pluginName() throws RemoteException {
			return "Demo empty plugin";
		}

		@Override
		public String pluginDescription() throws RemoteException {
			return "Demonstrates empty plugin";
		}

		@Override
		public String pluginVersion() throws RemoteException {
			return "1.0";
		}

		@Override
		public void commandReceived(String cmd, int camera)
				throws RemoteException {
			Log.v(LOG_TAG, "commandReceived, cmd="+cmd+" ,cam="+camera );
			
		}

		@Override
		public void parentActivityOnPause() throws RemoteException {
			Log.v(LOG_TAG, "parentActivityOnPause" );
			
		}

		@Override
		public void parentActivityOnResume() throws RemoteException {
			Log.v(LOG_TAG, "parentActivityOnResume" );
			
		}

		@Override
		public void parentActivityOnStart() throws RemoteException {
			Log.v(LOG_TAG, "parentActivityOnStart" );
			
		}

		@Override
		public void parentActivityOnStop() throws RemoteException {
			Log.v(LOG_TAG, "parentActivityOnStop" );
			
		}
    };

}

