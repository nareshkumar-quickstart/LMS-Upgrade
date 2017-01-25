/****************************
* Manuel Gonzalez           *
* design@stheory.com        *
* www.stheory.com           *
* www.codingcolor.com       *
*****************************/
package
{
	
	import flash.net.URLRequest;
	import flash.display.Loader;
	import flash.events.EventDispatcher;
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.utils.getTimer;

	public class DownloadBandwidthTest extends EventDispatcher
	{
		private var _imagePath:String;
		private var _bandwidthTests:Array;
		private var _numOfTests:uint;
		private var _downloadSpeed:Number;
		private var _startTime:uint;
		private var _imageLoader:Loader;

		public function DownloadBandwidthTest(inPath:String=null):void
		{
			_imagePath = inPath;
		}
		//////////////////// setters & getters //////////////////////
		public function set imagePath(inStr:String):void
		{
			_imagePath = inStr;
		}
		public function get imagePath():String
		{
			return _imagePath;
		}
		public function set downloadSpeed(inNum:Number):void{ 
			_downloadSpeed = inNum; 
		}
		public function get downloadSpeed():Number{ 
			return _downloadSpeed; 
		}
		public function set numOfTests(inNum:uint):void{ 
			_numOfTests = inNum; 
		}
		public function get numOfTests():uint{ 
			return _numOfTests; 
		}
		/////////////////////////////////////////////////////////////
		/*
		Method: startBandwidthCheck
		Paramters:
		Returns:
		*/
		public function startBandwidthCheck():void
		{
			_numOfTests = 3;
			_downloadSpeed = 0;
			_bandwidthTests=[];
			loadTestFile();
		}
		/*
		Method: loadTestFile
		Paramters:
		Returns:
		*/
		private function loadTestFile():void
		{
			var filepath:String = _imagePath ;//+= "?" + uniqueId();

			_imageLoader = new Loader();
			_imageLoader.contentLoaderInfo.addEventListener(Event.OPEN, startTimer,false,0,true);
			_imageLoader.contentLoaderInfo.addEventListener(Event.COMPLETE, downloadCompleteHandler,false,0,true);
			_imageLoader.contentLoaderInfo.addEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler,false,0,true);
			_imageLoader.load( new URLRequest(filepath) );
		}
		/*
		Method: startTimer
		Paramters:
		event:Event
		Returns:
		*/
		private function startTimer(event:Event):void
		{
			_startTime = getTimer();
		}
		/*
		Method: downloadCompleteHandler
		Paramters:
		event:Event
		Returns:
		*/
		private function downloadCompleteHandler(event:Event):void
		{
			logResults(event.currentTarget.bytesTotal);
			_imageLoader.unload();
		}
		/*
		Method: logResults
		Paramters:
		inBytesTotal:Number
		Returns:
		*/
		private function logResults(inBytesTotal:Number):void
		{
			var fileTotalBytes:Number = inBytesTotal;
			var endTime:uint = getTimer();
			var totalDownloadTime:Number = ( endTime - _startTime ) / 1000;
			var kilobits:Number = fileTotalBytes / 1000 * 8;
			var kbps = Math.floor(kilobits / totalDownloadTime);
			_bandwidthTests.push(kbps);
			if(_bandwidthTests.length >= _numOfTests)
			{
				reportResults();
			}else{
				loadTestFile();
			}
			
		}
		/*
		Method: reportResults
		Paramters: 
		Returns:
		*/
		private function reportResults():void
		{
			var detectedSpeed:Number=0;
			for(var i:uint=0; i < _numOfTests; i++)
			{
				detectedSpeed += _bandwidthTests[i];
			}
			_downloadSpeed = Math.floor(detectedSpeed /_numOfTests); 
			dispatchEvent(new Event(Event.COMPLETE));
			dispose();
		}
		/*
		Method: ioErrorHandler
		Paramters:
		event:IOErrorEvent 
		Returns:
		*/
		private function ioErrorHandler(event:IOErrorEvent):void
		{
			
			_downloadSpeed = -1
			dispatchEvent(event);
			dispose();
		}
		/*
		Method: dispose
		Paramters:
		Returns:
		*/
		private function dispose():void
		{
			_imageLoader.contentLoaderInfo.removeEventListener(Event.OPEN, startTimer);
			_imageLoader.contentLoaderInfo.removeEventListener(Event.COMPLETE, downloadCompleteHandler);
			_imageLoader.contentLoaderInfo.removeEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler);
		}
		/*
		Method: uniqueId
		Paramters:
		Returns:
		Number
		*/
		private function uniqueId():Number
		{
			var id:Number = getTimer(); 
			return id;
		}
	}
}