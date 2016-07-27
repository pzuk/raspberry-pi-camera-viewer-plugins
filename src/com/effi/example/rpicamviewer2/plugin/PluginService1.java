package com.effi.example.rpicamviewer2.plugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import pl.effisoft.rpicamviewer2.aidl.IPluginInterface;
import pl.effisoft.rpicamviewer2.aidl.IRpiCamViewerInterface;

import com.effi.example.rpicamviewer2.plugin.R;

public class PluginService1 extends Service {
	static final String LOG_TAG = "PluginService1";

	private IRpiCamViewerInterface callback_;
	private WindowManager windowManager;
	
	class ViewHolder extends LinearLayout
	{
		public int camera_;
		public View view;
		public ViewHolder(Context context, int camera) {
	        super(context);
	        view = View.inflate(context, R.layout.main, this);
	        camera_ = camera;
	    }
	};
	HashMap<Integer, ViewHolder> views = new HashMap<Integer, ViewHolder>();
	
	
	private MainThreadHandler mainHandler;
    private final class MainThreadHandler extends Handler {
        public MainThreadHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg)
        {
        	Bundle bundle = msg.getData();
        	if( bundle.getString("action") == null ) 
        		return;
        	
        	//-------------------------- parentActivityOnCreate -------------------------------
        	if( bundle.getString("action").equals("parentActivityOnCreate") )
        	{
        		//Do not send PREVIEW_FULLSCREEN message from parentActivityOnCreate callback. Switching from/to 
        		//full screen requires activity restart, so you may case infinity loop because of that. 
        		
        		int camera = bundle.getInt("camera", -1);
            	if( camera == -1 ) 
            		return;
            	
        		final ViewHolder viewHolder = new ViewHolder(PluginService1.this, camera);
        	    views.put(camera, viewHolder);
		    
        		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        		final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
			        WindowManager.LayoutParams.WRAP_CONTENT,
			        WindowManager.LayoutParams.WRAP_CONTENT,
			        WindowManager.LayoutParams.TYPE_PHONE,
			        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
			        PixelFormat.TRANSLUCENT);

			    params.gravity = Gravity.TOP | Gravity.LEFT;
				params.x = 0;
				params.y = 100;
			    
				Button btnUp = (Button) viewHolder.view.findViewById(R.id.btnUp);
				Button btnDown = (Button) viewHolder.view.findViewById(R.id.btnDown);
				Button btnLeft = (Button) viewHolder.view.findViewById(R.id.btnLeft);
				Button btnRight = (Button) viewHolder.view.findViewById(R.id.btnRight);
			
				if( btnUp != null )
				{
					btnUp.setOnClickListener( new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							Toast localToast = Toast.makeText(arg0.getContext(), "Up "+ viewHolder.camera_, 0);
						    localToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
						    localToast.show();
						}
					});
				}
				if( btnDown != null )
				{
					btnDown.setOnClickListener( new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							Toast localToast = Toast.makeText(arg0.getContext(), "Down "+ viewHolder.camera_, 0);
						    localToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
						    localToast.show();
						    try {
						    	//Example below Play/Pause pipeline. List of all available commands with description you may find 
						    	//here: http://effisoft.pl/raspberrypi-camera-viewer
						    	Bundle bundle = new Bundle();
						    	bundle.putInt("camera", viewHolder.camera_);
						    	//bundle.putBoolean("hide", true);
						    	
								callback_.sendCommand("PIPELINE_PLAY", bundle);
								/* You can use one of:
								 * PIPELINE_PLAY
								 * PIPELINE_PAUSE
								 * PIPELINE_STATE
								 * PIPELINE_SCREENSHOT
								 * PREVIEW_FULLSCREEN
								 * PREVIEW_HIDECONTROLS
								 * PREVIEW_HIDE_CONTROLS_BUTTON
								 * */
							} catch (RemoteException e) {
								Log.e(LOG_TAG, e.getMessage());
								e.printStackTrace();
							}
						}
					});
				}
				if( btnLeft != null )
				{
					btnLeft.setOnClickListener( new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							Toast localToast = Toast.makeText(arg0.getContext(), "Left "+ viewHolder.camera_, 0);
						    localToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
						    localToast.show();
						}
					});
				}
				if( btnRight != null )
				{
					btnRight.setOnClickListener( new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							Toast localToast = Toast.makeText(arg0.getContext(), "Right "+ viewHolder.camera_, 0);
						    localToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
						    localToast.show();
						}
					});
				}

				viewHolder.view.setOnTouchListener(new View.OnTouchListener() {
			    	  private int initialX;
			    	  private int initialY;
			    	  private float initialTouchX;
			    	  private float initialTouchY;
	
			    	  @Override 
			    	  public boolean onTouch(View v, MotionEvent event) {
			    		  
			    		  if( v == viewHolder.view)
			    		  {
			    	    switch (event.getAction()) {
			    	      case MotionEvent.ACTION_DOWN:
			    	        initialX = params.x;
			    	        initialY = params.y;
			    	        initialTouchX = event.getRawX();
			    	        initialTouchY = event.getRawY();
			    	        return true;
			    	      case MotionEvent.ACTION_UP:
			    	        return true;
			    	      case MotionEvent.ACTION_MOVE:
			    	        params.x = initialX + (int) (event.getRawX() - initialTouchX);
			    	        params.y = initialY + (int) (event.getRawY() - initialTouchY);
			    	        windowManager.updateViewLayout(viewHolder.view, params);
			    	        return true;
			    	    }
			    		  }
			    	    return false;
			    	  }
			    	});

				windowManager.addView(viewHolder, params);
        	}
        	else if( bundle.getString("action").equals("parentActivityOnClose"))
        	{
    			for (Map.Entry<Integer, ViewHolder> entry : views.entrySet()) {
        			windowManager.removeView(entry.getValue());
        		}
    			views.clear();
        	}
        	else if( bundle.getString("action").equals("parentActivityOnPause") )
        	{
        		for (Map.Entry<Integer, ViewHolder> entry : views.entrySet()) {
        			entry.getValue().view.setVisibility(View.INVISIBLE);
        		}
        	}
        	else if( bundle.getString("action").equals("parentActivityOnResume") )
        	{
        		for (Map.Entry<Integer, ViewHolder> entry : views.entrySet()) {
        			entry.getValue().view.setVisibility(View.VISIBLE);
        		}
        	}
        	else if( bundle.getString("action").equals("parentActivityOnStart") )
        	{
        		for (Map.Entry<Integer, ViewHolder> entry : views.entrySet()) {
        			entry.getValue().view.setVisibility(View.VISIBLE);
        		}
        	}
        	else if( bundle.getString("action").equals("parentActivityOnStop") )
        	{
        		for (Map.Entry<Integer, ViewHolder> entry : views.entrySet()) {
        			entry.getValue().view.setVisibility(View.INVISIBLE);
        		}
        	}

        }
    }
	

	public void onStart(Intent intent, int startId) {
		super.onStart( intent, startId );
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override public void onCreate() {
	    super.onCreate();
	    mainHandler = new MainThreadHandler( getMainLooper());
	}
	

	public IBinder onBind(Intent intent) {
      	return interfaceBinder;
	}

    private final IPluginInterface.Stub interfaceBinder = new IPluginInterface.Stub() {
		

    	/*
    	 * It’s not called directly from Activity’s onCreate() method. 
    	 * In onCreate() method RapsberryPi Camera Viewer binds plugins. 
    	 * Once connection to plugin is successfully established, 
    	 * application will query plugin using 3 methods:
    	 * String pluginName();
    	 * String pluginDescription();
    	 * String pluginVersion();
    	 * They should return basic information about plugin. In the 
    	 * last step (when Activity is visible and communication with 
    	 * plugins is made)  parentActivityOnCreate is called. */
		@Override
		public void parentActivityOnCreate(String pipeline, int camera, IRpiCamViewerInterface callback) throws RemoteException {
			callback_ = callback;
			//Notify main thread about parentActivityOnCreate
			Message msg = mainHandler.obtainMessage();
			Bundle bundle = new Bundle();
			bundle.putString("action", "parentActivityOnCreate");
			bundle.putInt("camera", camera);
	        msg.setData(bundle);
	        mainHandler.sendMessage(msg);
		}

		//Called directly from Activity onClose()
		@Override
		public void parentActivityOnClose() throws RemoteException {
			//Notify main thread about parentActivityOnClose
			Message msg = mainHandler.obtainMessage();
			Bundle bundle = new Bundle();
			bundle.putString("action", "parentActivityOnClose");
	        msg.setData(bundle);
	        mainHandler.sendMessage(msg);
	        
		}

		/*
		 * Called when Activity goes/returns to/from fullscreen mode. 
		 * Boolean attribute determines current state; true when activity 
		 * is currently in fullscreen mode, false otherwise
		 * */
		@Override
		public void parentActivityOnFullscreen(boolean fullscreen)
				throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		/*
		 * Called when Activity shows/hides visible controls. Boolean 
		 * attribute determines current state; true when controls are 
		 * currently visible, false otherwise
		 * */
		@Override
		public void parentActivityOnControlsVisibilityChange(boolean visible)
				throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		/*
		 * Be careful with this method implementation. It’s called 
		 * every time when pipeline is in PLAYING state (many times 
		 * per second), so any code inside must be fast, otherwise 
		 * you block RapsberryPi Camera Viewer and your pipeline.
		 * */
		@Override
		public void pipelinePlayingProgress(int progress, int total, int camera)
				throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		/*
		 * Called when pipeline changes it’s state. State can be one 
		 * of following:
		 * GST_STATE_VOID_PENDING        = 0,
		 * GST_STATE_NULL                = 1,
		 * GST_STATE_READY               = 2,
		 * GST_STATE_PAUSED              = 3,
		 * GST_STATE_PLAYING             = 4
		 * */
		@Override
		public void pipelineStateChanged(int state, int camera) throws RemoteException {
			// TODO Auto-generated method stub

		}

		/*
		 * Called when screenshot has been made. Camera 
		 * determines camera identifier see below.
		 * */
		@Override
		public void pipelineScreenshotMade(Bitmap bitmap, int camera)
				throws RemoteException {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public String pluginName() throws RemoteException {
			return "Demo GUI plugin";
		}

		@Override
		public String pluginDescription() throws RemoteException {
			return "Demonstrates plugin with GUI";
		}

		@Override
		public String pluginVersion() throws RemoteException {
			return "1.0";
		}

		//Called in response to sendCommand(String cmd, in Bundle params);
		@Override
		public void commandReceived(String cmd, Bundle result, int camera)
				throws RemoteException {
			// TODO Auto-generated method stub
			
		}

		//Called directly from Activity onPause()
		@Override
		public void parentActivityOnPause() throws RemoteException {
			//Notify main thread about parentActivityOnPause
			Message msg = mainHandler.obtainMessage();
			Bundle bundle = new Bundle();
			bundle.putString("action", "parentActivityOnPause");
	        msg.setData(bundle);
	        mainHandler.sendMessage(msg);
		}

		//Called directly from Activity onResume()
		@Override
		public void parentActivityOnResume() throws RemoteException {
			//Notify main thread about parentActivityOnResume
			Message msg = mainHandler.obtainMessage();
			Bundle bundle = new Bundle();
			bundle.putString("action", "parentActivityOnResume");
	        msg.setData(bundle);
	        mainHandler.sendMessage(msg);
		}

	    //Called directly from Activity onStart()
		@Override
		public void parentActivityOnStart() throws RemoteException {
			//Notify main thread about parentActivityOnStart
			Message msg = mainHandler.obtainMessage();
			Bundle bundle = new Bundle();
			bundle.putString("action", "parentActivityOnStart");
	        msg.setData(bundle);
	        mainHandler.sendMessage(msg);
		}

		//Called directly from Activity onStop()
		@Override
		public void parentActivityOnStop() throws RemoteException {
			//Notify main thread about parentActivityOnStop
			Message msg = mainHandler.obtainMessage();
			Bundle bundle = new Bundle();
			bundle.putString("action", "parentActivityOnStop");
	        msg.setData(bundle);
	        mainHandler.sendMessage(msg);
		}
    };
}

