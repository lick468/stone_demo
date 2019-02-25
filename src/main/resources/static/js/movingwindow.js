var clickstatus = false;  
var lastX = 0;  
var lastY = 0;  
var lastcX = 0;  
var lastcY = 0;  
    function mousedown(e){  
        if(e.target.className.indexOf('com1')!=-1){  
            clickstatus = true;  
            var moveObject = document.getElementById('mydiv');  
            lastX = moveObject.offsetLeft;  
            lastY = moveObject.offsetTop;  
            lastcX = e.clientX;  
            lastcY = e.clientY;  
        }  
    }  
  
    function mousemove(e){  
        var moveObject = document.getElementById('mydiv');  
        if(clickstatus){  
            moveObject.style.left = lastX + (e.clientX - lastcX - 10) + 'px' ;  
            moveObject.style.top = lastY + (e.clientY-lastcY - 10) + 'px';  
        }  
    }  
  
    function mouseup(e){  
        clickstatus = false;  
        lastX=0;  
        lastY=0;  
        lastcX=0;  
        lastcY=0;  
    }  
  
document.addEventListener('mousedown',mousedown);  
document.addEventListener('mousemove',mousemove);  
document.addEventListener('mouseup',mouseup); 