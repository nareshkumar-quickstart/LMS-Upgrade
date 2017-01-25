/****************************
* Manuel Gonzalez           *
* design@stheory.com        *
* www.stheory.com           *
* www.codingcolor.com       *
*****************************/
package{
	
	import flash.display.Sprite;
	import flash.display.MovieClip;
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.text.TextField;
	import flash.text.TextFormat;
	import flash.external.ExternalInterface;
	
	public class Main extends Sprite{
		private var _downloadTest:DownloadBandwidthTest;
		private var _testFile:String = "bandwidth/1.jpg";
		private var _circleAnimation:CircleAnimation;
		public function Main()
		{
			testBandwidth();
		}
		/*
		Method: testBandwidth
		Parameters:
		Returns:
		*/
		private function testBandwidth():void
		{
			_downloadTest = new DownloadBandwidthTest(_testFile);
			_downloadTest.addEventListener(Event.COMPLETE,completeEventHandler,false,0,true);
			_downloadTest.addEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler,false,0,true);
			_downloadTest.startBandwidthCheck();
			addLoadingAnimation();
			
		}
		/*
		Method: addLoadingAnimation
		Parameters:
		Returns:
		*/
		private function addLoadingAnimation():void {
			_circleAnimation = new CircleAnimation();
			_circleAnimation.x = (resultBkg.x + resultBkg.width/2) - _circleAnimation.width/2;
			_circleAnimation.y = (resultBkg.y + resultBkg.height/2) - _circleAnimation.height/2 + 10;
			addChild(_circleAnimation);
		}
		/*
		Method: removeLoadingAnimation
		Parameters:
		Returns:
		*/
		private function removeLoadingAnimation():void {
			removeChild(_circleAnimation);
		}
		/*
		Method: completeEventHandler
		Parameters:
		event:Event
		Returns:
		*/
		private function completeEventHandler(event:Event):void
		{
			removeLoadingAnimation();
			_downloadTest.removeEventListener( Event.COMPLETE, completeEventHandler );
			_downloadTest.removeEventListener( IOErrorEvent.IO_ERROR, ioErrorHandler );
			var detectedSpeed:Number = event.target.downloadSpeed;
			tField.text = String(detectedSpeed) + " kbps";
			ExternalInterface.call("jsfun");
		}
		/*
		Method: ioErrorHandler
		Parameters:
		event:IOErrorEvent
		Returns:
		*/
		private function ioErrorHandler(event:IOErrorEvent):void
		{
			removeLoadingAnimation();
			_downloadTest.removeEventListener( Event.COMPLETE, completeEventHandler );
			_downloadTest.removeEventListener( IOErrorEvent.IO_ERROR, ioErrorHandler );
			var errorFormat:TextFormat = new TextFormat();
            errorFormat.color = 0xFF0000;
			tField.text = event.text;
			tField.setTextFormat(errorFormat)
		}
	
	
	
	}
}