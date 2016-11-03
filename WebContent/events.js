window.onload = function() {

    var canvas = document.getElementById('mv_canvas');
    var label = document.getElementById("status-label");
      var  startX,
        startY,
        endX,
        endY,
        dist,
        threshold = 150, //required min distance traveled to be considered swipe
        allowedTime = 200, // maximum time allowed to travel that distance
        elapsedTime,
        startTime
       
    function handleswipe(isrightswipe){
        if (isrightswipe)
            canvas.innerHTML = 'Congrats, you\'ve made a <span style="color:red">right swipe!</span>'
        else{
            canvas.innerHTML = 'Condition for right swipe not met yet'
        }
    }
   
    canvas.addEventListener('mouseup', function(e){
    	var mousePos = getMousePos(canvas, e);
       	var x1= Math.round(mousePos.x ) ;
    	var y1= Math.round(mousePos.y ) ;
        var message = 'Mouse position staring: ' + x1 + ',' + y1;
       
        writeMessage(canvas, message);
        console.log(message);
        dist = 0;
        startX = x1;
        startY = y1;
        
        startTime = new Date().getTime(); // record time when finger first makes contact with surface
        e.preventDefault();
    }, false)
 
    canvas.addEventListener('mousemove', function(e){
        e.preventDefault(); // prevent scrolling when inside DIV
    }, false)
 
    canvas.addEventListener('mousedown', function(e){
    	var mousePos = getMousePos(canvas, e);
       	var x2= Math.round(mousePos.x ) ;
    	var y2= Math.round(mousePos.y ) ;
        var message = 'Mouse position ending: ' + x2 + ',' + y2;
        endX=x2;
        endY=y2;
        writeMessage(canvas, message);
        console.log(message);
        dist = x2 - startX // get total dist traveled by finger while in contact with surface
        elapsedTime = new Date().getTime() - startTime // get time elapsed
        // check that elapsed time is within specified, horizontal dist traveled >= threshold, and vertical dist traveled <= 100
        var swiperightBol = (elapsedTime <= allowedTime && dist >= threshold && Math.abs(endY - startY) <= 100)
        handleswipe(swiperightBol)
        e.preventDefault()
    }, false)
    
    
    function writeMessage(canvas, message) {
 		label.innerHTML=message;
    }
    function getMousePos(canvas, evt) {
	       var rect = canvas.getBoundingClientRect();
	       scaleX = canvas.width / rect.width,    // relationship bitmap vs. element for X
	       scaleY = canvas.height / rect.height;  // relationship bitmap vs. element for Y

		   return {
		     x: (evt.clientX - rect.left) * scaleX,   // scale mouse coordinates after they have
		     y: (evt.clientY - rect.top) * scaleY     // been adjusted to be relative to element
		   }
   	}
}
 
 
